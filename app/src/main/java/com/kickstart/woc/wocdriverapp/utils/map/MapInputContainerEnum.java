package com.kickstart.woc.wocdriverapp.utils.map;

import com.kickstart.woc.wocdriverapp.ui.fragments.DriverAvailabilityFragment;
import com.kickstart.woc.wocdriverapp.ui.fragments.DriverEnterRiderPinFragment;
import com.kickstart.woc.wocdriverapp.ui.fragments.DriverOnTripFragment;
import com.kickstart.woc.wocdriverapp.ui.fragments.DriverRideFoundFragment;
import com.kickstart.woc.wocdriverapp.ui.fragments.DriverTripSummaryFragment;
import com.kickstart.woc.wocdriverapp.ui.fragments.DriverVerificationFragment;

import java.io.Serializable;

public enum MapInputContainerEnum implements Serializable {

    DriverLoaderFragment,
    DriverVerificationFragment,
    DriverAvailabilityFragment,
    DriverRideFoundFragment,
    DriverEnterRiderPinFragment,
    DriverOnTripFragment,
    DriverTripSummaryFragment,
    Unknown;
}