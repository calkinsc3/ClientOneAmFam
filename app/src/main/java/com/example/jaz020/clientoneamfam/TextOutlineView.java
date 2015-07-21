package com.example.jaz020.clientoneamfam;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by nsr009 on 7/21/2015.
 */
public class TextOutlineView extends TextView {

    private float strokeWidth;
    private Integer strokeColor;
    private Join strokeJoin;
    private float strokeMiter;

    private int[] lockedCompoundPadding;
    private boolean frozen = false;

    public TextOutlineView(Context context) {
        super(context);
        init(null);
    }

    public TextOutlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextOutlineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextOutlineView);

            String typefaceName = a.getString(R.styleable.TextOutlineView_typeface);

            if(typefaceName != null) {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), String.format("fonts/%s.ttf", typefaceName));
                setTypeface(tf);
            }

            if(a.hasValue(R.styleable.TextOutlineView_strokeColor)) {
                float strokeMiter = a.getDimensionPixelSize(R.styleable.TextOutlineView_strokeMiter, 10);
                float strokeWidth = a.getDimensionPixelSize(R.styleable.TextOutlineView_strokeWidth, 1);
                int strokeColor = a.getColor(R.styleable.TextOutlineView_strokeColor, 0xff000000);

                Join strokeJoin = null;

                switch(a.getInt(R.styleable.TextOutlineView_strokeJoinStyle, 0)) {
                    case(0):
                        strokeJoin = Join.MITER;
                        break;

                    case(1):
                        strokeJoin = Join.BEVEL;
                        break;

                    case(2):
                        strokeJoin = Join.ROUND;
                        break;

                    default:
                        break;
                }

                this.setStroke(strokeWidth, strokeColor, strokeJoin, strokeMiter);
            }
        }
    }

    public void setStroke(float width, int color) {
        setStroke(width, color, Join.MITER, 10);
    }

    public void setStroke(float width, int color, Join join, float miter) {
        strokeWidth = width;
        strokeColor = color;
        strokeJoin = join;
        strokeMiter = miter;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        freeze();

        int restoreColor = this.getCurrentTextColor();
        this.setCompoundDrawables(null,  null, null, null);
        this.setTextColor(restoreColor);

        if(strokeColor != null) {
            TextPaint paint = this.getPaint();

            paint.setStyle(Style.STROKE);
            paint.setStrokeJoin(strokeJoin);
            paint.setStrokeMiter(strokeMiter);

            this.setTextColor(strokeColor);
            paint.setStrokeWidth(strokeWidth);

            super.onDraw(canvas);

            paint.setStyle(Style.FILL);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.setTextColor(restoreColor);
        }

        this.setTextColor(restoreColor);

        unfreeze();
    }

    public void freeze() {
        lockedCompoundPadding = new int[]{
                getCompoundPaddingLeft(),
                getCompoundPaddingRight(),
                getCompoundPaddingTop(),
                getCompoundPaddingBottom()
        };

        frozen = true;
    }

    public void unfreeze(){
        frozen = false;
    }

    @Override
    public void requestLayout(){
        if(!frozen) super.requestLayout();
    }

    @Override
    public void postInvalidate(){
        if(!frozen) super.postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom){
        if(!frozen) super.postInvalidate(left, top, right, bottom);
    }

    @Override
    public void invalidate(){
        if(!frozen)	super.invalidate();
    }

    @Override
    public void invalidate(Rect rect){
        if(!frozen) super.invalidate(rect);
    }

    @Override
    public void invalidate(int l, int t, int r, int b){
        if(!frozen) super.invalidate(l,t,r,b);
    }

    @Override
    public int getCompoundPaddingLeft(){
        return !frozen ? super.getCompoundPaddingLeft() : lockedCompoundPadding[0];
    }

    @Override
    public int getCompoundPaddingRight(){
        return !frozen ? super.getCompoundPaddingRight() : lockedCompoundPadding[1];
    }

    @Override
    public int getCompoundPaddingTop(){
        return !frozen ? super.getCompoundPaddingTop() : lockedCompoundPadding[2];
    }

    @Override
    public int getCompoundPaddingBottom(){
        return !frozen ? super.getCompoundPaddingBottom() : lockedCompoundPadding[3];
    }
}