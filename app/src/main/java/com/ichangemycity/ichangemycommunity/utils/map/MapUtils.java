package com.ichangemycity.ichangemycommunity.utils.map;

import android.animation.ObjectAnimator;
import android.widget.FrameLayout;

public class MapUtils {

    private static final int interval = 800;

    public void expandMapAnimation(FrameLayout mMapViewContainer, int mMapOldSize, int mMapNewSize,
                                   FrameLayout mInputViewContainer, int mTextOldSize, int mTextNewSize) {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapViewContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                mMapOldSize,
                mMapNewSize);
        mapAnimation.setDuration(interval);

        ViewWeightAnimationWrapper textAnimationWrapper = new ViewWeightAnimationWrapper(mInputViewContainer);
        ObjectAnimator textAnimation = ObjectAnimator.ofFloat(textAnimationWrapper,
                "weight",
                mTextOldSize,
                mTextNewSize);
        textAnimation.setDuration(interval);

        textAnimation.start();
        mapAnimation.start();
    }

    public void contractMapAnimation(FrameLayout mMapViewContainer, int mMapOldSize, int mMapNewSize,
                                     FrameLayout mInputViewContainer, int mTextOldSize, int mTextNewSize) {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapViewContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                mMapOldSize,
                mMapNewSize);
        mapAnimation.setDuration(interval);

        ViewWeightAnimationWrapper textAnimationWrapper = new ViewWeightAnimationWrapper(mInputViewContainer);
        ObjectAnimator textAnimation = ObjectAnimator.ofFloat(textAnimationWrapper,
                "weight",
                mTextOldSize,
                mTextNewSize);
        textAnimation.setDuration(interval);

        textAnimation.start();
        mapAnimation.start();
    }
}
