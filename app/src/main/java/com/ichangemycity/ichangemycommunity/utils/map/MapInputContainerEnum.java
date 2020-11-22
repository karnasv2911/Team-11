package com.ichangemycity.ichangemycommunity.utils.map;

import com.ichangemycity.ichangemycommunity.ui.fragments.SurveyCategoryFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.SurveyFormFragment;
import com.ichangemycity.ichangemycommunity.ui.fragments.SurveySubCategoryFragment;

import java.io.Serializable;

public enum MapInputContainerEnum implements Serializable {

    LoaderFragment,
    SurveyCategoryFragment,
    SurveySubCategoryFragment,
    SurveyFormFragment,
    Unknown;
}