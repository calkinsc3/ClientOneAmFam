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
 *  Custom text view that outlines text with white border
 *
 *  @author nreddy
 */
public class TextOutlineView extends TextView {

    private float strokeWidth;
    private Integer strokeColor;
    private Join strokeJoin;
    private float strokeMiter;

    private int[] lockedCompoundPadding;
    private boolean frozen = false;

    /**
     * Instantiates a new Text outline view.
     *
     * @param context the context
     */
    public TextOutlineView(Context context) {
        super(context);
        init(null);
    }

    /**
     * Instantiates a new Text outline view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public TextOutlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Instantiates a new Text outline view.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public TextOutlineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Init void.
     *
     * @param attrs the attrs
     */
    public void init(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextOutlineView);

            String typefaceName = a.getString(R.styleable.TextOutlineView_typeface);

            if(typefaceName != null) {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                        String.format("fonts/%s.ttf", typefaceName));
                setTypeface(tf);
            }

            if(a.hasValue(R.styleable.TextOutlineView_strokeColor)) {
                float strokeMiter = a.getDimensionPixelSize(
                        R.styleable.TextOutlineView_strokeMiter, 10);
                float strokeWidth = a.getDimensionPixelSize(
                        R.styleable.TextOutlineView_strokeWidth, 1);
                int strokeColor = a.getColor(
                        R.styleable.TextOutlineView_strokeColor, 0xff000000);

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

    /**
     * Sets stroke.
     *
     * @param width the width
     * @param color the color
     */
    public void setStroke(float width, int color) {
        setStroke(width, color, Join.MITER, 10);
    }

    /**
     * Sets stroke.
     *
     * @param width the width
     * @param color the color
     * @param join the join
     * @param miter the miter
     */
    public void setStroke(float width, int color, Join join, float miter) {
        strokeWidth = width;
        strokeColor = color;
        strokeJoin = join;
        strokeMiter = miter;
    }

    /**
     * On draw.
     *
     * @param canvas the canvas
     */
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

    /**
     * Freeze void.
     */
    public void freeze() {
        lockedCompoundPadding = new int[]{
                getCompoundPaddingLeft(),
                getCompoundPaddingRight(),
                getCompoundPaddingTop(),
                getCompoundPaddingBottom()
        };

        frozen = true;
    }

    /**
     * Unfreeze void.
     */
    public void unfreeze(){
        frozen = false;
    }

    /**
     * Request layout.
     */
    @Override
    public void requestLayout(){
        if(!frozen) super.requestLayout();
    }

    /**
     * Post invalidate.
     */
    @Override
    public void postInvalidate(){
        if(!frozen) super.postInvalidate();
    }

    /**
     * Post invalidate.
     *
     * @param left the left
     * @param top the top
     * @param right the right
     * @param bottom the bottom
     */
    @Override
    public void postInvalidate(int left, int top, int right, int bottom){
        if(!frozen) super.postInvalidate(left, top, right, bottom);
    }

    /**
     * Invalidate void.
     */
    @Override
    public void invalidate(){
        if(!frozen)	super.invalidate();
    }

    /**
     * Invalidate void.
     *
     * @param rect the rect
     */
    @Override
    public void invalidate(Rect rect){
        if(!frozen) super.invalidate(rect);
    }

    /**
     * Invalidate void.
     *
     * @param l the l
     * @param t the t
     * @param r the r
     * @param b the b
     */
    @Override
    public void invalidate(int l, int t, int r, int b){
        if(!frozen) super.invalidate(l,t,r,b);
    }

    /**
     * Get compound padding left.
     *
     * @return the int
     */
    @Override
    public int getCompoundPaddingLeft(){
        return !frozen ? super.getCompoundPaddingLeft() : lockedCompoundPadding[0];
    }

    /**
     * Get compound padding right.
     *
     * @return the int
     */
    @Override
    public int getCompoundPaddingRight(){
        return !frozen ? super.getCompoundPaddingRight() : lockedCompoundPadding[1];
    }

    /**
     * Get compound padding top.
     *
     * @return the int
     */
    @Override
    public int getCompoundPaddingTop(){
        return !frozen ? super.getCompoundPaddingTop() : lockedCompoundPadding[2];
    }

    /**
     * Get compound padding bottom.
     *
     * @return the int
     */
    @Override
    public int getCompoundPaddingBottom(){
        return !frozen ? super.getCompoundPaddingBottom() : lockedCompoundPadding[3];
    }
}