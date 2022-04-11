package com.zeyaly.extractor.utils;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.view.animation.DecelerateInterpolator;

public class TransistionAnimation {
    public Transition enterTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(1000);

        return bounds;
    }

    public Transition returnTransition() {
        ChangeBounds bounds = new ChangeBounds();
        bounds.setInterpolator(new DecelerateInterpolator());
        bounds.setDuration(1000);

        return bounds;
    }
}
