package com.kickstart.woc.wocdriverapp.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {

    public void replaceFragment(int containerId, String source, FragmentManager fragmentManager, Fragment destination) {

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(containerId, destination, destination.getTag());

        fragmentTransaction.addToBackStack(source);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
