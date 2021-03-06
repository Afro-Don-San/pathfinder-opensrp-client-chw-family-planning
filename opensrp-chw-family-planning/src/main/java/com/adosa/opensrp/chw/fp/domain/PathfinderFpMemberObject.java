package com.adosa.opensrp.chw.fp.domain;

import java.io.Serializable;

public class PathfinderFpMemberObject implements Serializable {

    private String familyHeadName;
    private String familyHeadPhoneNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String gender;
    private String uniqueId;
    private String age;
    private String relationalid;
    private String details;
    private String baseEntityId;
    private String relationalId;
    private String primaryCareGiver;
    private String primaryCareGiverName;
    private String primaryCareGiverPhone;
    private String familyHead;
    private String familyBaseEntityId;
    private String familyName;
    private String phoneNumber;
    private String fpStartDate;
    private String fpRegistrationDate;
    private String edd;
    private int pillCycles;
    private String fpMethod;
    private String pregnancyStatus;
    private String choosePregnancyTestReferral;
    private String fpPregnancyScreeningDate;
    private String fpMethodChoiceDate;
    private String periodsRegularity;
    private String issuedReferralService;
    private String gps;
    private String landmark;
    private boolean introductionToFamilyPlanningDone;
    private boolean pregnancyScreeningDone;
    private boolean fpMethodChoiceDone;
    private boolean giveFpMethodDone;
    private boolean isClientAlreadyUsingFp;
    private boolean doesTheClientRememberLmp;
    private boolean manRequestedMethodForPartner;
    private boolean fpRiskAssessmentDone;
    private boolean fpFollowupDone;
    private boolean fpCitizenReportCardDone;
    private boolean clientIsCurrentlyReferred;

    public PathfinderFpMemberObject() {
    }

    public String getFamilyHeadName() {
        return familyHeadName;
    }

    public void setFamilyHeadName(String familyHeadName) {
        this.familyHeadName = familyHeadName;
    }

    public String getFamilyHeadPhoneNumber() {
        return familyHeadPhoneNumber;
    }

    public void setFamilyHeadPhoneNumber(String familyHeadPhoneNumber) {
        this.familyHeadPhoneNumber = familyHeadPhoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRelationalid() {
        return relationalid;
    }

    public void setRelationalid(String relationalid) {
        this.relationalid = relationalid;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getRelationalId() {
        return relationalId;
    }

    public void setRelationalId(String relationalId) {
        this.relationalId = relationalId;
    }

    public String getPrimaryCareGiver() {
        return primaryCareGiver;
    }

    public void setPrimaryCareGiver(String primaryCareGiver) {
        this.primaryCareGiver = primaryCareGiver;
    }

    public String getPrimaryCareGiverName() {
        return primaryCareGiverName;
    }

    public void setPrimaryCareGiverName(String primaryCareGiverName) {
        this.primaryCareGiverName = primaryCareGiverName;
    }

    public String getPrimaryCareGiverPhone() {
        return primaryCareGiverPhone;
    }

    public void setPrimaryCareGiverPhone(String primaryCareGiverPhone) {
        this.primaryCareGiverPhone = primaryCareGiverPhone;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public String getFamilyBaseEntityId() {
        return familyBaseEntityId;
    }

    public void setFamilyBaseEntityId(String familyBaseEntityId) {
        this.familyBaseEntityId = familyBaseEntityId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFpStartDate() {
        return fpStartDate;
    }

    public void setFpStartDate(String fpStartDate) {
        this.fpStartDate = fpStartDate;
    }

    public int getPillCycles() {
        return pillCycles;
    }

    public void setPillCycles(int pillCycles) {
        this.pillCycles = pillCycles;
    }

    public String getFpMethod() {
        return fpMethod;
    }

    public void setFpMethod(String fpMethod) {
        this.fpMethod = fpMethod;
    }

    public String getFpRegistrationDate() {
        return fpRegistrationDate;
    }

    public void setFpRegistrationDate(String fpRegistrationDate) {
        this.fpRegistrationDate = fpRegistrationDate;
    }

    public String getPregnancyStatus() {
        return pregnancyStatus;
    }

    public void setPregnancyStatus(String pregnancyStatus) {
        this.pregnancyStatus = pregnancyStatus;
    }

    public boolean isIntroductionToFamilyPlanningDone() {

        return introductionToFamilyPlanningDone;
    }

    public void setIntroductionToFamilyPlanningDone(boolean introductionToFamilyPlanningDone) {
        this.introductionToFamilyPlanningDone = introductionToFamilyPlanningDone;
    }

    public boolean isPregnancyScreeningDone() {
        return pregnancyScreeningDone;
    }

    public void setPregnancyScreeningDone(boolean pregnancyScreeningDone) {
        this.pregnancyScreeningDone = pregnancyScreeningDone;
    }

    public boolean isFpMethodChoiceDone() {
        return fpMethodChoiceDone;
    }

    public void setFpMethodChoiceDone(boolean fpMethodChoiceDone) {
        this.fpMethodChoiceDone = fpMethodChoiceDone;
    }

    public boolean isClientAlreadyUsingFp() {
        return isClientAlreadyUsingFp;
    }

    public void setClientAlreadyUsingFp(boolean clientAlreadyUsingFp) {
        isClientAlreadyUsingFp = clientAlreadyUsingFp;
    }

    public String getEdd() {
        return edd;
    }

    public void setEdd(String edd) {
        this.edd = edd;
    }

    public String getChoosePregnancyTestReferral() {
        return choosePregnancyTestReferral;
    }

    public void setChoosePregnancyTestReferral(String choosePregnancyTestReferral) {
        this.choosePregnancyTestReferral = choosePregnancyTestReferral;
    }

    public String getFpPregnancyScreeningDate() {
        return fpPregnancyScreeningDate;
    }

    public void setFpPregnancyScreeningDate(String fpPregnancyScreeningDate) {
        this.fpPregnancyScreeningDate = fpPregnancyScreeningDate;
    }

    public String getPeriodsRegularity() {
        return periodsRegularity;
    }

    public void setPeriodsRegularity(String periodsRegularity) {
        this.periodsRegularity = periodsRegularity;
    }

    public boolean isDoesTheClientRememberLmp() {
        return doesTheClientRememberLmp;
    }

    public void setDoesTheClientRememberLmp(boolean doesTheClientRememberLmp) {
        this.doesTheClientRememberLmp = doesTheClientRememberLmp;
    }

    public String getFpMethodChoiceDate() {
        return fpMethodChoiceDate;
    }

    public void setFpMethodChoiceDate(String fpMethodChoiceDate) {
        this.fpMethodChoiceDate = fpMethodChoiceDate;
    }

    public boolean isManRequestedMethodForPartner() {
        return manRequestedMethodForPartner;
    }

    public void setManRequestedMethodForPartner(boolean manRequestedMethodForPartner) {
        this.manRequestedMethodForPartner = manRequestedMethodForPartner;
    }

    public boolean isGiveFpMethodDone() {
        return giveFpMethodDone;
    }

    public void setGiveFpMethodDone(boolean giveFpMethodDone) {
        this.giveFpMethodDone = giveFpMethodDone;
    }

    public boolean isFpRiskAssessmentDone() {
        return fpRiskAssessmentDone;
    }

    public void setFpRiskAssessmentDone(boolean fpRiskAssessmentDone) {
        this.fpRiskAssessmentDone = fpRiskAssessmentDone;
    }

    public boolean isFpFollowupDone() {
        return fpFollowupDone;
    }

    public void setFpFollowupDone(boolean fpFollowupDone) {
        this.fpFollowupDone = fpFollowupDone;
    }

    public boolean isFpCitizenReportCardDone() {
        return fpCitizenReportCardDone;
    }

    public void setFpCitizenReportCardDone(boolean fpCitizenReportCardDone) {
        this.fpCitizenReportCardDone = fpCitizenReportCardDone;
    }

    public String getIssuedReferralService() {
        return issuedReferralService;
    }

    public void setIssuedReferralService(String issuedReferralService) {
        this.issuedReferralService = issuedReferralService;
    }

    public boolean isClientIsCurrentlyReferred() {
        return clientIsCurrentlyReferred;
    }

    public void setClientIsCurrentlyReferred(boolean clientIsCurrentlyReferred) {
        this.clientIsCurrentlyReferred = clientIsCurrentlyReferred;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
