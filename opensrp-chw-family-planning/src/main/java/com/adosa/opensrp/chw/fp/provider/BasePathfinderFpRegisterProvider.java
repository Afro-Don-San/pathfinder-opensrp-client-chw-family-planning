package com.adosa.opensrp.chw.fp.provider;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adosa.opensrp.chw.fp.R;
import com.adosa.opensrp.chw.fp.dao.PathfinderFpDao;
import com.adosa.opensrp.chw.fp.domain.PathfinderFpMemberObject;
import com.adosa.opensrp.chw.fp.fragment.BasePathfinderFpRegisterFragment;
import com.adosa.opensrp.chw.fp.util.FpUtil;
import com.adosa.opensrp.chw.fp.util.PathfinderFamilyPlanningConstants;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;
import java.util.Set;

import timber.log.Timber;

import static org.smartregister.util.Utils.getName;

public class BasePathfinderFpRegisterProvider implements RecyclerViewProvider<BasePathfinderFpRegisterProvider.RegisterViewHolder> {

    private final LayoutInflater inflater;
    protected View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;
    private Context context;
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;

    public BasePathfinderFpRegisterProvider(Context context, View.OnClickListener paginationClickListener, View.OnClickListener onClickListener, Set visibleColumns) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.paginationClickListener = paginationClickListener;
        this.onClickListener = onClickListener;
        this.visibleColumns = visibleColumns;
        this.context = context;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient smartRegisterClient, RegisterViewHolder registerViewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        PathfinderFpMemberObject pathfinderFpMemberObject = PathfinderFpDao.getMember(pc.getCaseId());

        if (visibleColumns.isEmpty()) {
            populatePatientColumn(pc, pathfinderFpMemberObject, registerViewHolder);
        }
    }

    private void populatePatientColumn(CommonPersonObjectClient pc, PathfinderFpMemberObject pathfinderFpMemberObject, final RegisterViewHolder viewHolder) {
        try {
            String firstName = getName(
                    pathfinderFpMemberObject.getFirstName(),
                    pathfinderFpMemberObject.getMiddleName());

            String dobString = Utils.getValue(pc.getColumnmaps(), PathfinderFamilyPlanningConstants.DBConstants.DOB, false);
            int age = new Period(new DateTime(dobString), new DateTime()).getYears();

            String patientName = getName(firstName, pathfinderFpMemberObject.getLastName());
            String methodAccepted = FpUtil.getTranslatedMethodValue(pathfinderFpMemberObject.getFpMethod(), context);
            viewHolder.patientName.setText(patientName + ", " + age);
            viewHolder.textViewFpMethod.setText(methodAccepted);

            if (methodAccepted.equals("") || methodAccepted.equals("0")) {
                viewHolder.textViewFpMethod.setVisibility(View.GONE);
            }

            viewHolder.textViewVillage.setText(pathfinderFpMemberObject.getAddress());
            viewHolder.patientColumn.setOnClickListener(onClickListener);
            viewHolder.patientColumn.setTag(pc);
            viewHolder.patientColumn.setTag(R.id.VIEW_ID, BasePathfinderFpRegisterFragment.CLICK_VIEW_NORMAL);

            viewHolder.dueButton.setOnClickListener(onClickListener);
            viewHolder.dueButton.setTag(pc);
            viewHolder.dueButton.setTag(R.id.VIEW_ID, BasePathfinderFpRegisterFragment.FOLLOW_UP_VISIT);
            viewHolder.registerColumns.setOnClickListener(onClickListener);

            viewHolder.registerColumns.setOnClickListener(v -> viewHolder.patientColumn.performClick());
            viewHolder.registerColumns.setOnClickListener(v -> viewHolder.dueButton.performClick());

        } catch (Exception e) {
            Timber.e(e);
        }
    }


    @Override
    public void getFooterView(RecyclerView.ViewHolder viewHolder, int currentPageCount, int totalPageCount, boolean hasNext, boolean hasPrevious) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
        footerViewHolder.pageInfoView.setText(MessageFormat.format(context.getString(org.smartregister.R.string.str_page_info), currentPageCount, totalPageCount));

        footerViewHolder.nextPageView.setVisibility(hasNext ? View.VISIBLE : View.INVISIBLE);
        footerViewHolder.previousPageView.setVisibility(hasPrevious ? View.VISIBLE : View.INVISIBLE);

        footerViewHolder.nextPageView.setOnClickListener(paginationClickListener);
        footerViewHolder.previousPageView.setOnClickListener(paginationClickListener);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption filterOption, ServiceModeOption serviceModeOption, FilterOption filterOption1, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
//        implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String s, String s1, String s2) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public RegisterViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.family_planning_register_list_row, parent, false);
        return new RegisterViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder createFooterHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.smart_register_pagination, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public boolean isFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        return viewHolder instanceof FooterViewHolder;
    }

    public class RegisterViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView parentName;
        public TextView textViewVillage;
        public TextView textViewFpMethod;
        public Button dueButton;
        public View patientColumn;

        public View registerColumns;
        public View dueWrapper;

        public RegisterViewHolder(View itemView) {
            super(itemView);

            parentName = itemView.findViewById(R.id.patient_parent_name);
            patientName = itemView.findViewById(R.id.patient_name_age);
            textViewVillage = itemView.findViewById(R.id.text_view_village);
            textViewFpMethod = itemView.findViewById(R.id.text_view_fp_method);
            dueButton = itemView.findViewById(R.id.due_button);
            patientColumn = itemView.findViewById(R.id.patient_column);
            registerColumns = itemView.findViewById(R.id.register_columns);
            dueWrapper = itemView.findViewById(R.id.due_button_wrapper);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView pageInfoView;
        public Button nextPageView;
        public Button previousPageView;

        public FooterViewHolder(View view) {
            super(view);

            nextPageView = view.findViewById(org.smartregister.R.id.btn_next_page);
            previousPageView = view.findViewById(org.smartregister.R.id.btn_previous_page);
            pageInfoView = view.findViewById(org.smartregister.R.id.txt_page_info);
        }
    }
}
