package com.adosa.opensrp.chw.fp.presenter;

import com.adosa.opensrp.chw.fp.contract.BaseFpRegisterFragmentContract;
import com.adosa.opensrp.chw.fp.util.PathfinderFamilyPlanningConstants;

import org.smartregister.configurableviews.model.Field;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BasePathfinderFpRegisterFragmentPresenter implements BaseFpRegisterFragmentContract.Presenter {

    protected WeakReference<BaseFpRegisterFragmentContract.View> viewReference;

    protected BaseFpRegisterFragmentContract.Model model;

    protected RegisterConfiguration config;

    protected Set<View> visibleColumns = new TreeSet<>();

    public BasePathfinderFpRegisterFragmentPresenter(BaseFpRegisterFragmentContract.View view, BaseFpRegisterFragmentContract.Model model) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.config = model.defaultRegisterConfiguration();
    }

    @Override
    public void updateSortAndFilter(List<Field> filterList, Field sortField) {
//        implement
    }

    @Override
    public String getMainCondition() {
        return " ec_family_member.date_removed is null AND ec_family_planning.is_closed = 0 AND ec_family_planning.ecp = 1";
    }

    @Override
    public String getDefaultSortQuery() {
        return "ec_family_planning.last_interacted_with DESC ";

    }

    @Override
    public void processViewConfigurations() {

        ViewConfiguration viewConfiguration = model.getViewConfiguration(PathfinderFamilyPlanningConstants.CONFIGURATION.FAMILY_PLANNING_REGISTER);
        if (viewConfiguration != null) {
            config = (RegisterConfiguration) viewConfiguration.getMetadata();
            this.visibleColumns = model.getRegisterActiveColumns(PathfinderFamilyPlanningConstants.CONFIGURATION.FAMILY_PLANNING_REGISTER);
        }

        if (config.getSearchBarText() != null && getView() != null) {
            getView().updateSearchBarHint(config.getSearchBarText());
        }
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = PathfinderFamilyPlanningConstants.DBConstants.FAMILY_PLANNING_TABLE;

        String countSelect = model.countSelect(tableName, "");
        String mainSelect = model.mainSelect(tableName, "");

        getView().initializeQueryParams(tableName, countSelect, mainSelect);
        getView().initializeAdapter(visibleColumns);

        getView().countExecute();
        getView().filterandSortInInitializeQueries();
    }

    protected BaseFpRegisterFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
//        implement
    }

    @Override
    public void searchGlobally(String s) {
//        implement

    }

    @Override
    public String getDueFilterCondition() {
//        TODO implement using schedule table for visit
        return " (cast( julianday(STRFTIME('%Y-%m-%d', datetime('now'))) -  julianday(IFNULL(SUBSTR(fp_reg_date,7,4)|| '-' || SUBSTR(fp_reg_date,4,2) || '-' || SUBSTR(fp_reg_date,1,2),'')) as integer) between 0 and 14) ";
    }


    @Override
    public String getMainTable() {
        return PathfinderFamilyPlanningConstants.DBConstants.FAMILY_PLANNING_TABLE;
    }
}
