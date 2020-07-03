package com.adosa.opensrp.chw.fp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adosa.opensrp.chw.fp.R;
import com.adosa.opensrp.chw.fp.contract.BaseFpProfileContract;
import com.adosa.opensrp.chw.fp.custom_views.BaseFpFloatingMenu;
import com.adosa.opensrp.chw.fp.domain.PathfinderFpMemberObject;
import com.adosa.opensrp.chw.fp.interactor.BasePathfinderFpProfileInteractor;
import com.adosa.opensrp.chw.fp.presenter.BasePathfinderFpProfilePresenter;
import com.adosa.opensrp.chw.fp.util.FpUtil;
import com.adosa.opensrp.chw.fp.util.PathfinderFamilyPlanningConstants;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.smartregister.domain.AlertStatus;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class BasePathfinderFpProfileActivity extends BaseProfileActivity implements BaseFpProfileContract.View {
    protected View lastVisitRow;
    protected LinearLayout recordFollowUpVisitLayout;
    protected RelativeLayout recordVisitStatusBarLayout;
    protected ImageView tickImage;
    protected TextView tvEditVisit;
    protected TextView tvUndo;
    protected TextView tvVisitDone;
    protected RelativeLayout rlLastVisitLayout;
    protected RelativeLayout rlUpcomingServices;
    protected RelativeLayout rlFamilyServicesDue;
    protected TextView tvLastVisitDay;
    protected TextView tvViewMedicalHistory;
    protected TextView tvUpComingServices;
    protected TextView tvFamilyStatus;
    protected TextView tvRecordFpFollowUp;
    protected TextView tvIntroductionToFp;
    protected TextView tvFpPregnancyScreening;
    protected TextView tvFpPregnancyTestFollowup;
    protected TextView tvRiskAssessment;
    protected TextView tvChooseFpMethod;
    protected TextView tvGiveFpMethod;
    protected TextView tvFpMethodRow;
    protected BaseFpProfileContract.Presenter fpProfilePresenter;
    protected BaseFpFloatingMenu fpFloatingMenu;
    protected PathfinderFpMemberObject pathfinderFpMemberObject;
    protected int numOfDays;
    private ProgressBar progressBar;
    private CircleImageView profileImageView;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvLocation;
    private TextView tvUniqueID;
    private View overDueRow;
    private View familyRow;
    private ImageRenderHelper imageRenderHelper;

    public static void startProfileActivity(Activity activity, PathfinderFpMemberObject memberObject) {
        Intent intent = new Intent(activity, BasePathfinderFpProfileActivity.class);
        intent.putExtra(PathfinderFamilyPlanningConstants.FamilyPlanningMemberObject.MEMBER_OBJECT, memberObject);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_base_pathfinder_fp_profile);

        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BasePathfinderFpProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        pathfinderFpMemberObject = (PathfinderFpMemberObject) getIntent().getSerializableExtra(PathfinderFamilyPlanningConstants.FamilyPlanningMemberObject.MEMBER_OBJECT);
        imageRenderHelper = new ImageRenderHelper(this);

        setupViews();

        initializePresenter();
        fetchProfileData();
        initializeCallFAB();
    }

    @Override
    protected void setupViews() {
        tvName = findViewById(R.id.textview_name);
        tvGender = findViewById(R.id.textview_gender);
        tvLocation = findViewById(R.id.textview_address);
        tvUniqueID = findViewById(R.id.textview_unique_id);
        recordVisitStatusBarLayout = findViewById(R.id.record_visit_status_bar_layout);
        recordFollowUpVisitLayout = findViewById(R.id.record_recurring_layout);
        lastVisitRow = findViewById(R.id.view_last_visit_row);
        overDueRow = findViewById(R.id.view_most_due_overdue_row);
        familyRow = findViewById(R.id.view_family_row);
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);
        rlLastVisitLayout = findViewById(R.id.rl_last_visit_layout);
        tvLastVisitDay = findViewById(R.id.textview_last_vist_day);
        tvViewMedicalHistory = findViewById(R.id.textview_medical_history);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);
        progressBar = findViewById(R.id.progress_bar);
        tickImage = findViewById(R.id.tick_image);
        tvVisitDone = findViewById(R.id.textview_visit_done);
        tvEditVisit = findViewById(R.id.textview_edit);
        tvUndo = findViewById(R.id.textview_undo);
        profileImageView = findViewById(R.id.imageview_profile);
        tvRecordFpFollowUp = findViewById(R.id.textview_record_reccuring_visit);
        tvIntroductionToFp = findViewById(R.id.textview_introduction_to_fp);
        tvFpPregnancyScreening = findViewById(R.id.textview_fp_pregnancy_screening);
        tvFpPregnancyTestFollowup = findViewById(R.id.textview_fp_pregnancy_test_followup);
        tvRiskAssessment = findViewById(R.id.textview_risk_assesment);
        tvChooseFpMethod = findViewById(R.id.textview_choose_fp_method);
        tvGiveFpMethod = findViewById(R.id.textview_give_fp_method);
        tvFpMethodRow = findViewById(R.id.textview_fp_method_date_row);

        tvUndo.setOnClickListener(this);
        tvEditVisit.setOnClickListener(this);
        tvRecordFpFollowUp.setOnClickListener(this);
        tvIntroductionToFp.setOnClickListener(this);
        tvFpPregnancyScreening.setOnClickListener(this);
        tvChooseFpMethod.setOnClickListener(this);
        tvGiveFpMethod.setOnClickListener(this);
        tvFpPregnancyTestFollowup.setOnClickListener(this);
        tvRiskAssessment.setOnClickListener(this);
        findViewById(R.id.rl_last_visit_layout).setOnClickListener(this);
        findViewById(R.id.rlUpcomingServices).setOnClickListener(this);
        findViewById(R.id.rlFamilyServicesDue).setOnClickListener(this);
        findViewById(R.id.rlFpRegistrationDate).setOnClickListener(this);
    }

    @Override
    protected void initializePresenter() {
        fpProfilePresenter = new BasePathfinderFpProfilePresenter(this, new BasePathfinderFpProfileInteractor(), pathfinderFpMemberObject);
    }

    public void initializeCallFAB() {
        if (StringUtils.isNotBlank(pathfinderFpMemberObject.getPhoneNumber())
                || StringUtils.isNotBlank(pathfinderFpMemberObject.getFamilyHeadPhoneNumber())) {
            fpFloatingMenu = new BaseFpFloatingMenu(this, pathfinderFpMemberObject);
            fpFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(fpFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        fpProfilePresenter.refreshProfileData();
        fpProfilePresenter.refreshProfileFpStatusInfo();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rl_last_visit_layout) {
            this.openMedicalHistory();
        } else if (id == R.id.rlUpcomingServices) {
            this.openUpcomingServices();
        } else if (id == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        } else if (id == R.id.textview_record_reccuring_visit) {
            this.openFollowUpVisitForm(false);
        } else if (id == R.id.textview_edit) {
            this.openFollowUpVisitForm(true);
        } else if (id == R.id.rlFpRegistrationDate) {
            this.openFamilyPlanningRegistration();
        } else if (id == R.id.textview_introduction_to_fp) {
            this.openFamilyPlanningIntroduction();
        } else if (id == R.id.textview_fp_pregnancy_screening) {
            this.openPregnancyScreening();
        } else if (id == R.id.textview_choose_fp_method) {
            this.openChooseFpMethod();
        } else if (id == R.id.textview_give_fp_method) {
            this.openGiveFpMethodButton();
        } else if (id == R.id.textview_fp_pregnancy_test_followup) {
            this.openPregnancyTestFollowup();
        } else if (id == R.id.textview_risk_assesment) {
            this.openRiskAssessment();
        }
    }

    @Override
    public void setupFollowupVisitEditViews(boolean isWithin24Hours) {
        if (isWithin24Hours) {
            recordFollowUpVisitLayout.setVisibility(View.GONE);
//            recordVisitStatusBarLayout.setVisibility(View.VISIBLE);
//            tvEditVisit.setVisibility(View.VISIBLE);
        } else {
            tvEditVisit.setVisibility(View.GONE);
            recordFollowUpVisitLayout.setVisibility(View.VISIBLE);
            recordVisitStatusBarLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void openMedicalHistory() {
        // TODO :: Open medical history view
    }

    @Override
    public void openFamilyPlanningRegistration() {
        // TODO :: Show registration form for edit
    }

    @Override
    public void openFamilyPlanningIntroduction() {
        // TODO :: Show Introduction to Family Planning
    }

    @Override
    public void openPregnancyScreening() {
        // TODO :: Show Pregnancy Screening Form
    }

    @Override
    public void openChooseFpMethod() {
        // TODO :: Show Choose Family Planning Method
    }

    @Override
    public void openGiveFpMethodButton() {
        // TODO :: Show give Family Planning Method
    }

    @Override
    public void openUpcomingServices() {
        // TODO :: Show upcoming services
    }

    @Override
    public void openFamilyDueServices() {
        // TODO :: Show family due services
    }

    @Override
    public void openFpRegistrationForm() {
        // TODO :: Show fp registration for edit
    }

    @Override
    public void openFollowUpVisitForm(boolean isEdit) {
        // TODO :: Open follow-up visit form for editing
    }

    @Override
    public void openPregnancyTestFollowup() {
        // TODO :: Show pregnancy test followup form
    }

    @Override
    public void openRiskAssessment() {
        // TODO :: Show Risk assessment form
    }

    @Override
    public void setUpComingServicesStatus(String service, AlertStatus status, Date date) {
        Timber.e("Coze :: setUpComingServicesStatus");
        Timber.e("Coze :: AlertStatus = "+new Gson().toJson(status));
        showProgressBar(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        if (status == AlertStatus.complete)
            return;
        overDueRow.setVisibility(View.VISIBLE);
        rlUpcomingServices.setVisibility(View.VISIBLE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(FpUtil.fromHtml(getString(R.string.fp_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(FpUtil.fromHtml(getString(R.string.fp_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void setFamilyStatus(AlertStatus status) {
        familyRow.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);

        if (status == AlertStatus.complete) {
            tvFamilyStatus.setText(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            tvFamilyStatus.setText(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(FpUtil.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    @Override
    public void setProfileViewDetails(PathfinderFpMemberObject pathfinderFpMemberObject) {
        int age = new Period(new DateTime(pathfinderFpMemberObject.getAge()), new DateTime()).getYears();
        tvName.setText(String.format(Locale.getDefault(), "%s %s %s, %d", pathfinderFpMemberObject.getFirstName(),
                pathfinderFpMemberObject.getMiddleName(), pathfinderFpMemberObject.getLastName(), age));
        tvGender.setText(pathfinderFpMemberObject.getGender());
        tvLocation.setText(pathfinderFpMemberObject.getAddress());
        tvUniqueID.setText(pathfinderFpMemberObject.getUniqueId());
        imageRenderHelper.refreshProfileImage(pathfinderFpMemberObject.getBaseEntityId(), profileImageView, FpUtil.getMemberProfileImageResourceIDentifier());
        tvFpMethodRow.setText(getFpMethodRowString(pathfinderFpMemberObject.getFpMethod(), pathfinderFpMemberObject.getFpStartDate(), pathfinderFpMemberObject.getFpRegistrationDate()));

        if (StringUtils.isNotBlank(pathfinderFpMemberObject.getFamilyHead()) && pathfinderFpMemberObject.getFamilyHead().equals(pathfinderFpMemberObject.getBaseEntityId())) {
            findViewById(R.id.fp_family_head).setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNotBlank(pathfinderFpMemberObject.getPrimaryCareGiver()) && pathfinderFpMemberObject.getPrimaryCareGiver().equals(pathfinderFpMemberObject.getBaseEntityId())) {
            findViewById(R.id.fp_primary_caregiver).setVisibility(View.VISIBLE);
        }
    }

    private CharSequence formatTime(String dateTime) {
        CharSequence timePassedString = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = df.parse(dateTime);
            timePassedString = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
        } catch (Exception e) {
            Timber.d(e);
        }
        return timePassedString;
    }

    private CharSequence formatTime(long timestamp) {
        CharSequence timePassedString = null;
        try {
            Date date = new Date(timestamp);
            timePassedString = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date);
        } catch (Exception e) {
            Timber.d(e);
        }
        return timePassedString;
    }

    public String getFpMethodRowString(String fpMethod, String fpStartDate, String fpRegistrationDate) {
        String fpMethodDisplayText;
        String fpDisplayDate = "";
        if (StringUtils.isNotEmpty(fpStartDate) || StringUtils.isNotEmpty(fpRegistrationDate)) {
            if (StringUtils.isNotEmpty(fpStartDate))
                fpDisplayDate = String.valueOf(formatTime(Long.parseLong(fpStartDate)));
            else
                fpDisplayDate = String.valueOf(fpRegistrationDate);
        }
        String insertionText = getString(R.string.fp_insertion);
        String startedText = getString(R.string.fp_started);
        String onText = getString(R.string.fp_on);

        switch (fpMethod) {
            case PathfinderFamilyPlanningConstants.DBConstants.FP_POP:
                fpMethodDisplayText = getString(R.string.pop) + " " + startedText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_COC:
                fpMethodDisplayText = getString(R.string.coc) + " " + startedText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_FEMALE_CONDOM:
                fpMethodDisplayText = getString(R.string.female_condom) + " " + startedText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_MALE_CONDOM:
                fpMethodDisplayText = getString(R.string.male_condom) + " " + startedText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_INJECTABLE:
                fpMethodDisplayText = getString(R.string.injectable) + " " + startedText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_IUD:
                fpMethodDisplayText = getString(R.string.iud) + " " + insertionText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_VASECTOMY:
                fpMethodDisplayText = getString(R.string.vasectomy);
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_TUBAL_LIGATION:
                fpMethodDisplayText = getString(R.string.tubal_ligation);
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_LAM:
                fpMethodDisplayText = getString(R.string.lam) + " " + insertionText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_IMPLANTS:
                fpMethodDisplayText = getString(R.string.implants) + " " + insertionText;
                break;
            case PathfinderFamilyPlanningConstants.DBConstants.FP_SDM:
                fpMethodDisplayText = getString(R.string.standard_day_method) + " " + startedText;
                break;
            case "0": //TODO coze update empty fp method to ""
                fpMethodDisplayText = getString(R.string.registered) + " ";
                break;
            default:
                fpMethodDisplayText = fpMethod + " " + getString(R.string.fp_started) + " " + onText;
        }

        return fpMethodDisplayText + " " + onText + " " + fpDisplayDate;
    }

    @Override
    public void updateLastVisitRow(Date lastVisitDate) {
        showProgressBar(false);
        if (lastVisitDate == null)
            return;

        tvLastVisitDay.setVisibility(View.VISIBLE);
        numOfDays = Days.daysBetween(new DateTime(lastVisitDate).toLocalDate(), new DateTime().toLocalDate()).getDays();
        tvLastVisitDay.setText(getString(R.string.last_visit_n_days_ago, (numOfDays <= 1) ? getString(R.string.less_than_twenty_four) : numOfDays + " " + getString(R.string.days)));
        rlLastVisitLayout.setVisibility(View.VISIBLE);
        lastVisitRow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMemberDetailsReloaded(PathfinderFpMemberObject pathfinderFpMemberObject) {
        setupViews();
        fetchProfileData();
    }

    @Override
    public void setFollowUpButtonDue() {
        showFollowUpVisitButton();
        tvRecordFpFollowUp.setBackground(getResources().getDrawable(R.drawable.record_fp_followup));
    }

    @Override
    public void setFollowUpButtonOverdue() {
        showFollowUpVisitButton();
        tvRecordFpFollowUp.setBackground(getResources().getDrawable(R.drawable.record_fp_followup_overdue));
    }

    @Override
    public void setPregnancyScreeningButtonDue() {
        showFpPregnancyScreeningButton();
        tvFpPregnancyScreening.setBackground(getResources().getDrawable(R.drawable.record_fp_followup));
    }
    @Override
    public void setPregnancyScreeningButtonOverdue() {
        showFpPregnancyScreeningButton();
        tvFpPregnancyScreening.setBackground(getResources().getDrawable(R.drawable.record_fp_followup_overdue));
    }


    @Override
    public void setPregnancyTestFollowupButtonDue() {
        showFpPregnancyTestFollowupButton();
        tvFpPregnancyTestFollowup.setBackground(getResources().getDrawable(R.drawable.record_fp_followup));
    }
    @Override
    public void setPregnancyTestFollowupButtonOverdue() {
        showFpPregnancyTestFollowupButton();
        tvFpPregnancyTestFollowup.setBackground(getResources().getDrawable(R.drawable.record_fp_followup_overdue));
    }

    @Override
    public void setFpMethodChoiceButtonDue() {
        showChooseFpMethodButton();
        tvChooseFpMethod.setBackground(getResources().getDrawable(R.drawable.record_fp_followup));
    }
    @Override
    public void setFpMethodChoiceButtonOverdue() {
        showChooseFpMethodButton();
        tvChooseFpMethod.setBackground(getResources().getDrawable(R.drawable.record_fp_followup_overdue));
    }

    @Override
    public void showFollowUpVisitButton() {
        tvRecordFpFollowUp.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIntroductionToFpButton() {
        tvIntroductionToFp.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFpPregnancyScreeningButton() {
        tvFpPregnancyScreening.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFpPregnancyTestFollowupButton() {
        tvFpPregnancyTestFollowup.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRiskAssessmentButton() {
        tvRiskAssessment.setVisibility(View.VISIBLE);
    }

    @Override
    public void showChooseFpMethodButton() {
        tvChooseFpMethod.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGiveFpMethodButton() {
        tvGiveFpMethod.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFollowUpVisitButton() {
        tvRecordFpFollowUp.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }
}
