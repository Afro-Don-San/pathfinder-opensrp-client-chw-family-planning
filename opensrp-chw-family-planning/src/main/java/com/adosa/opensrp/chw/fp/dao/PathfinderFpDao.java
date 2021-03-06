package com.adosa.opensrp.chw.fp.dao;

import com.adosa.opensrp.chw.fp.domain.PathfinderFpAlertObject;
import com.adosa.opensrp.chw.fp.domain.PathfinderFpMemberObject;
import com.adosa.opensrp.chw.fp.util.PathfinderFamilyPlanningConstants;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.dao.AbstractDao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class PathfinderFpDao extends AbstractDao {

    public static boolean isRegisteredForFp(String baseEntityID) {
        String sql = String.format(
                "select count(ec_family_planning.base_entity_id) count\n" +
                        "from ec_family_planning\n" +
                        "where base_entity_id = '%s'\n" +
                        "  and ec_family_planning.is_closed = 0\n" +
                        "  and ec_family_planning.ecp = 1", baseEntityID);

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return false;

        return res.get(0) > 0;
    }

    public static Visit getLatestVisit(String baseEntityId, String visitType) {
        String sql = "select visit_id, visit_type, parent_visit_id, visit_date from visits where base_entity_id = '" +
                baseEntityId + "' " +
                "and visit_type = '" + visitType + "' ORDER BY visit_date DESC LIMIT 1";
        List<Visit> visit = AbstractDao.readData(sql, getVisitDataMap());
        if (visit.size() == 0) {
            return null;
        }

        return visit.get(0);
    }

    public static PathfinderFpMemberObject getMember(String baseEntityID) {
        String sql = "select m.base_entity_id , m.unique_id , m.relational_id , m.dob , m.first_name , " +
                "m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , " +
                "f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town, f.nearest_facility , f.landmark,  f.gps ," +
                "fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , " +
                "fh.last_name family_head_last_name, fh.phone_number family_head_phone_number, " +
                "pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , " +
                "pcg.phone_number  pcg_phone_number , mr.* from ec_family_member m " +
                "inner join ec_family f on m.relational_id = f.base_entity_id " +
                "inner join ec_family_planning mr on mr.base_entity_id = m.base_entity_id " +
                "left join ec_family_member fh on fh.base_entity_id = f.family_head " +
                "left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver where m.base_entity_id ='" + baseEntityID + "' ";

        DataMap<PathfinderFpMemberObject> dataMap = cursor -> {
            PathfinderFpMemberObject memberObject = new PathfinderFpMemberObject();

            memberObject.setFirstName(getCursorValue(cursor, "first_name", ""));
            memberObject.setMiddleName(getCursorValue(cursor, "middle_name", ""));
            memberObject.setLastName(getCursorValue(cursor, "last_name", ""));
            JSONArray locationArray = null;
            try {
                locationArray = new JSONArray(getCursorValue(cursor, "nearest_facility"));
                memberObject.setAddress(locationArray.getString(locationArray.length()-1));
            } catch (JSONException e) {
                Timber.e(e);
                memberObject.setAddress(getCursorValue(cursor, "village_town"));
            }
            memberObject.setGender(getCursorValue(cursor, "gender"));
            memberObject.setUniqueId(getCursorValue(cursor, "unique_id", ""));
            memberObject.setAge(getCursorValue(cursor, "dob"));
            memberObject.setChoosePregnancyTestReferral(getCursorValue(cursor, "choose_pregnancy_test_referral",""));
            memberObject.setFamilyBaseEntityId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setRelationalId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setPrimaryCareGiver(getCursorValue(cursor, "primary_caregiver"));
            memberObject.setFamilyName(getCursorValue(cursor, "family_name", ""));
            memberObject.setPhoneNumber(getCursorValue(cursor, "phone_number", ""));
            memberObject.setBaseEntityId(getCursorValue(cursor, "base_entity_id", ""));
            memberObject.setFamilyHead(getCursorValue(cursor, "family_head", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "pcg_phone_number", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "family_head_phone_number", ""));
            memberObject.setFpStartDate(getCursorValue(cursor, "fp_start_date", ""));
            memberObject.setFpPregnancyScreeningDate(getCursorValue(cursor, "fp_pregnancy_screening_date", ""));
            memberObject.setFpMethodChoiceDate(getCursorValue(cursor, "fp_method_choice_date", ""));
            memberObject.setPillCycles(Integer.parseInt(getCursorValue(cursor, "no_pillcycles", "0")));
            memberObject.setFpMethod(getCursorValue(cursor, "fp_method_accepted", ""));
            memberObject.setPregnancyStatus(getCursorValue(cursor, "pregnancy_status", ""));
            memberObject.setFpRegistrationDate(getCursorValue(cursor, "fp_reg_date", ""));
            memberObject.setEdd(getCursorValue(cursor, "edd", ""));
            memberObject.setPeriodsRegularity(getCursorValue(cursor, "periods_regularity", ""));
            memberObject.setIssuedReferralService(getCursorValue(cursor, "issued_referral_service", ""));
            memberObject.setGps(getCursorValue(cursor, "gps", ""));
            memberObject.setLandmark(getCursorValue(cursor, "landmark", ""));

            memberObject.setManRequestedMethodForPartner(getCursorValue(cursor, "man_request_method_for_partner", "").equals("true"));
            memberObject.setDoesTheClientRememberLmp(getCursorValue(cursor, "does_the_client_remember_lmp", "").equals("yes"));
            memberObject.setClientAlreadyUsingFp(getCursorValue(cursor, "is_client_already_using_fp", "").equals("yes"));
            memberObject.setIntroductionToFamilyPlanningDone(getCursorValue(cursor, "introduction_to_fp_done", "").equals("true"));
            memberObject.setPregnancyScreeningDone(getCursorValue(cursor, "pregnant_screening_done", "").equals("true"));
            memberObject.setFpMethodChoiceDone(getCursorValue(cursor, "fp_method_choice_done", "").equals("true"));
            memberObject.setGiveFpMethodDone(getCursorValue(cursor, "give_fp_method_done", "").equals("true"));
            memberObject.setFpRiskAssessmentDone(getCursorValue(cursor, "fp_risk_assessment_done", "").equals("true"));
            memberObject.setFpFollowupDone(getCursorValue(cursor, "fp_followup_done", "").equals("true"));
            memberObject.setFpCitizenReportCardDone(getCursorValue(cursor, "fp_citizen_report_card_done", "").equals("true"));
            memberObject.setClientIsCurrentlyReferred(getCursorValue(cursor, "client_is_currently_referred", "").equals("true"));
            String familyHeadName = getCursorValue(cursor, "family_head_first_name", "") + " "
                    + getCursorValue(cursor, "family_head_middle_name", "");

            familyHeadName =
                    (familyHeadName.trim() + " " + getCursorValue(cursor, "family_head_last_name", "")).trim();
            memberObject.setFamilyHeadName(familyHeadName);

            String familyPcgName = getCursorValue(cursor, "pcg_first_name", "") + " "
                    + getCursorValue(cursor, "pcg_middle_name", "");

            familyPcgName =
                    (familyPcgName.trim() + " " + getCursorValue(cursor, "pcg_last_name", "")).trim();
            memberObject.setPrimaryCareGiverName(familyPcgName);

            return memberObject;
        };

        List<PathfinderFpMemberObject> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    @Nullable
    public static List<PathfinderFpAlertObject> getFpDetails(String baseEntityID) {
        String sql = "select fp_method_accepted, fp_start_date from ec_family_planning where base_entity_id = '" + baseEntityID + "' " +
                "and is_closed is 0 and ecp = 1";

        List<PathfinderFpAlertObject> pathfinderFpAlertObjects = AbstractDao.readData(sql, getVisitDetailsDataMap());
        if (pathfinderFpAlertObjects.size() == 0) {
            return null;
        }
        return pathfinderFpAlertObjects;
    }

    private static DataMap<PathfinderFpAlertObject> getVisitDetailsDataMap() {
        return c -> {
            PathfinderFpAlertObject pathfinderFpAlertObject = new PathfinderFpAlertObject();
            try {
                pathfinderFpAlertObject.setFpMethod(getCursorValue(c, "fp_method_accepted"));
                pathfinderFpAlertObject.setFpPillCycles(getCursorIntValue(c, "no_pillcycles"));
                pathfinderFpAlertObject.setFpStartDate(getCursorValue(c, "fp_start_date"));
            } catch (Exception e) {
                Timber.e(e.toString());
            }
            return pathfinderFpAlertObject;
        };
    }

    @Nullable
    public static Visit getLatestFpVisit(String baseEntityId, String entityType, String fpMethod) {
        String sql = " SELECT visit_date, visit_id,visit_type, parent_visit_id " +
                "FROM Visits v " +
                "INNER JOIN ec_family_planning fp on fp.base_entity_id = v.base_entity_id " +
                " WHERE v.base_entity_id = '" + baseEntityId + "' COLLATE NOCASE " +
                " AND v.visit_type = '" + entityType + "' COLLATE NOCASE " +
                " AND fp.fp_method_accepted = '" + fpMethod + "' COLLATE NOCASE "+
                " ORDER BY v.visit_date DESC";

        List<Visit> visit = AbstractDao.readData(sql, getVisitDataMap());
        if (visit.size() == 0) {
            return null;
        }
        return visit.get(0);
    }

    @Nullable
    public static Visit getLatestFpVisit(String baseEntityId) {
        String sql = " SELECT visit_date, visit_id,visit_type, parent_visit_id " +
                "FROM Visits v " +
                "INNER JOIN ec_family_planning fp on fp.base_entity_id = v.base_entity_id " +
                " WHERE v.base_entity_id = '" + baseEntityId + "' COLLATE NOCASE " +
                " AND (v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.FAMILY_PLANNING_REGISTRATION + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.INTRODUCTION_TO_FAMILY_PLANNING + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.FAMILY_PLANNING_PREGNANCY_SCREENING + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.CHOOSING_FAMILY_PLANNING_METHOD + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.FP_FOLLOW_UP_VISIT + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.FAMILY_PLANNING_METHOD_REFERRAL_FOLLOWUP + "' COLLATE NOCASE  OR " +
                "v.visit_type = '" + PathfinderFamilyPlanningConstants.EventType.GIVE_FAMILY_PLANNING_METHOD + "' COLLATE NOCASE) " +
                "ORDER BY v.visit_date DESC";
        List<Visit> visit = AbstractDao.readData(sql, getVisitDataMap());
        if (visit.size() == 0) {
            return null;
        }
        return visit.get(0);
    }

    private static DataMap<Visit> getVisitDataMap() {
        return c -> {
            Visit visit = new Visit();
            visit.setVisitId(getCursorValue(c, "visit_id"));
            visit.setParentVisitID(getCursorValue(c, "parent_visit_id"));
            visit.setVisitType(getCursorValue(c, "visit_type"));
            visit.setDate(getCursorValueAsDate(c, "visit_date"));

            return visit;
        };
    }


    @Nullable
    public static Visit getLatestInjectionVisit(String baseEntityId, String fpMethod) {
        String sql = "SELECT  substr(details,7,4) || '-' || substr(details,4,2) || '-' || substr(details,1,2) details " +
                " FROM visit_details vd " +
                " INNER JOIN visits v on vd.visit_id = v.visit_id " +
                " WHERE vd.visit_key = 'fp_refill_injectable' " +
                " AND v.base_entity_id = '" + baseEntityId + "' COLLATE NOCASE " +
                " AND strftime('%Y%d%m', (datetime(v.visit_date/1000, 'unixepoch', 'localtime'))) " +
                " >= ( SELECT substr(fp_start_date,7,4) || substr(fp_start_date,4,2) || substr(fp_start_date,1,2) FROM ec_family_planning WHERE base_entity_id = '" + baseEntityId + "' COLLATE NOCASE  AND fp_method_accepted = '" + fpMethod + "' COLLATE NOCASE) " +
                " ORDER BY vd.details DESC";

        List<Visit> visit = AbstractDao.readData(sql, getInjectionVisitDataMap());
        if (visit.size() == 0) {
            return null;
        }
        return visit.get(0);
    }

    private static DataMap<Visit> getInjectionVisitDataMap() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return c -> {
            Visit visit = new Visit();
            try {
                visit.setDate(sdf.parse(getCursorValue(c, "details")));
            } catch (Exception e) {
                Timber.e(e.toString());
            }
            return visit;
        };
    }

    public static Integer getFpWomenCount(String familyBaseEntityId) {
        String sql = "SELECT count(fp.base_entity_id) count " +
                "FROM ec_family_planning fp " +
                "INNER Join ec_family_member fm on fm.base_entity_id = fp.base_entity_id " +
                "WHERE fm.relational_id = '" + familyBaseEntityId + "' COLLATE NOCASE " +
                "AND fp.is_closed = 0 " +
                "AND fp.ecp = 1 ";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() == 0)
            return null;

        return res.get(0);
    }

    public static void closeFpMemberFromRegister(String baseEntityID) {
        String sql = "update ec_family_planning set is_closed = 1 where base_entity_id = '" + baseEntityID + "'";
        updateDB(sql);
    }


    @Nullable
    public static Integer getCountFpVisits(String baseEntityId, String entityType, String fpMethod) {
        String sql = " SELECT count(visit_id) visits " +
                "FROM Visits v " +
                "INNER JOIN ec_family_planning fp on fp.base_entity_id = v.base_entity_id " +
                " WHERE v.base_entity_id = '" + baseEntityId + "' COLLATE NOCASE " +
                " AND v.visit_type = '" + entityType + "' COLLATE NOCASE " +
                " AND fp.fp_method_accepted = '" + fpMethod + "' COLLATE NOCASE " +
                " AND strftime('%Y%d%m', (datetime(v.visit_date/1000, 'unixepoch')))  >= substr(fp.fp_start_date " +
                ",7,4) || substr(fp.fp_start_date " +
                ",4,2) || substr(fp.fp_start_date " +
                ",1,2) ";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "visits");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() == 0)
            return null;

        return res.get(0);
    }

    public static Integer getLastPillCycle(String baseEntityId, String fpMethod) {
        String sql = " SELECT vd.details as details " +
                " FROM visit_details vd " +
                " INNER JOIN visit_details vdd " +
                " on vd.visit_id  = vdd.visit_id " +
                "  INNER JOIN visits v on vd.visit_id = v.visit_id " +
                "  WHERE (vd.visit_key LIKE 'number_of_') " +
                "  AND (vdd.visit_key LIKE 'fp_method_accepted' or vdd.visit_key LIKE 'fp_method') " +
                "  AND (vdd.details LIKE '" + fpMethod + "' COLLATE NOCASE ) " +
                "  AND v.base_entity_id = '" + baseEntityId + "' COLLATE NOCASE " +
                "  ORDER by v.visit_date DESC " +
                "  LIMIT 1 ";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "details");

        List<String> res = readData(sql, dataMap);
//        return (res == null || res.size() == 0) ? 0 : Integer.parseInt(res.get(0));
        return 1;
    }
}

