package com.example.jaz020.clientoneamfam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by nsr009 on 7/21/2015.
 */
public class TextOutlineView extends TextView {

    public TextOutlineView(Context context) {
        super(context);
    }

    public TextOutlineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint strokePaint = new Paint();

        strokePaint.setARGB(255, 0, 0, 0);
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setTextSize(16);
        strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3);

        Paint textPaint = new Paint();

        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(16);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        Path path = new Path();

        String text = (String) getText();
        Log.i("TEXT", text);

        textPaint.getTextPath(text, 0, text.length(), 0, 100, path);

        canvas.drawPath(path, strokePaint);
        canvas.drawPath(path, textPaint);

//        canvas.drawText("Some Text", 100, 100, strokePaint);
//        canvas.drawText("Some Text", 100, 100, textPaint);

        super.draw(canvas);
    }
}