package com.example.globalcapsleagueapp.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.lang.reflect.Type;

public class CurvedText extends androidx.appcompat.widget.AppCompatTextView {

        private final Paint paint;
    private final Path path;

    public CurvedText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setText("TEST");

        path = new Path();
        paint = new Paint();

        int value = 180;
        paint.setColor(Color.rgb(value, value, value));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }

        paint.setTextSize(9f / 100f * getMeasuredWidth());
        path.addArc(0, 0, getMeasuredHeight(), getMeasuredWidth(), 260, 20);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawTextOnPath(getText().toString(), path, 0, 8f / 100f * getWidth(), paint);
    }
};