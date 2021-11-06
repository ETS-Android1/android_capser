package com.example.globalcapsleagueapp.components;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.globalcapsleagueapp.R;
import com.example.globalcapsleagueapp.activity.SaveGameActivity;

public class SlideComponent extends View {

    private int offset;
    private int trackAlpha = 60;
    private final float maxOffset = 93;
    private final boolean right_mirror;
    private final Color color;
    private boolean pressed = false;
    private boolean alreadyActivated=false;
    private final Handler handler = new Handler();
    private Runnable runnable;


    private final Paint paint = new Paint();
    private final Paint paintLight = new Paint();


    public SlideComponent(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray arr = context.obtainStyledAttributes(attributeSet, R.styleable.TestComponent);
        right_mirror = arr.getBoolean(R.styleable.TestComponent_right_mirror, false);
        color =  Color.valueOf(arr.getColor(R.styleable.TestComponent_color,Color.RED));

        ValueAnimator animator = ValueAnimator.ofInt(trackAlpha,40);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000);
        animator.start();

        animator.addUpdateListener(animation -> {
            trackAlpha = (int)animation.getAnimatedValue();
            invalidate();
        });

        paint.setColor(color.toArgb());


        paint.setAntiAlias(true);
        paintLight.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec > heightMeasureSpec) {
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }

    }

    public void setSmoothOffset(int newOffset){
        if(right_mirror){
            newOffset=-newOffset;
        }

        if(runnable!=null || alreadyActivated){
            handler.removeCallbacks(runnable);
        }
        if(Math.abs(offset)>maxOffset){
            return;
        }

        if(Math.abs(newOffset)>70){
            alreadyActivated=true;
            callOnClick();
        }

        ValueAnimator animator = ValueAnimator.ofInt(offset,newOffset);
        animator.setDuration(100);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int)animation.getAnimatedValue();
                invalidate();
            }
        });

        runnable = () -> {
            if(!pressed) {
                ValueAnimator animator1 = ValueAnimator.ofInt(offset,0);
                animator1.setDuration(100);
                animator1.start();
                animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        offset = (int)animation.getAnimatedValue();
                        if(offset==0){
                            alreadyActivated=false;
                        }
                        invalidate();
                    }
                });

            }
        };
        handler.postDelayed(runnable,500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            pressed=false;
            if (offset > 70) {
                callOnClick();
            }
            offset = 0;
            invalidate();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            pressed=true;
            int[] position = new int[2];
            getLocationOnScreen(position);

            float motionInX;
            float motionInY ;
            if(!right_mirror) {
                motionInX = ev.getX() - scale(88f);
                motionInY = ev.getY() - scale(83f);
                if (motionInX > 0) {
                    motionInX = 0;
                }

                if (motionInY > 0) {
                    motionInY = 0;
                }
            } else {
                motionInX = ev.getX() - scale(12f);
                motionInY = ev.getY() - scale(83f);
                if (motionInX < 0) {
                    motionInX = 0;
                }

                if (motionInY > 0) {
                    motionInY = 0;
                }
            }


            float totalMotion = (float) Math.sqrt(Math.pow(motionInX, 2) + Math.pow(motionInY, 2)) / (getWidth() / 2f) * 45f;
            Log.i("Motion",String.valueOf(totalMotion));

            offset = (int) totalMotion;
            if (offset > 93f) {
                offset = 93;
            } else if (offset < 0) {
                offset = 0;
            }


            invalidate();
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        paintLight.setColor(Color.rgb(trackAlpha, trackAlpha, trackAlpha));

        if (!right_mirror) {
            Point center = new Point(canvas.getWidth(),(int)(5f/100f*getHeight()));
            int innerRadius =(int)(69/100f*getWidth());
            int outerRadius = (int)(91/100f*getWidth());
            int arcSweep = 65;
            int arcOffset = 110;

            RectF outer_rect = new RectF(center.x-outerRadius, center.y-outerRadius, center.x+outerRadius, center.y+outerRadius);
            RectF inner_rect = new RectF(center.x-innerRadius, center.y-innerRadius, center.x+innerRadius, center.y+innerRadius);

            Path path = new Path();
            path.arcTo(outer_rect, arcOffset, arcSweep);
            path.arcTo(inner_rect, arcOffset + arcSweep, -arcSweep);
            path.close();

            canvas.drawPath(path,paintLight);
            canvas.drawCircle( scale(72.2f),
                    scale(80f), 11f / 100f * getWidth(), paintLight);

            canvas.drawCircle( 20.4f / 100f * getWidth(),
                    12.5f / 100f * getWidth() , 11f / 100f * getWidth(), paintLight);

            canvas.drawCircle( scale(105f) - scale(85f)  * (float) Math.cos(((offset - maxOffset) / maxOffset * 70f) / 360f * 6.28f),
                    scale(13f) - scale(72f)* (float) Math.sin(((offset - maxOffset) / maxOffset * 70f) / 360f * 6.28f),
                    13f / 100f * getWidth(), paint);
        } else {
            Point center = new Point(0,(int)(5f/100f*getHeight()));
            int innerRadius =(int)(69/100f*getWidth());
            int outerRadius = (int)(91/100f*getWidth());
            int arcSweep = -65;
            int arcOffset = 70;

            RectF outer_rect = new RectF(center.x-outerRadius, center.y-outerRadius, center.x+outerRadius, center.y+outerRadius);
            RectF inner_rect = new RectF(center.x-innerRadius, center.y-innerRadius, center.x+innerRadius, center.y+innerRadius);

            Path path = new Path();
            path.arcTo(outer_rect, arcOffset, arcSweep);
            path.arcTo(inner_rect, arcOffset + arcSweep, -arcSweep);
            path.close();

            canvas.drawPath(path,paintLight);
            canvas.drawCircle( scale(100f-72.2f),
                    scale(80f) , 11f / 100f * getWidth(), paintLight);

            canvas.drawCircle( scale(100f-20.4f),
                    scale(12.5f), scale(11f), paintLight);

            canvas.drawCircle( scale(-5f) + scale( 85f)  * (float) Math.cos(((offset - maxOffset) / maxOffset * 70f) / 360f * 6.28f),
                    scale(13f) - scale(72f)* (float) Math.sin(((offset - maxOffset) / maxOffset * 70f) / 360f * 6.28f),
                    13f / 100f * getWidth(), paint);
        }
    }

    private float scale(float value){
        return value/100f*getWidth();
    }
}
