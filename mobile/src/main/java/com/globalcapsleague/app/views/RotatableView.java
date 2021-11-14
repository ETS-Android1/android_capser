package com.globalcapsleague.app.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.globalcapsleagueapp.R;

public class RotatableView extends ConstraintLayout {

    private float touchStart = 0f;
    private boolean animated = false;
    private boolean touched = false;

    private boolean top;
    private ValueAnimator animator;
    private Callback callback;


    public RotatableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.RotatableView);

        top = arr.getBoolean(R.styleable.RotatableView_top, false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!top) {
            this.setPivotY(0);
        } else {
            this.setPivotY(getMeasuredHeight());
        }
        this.setPivotX((float) getMeasuredWidth() / 2);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP && !animated) {
            ValueAnimator animator = ValueAnimator.ofInt((int)getRotationX(),0);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setRotationX((int)animation.getAnimatedValue());
                }
            });
            animator.start();
            touched = false;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            touchStart = ev.getRawY();
            touched = true;
        }

        if (animated || !touched) {
            return true;
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (ev.getRawY() < 0) {
                return true;
            }
            int[] position = new int[2];
            getLocationOnScreen(position);

            float motionInY;

            if(!top) {
                motionInY = getHeight() - ev.getRawY() - (getHeight() - touchStart);
            } else{
                motionInY =  ev.getRawY()  - touchStart;
            }

            int rotation = (int) (motionInY / getHeight() * 90f * 1.2f);

            if (rotation > 90) {
                rotation = 90;
            } else if (rotation < 0) {
                rotation = 0;
            }

            if (rotation > 70) {
                animated = true;
                touched = false;
                ValueAnimator valueAnimator = ValueAnimator.ofInt(top ? -70 : 70, top ? -90 : 90);
                valueAnimator.setDuration(100);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setRotationX((int) animation.getAnimatedValue());
                        if ((int) animation.getAnimatedValue() == 90
                                || (int)animation.getAnimatedValue()==-90) {
                            animated = false;
                            callOnClick();
                            setRotationX(0);
                        }
                        invalidate();
                    }
                });
                valueAnimator.start();
            }


            if(top){
                rotation*=-1;
            }


            setRotationX(rotation);


        }
        return true;
    }

    public void animateReverse(Callback callback){
        if(animator!=null && animator.isRunning()){
            animator.cancel();
        }
        this.callback = callback;
        animator = ValueAnimator.ofInt(top ? -90 :90, 0);
        animator.setDuration(350);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setRotationX((int) animation.getAnimatedValue());
                invalidate();
                if ((int) animation.getAnimatedValue() == 0) {
                    animated = false;
                    callback.call();
                }
            }
        });
        animator.start();

    }

    public interface Callback{
        void call();
    }


}
