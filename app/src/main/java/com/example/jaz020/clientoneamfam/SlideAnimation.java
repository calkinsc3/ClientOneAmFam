package com.example.jaz020.clientoneamfam;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


/**
 * handles all sliding animation
 *
 * @author jziglinski
 */
public class SlideAnimation extends Animation {

    private static final float SPEED = 0.5f;

    private float mStart;
    private float mEnd;
    private View myView;

    /**
     * Instantiates a new Slide animation.
     *
     * @param fromX the from x
     * @param toX the to x
     * @param v the v
     */
    public SlideAnimation(float fromX, float toX , View v) {
        mStart = fromX;
        mEnd = toX;
        myView = v;

        setInterpolator(new LinearInterpolator());

        float duration = Math.abs(mEnd - mStart) / SPEED;
        setDuration((long) duration);
    }

    /**
     * Apply transformation.
     *
     * @param interpolatedTime the interpolated time
     * @param t the t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float offset = (mEnd - mStart) * interpolatedTime + mStart;
        myView.setX(offset);
    }
}