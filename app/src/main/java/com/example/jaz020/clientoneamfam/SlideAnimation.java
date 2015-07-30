package com.example.jaz020.clientoneamfam;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


/**
 * Created by jaz020 on 7/23/2015.
 */
public class SlideAnimation extends Animation {

    private static final float SPEED = 0.5f;

    private float mStart;
    private float mEnd;
    private View myView;

    public SlideAnimation(float fromX, float toX , View v) {
        mStart = fromX;
        mEnd = toX;
        myView = v;

        setInterpolator(new LinearInterpolator());

        float duration = Math.abs(mEnd - mStart) / SPEED;
        setDuration((long) duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float offset = (mEnd - mStart) * interpolatedTime + mStart;
        myView.setX(offset);
    }
}