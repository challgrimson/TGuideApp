package com.example.android.tguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.wdullaer.materialdatetimepicker.time.CircleView;

/**
 * Created by zacha_000 on 2018-02-23.
 * Animation Script to make gradient ring around welcome view image spin
 */

public class WelcomeAnimation extends View implements Runnable {
    private Paint paint;
    float degree = 90;

    public WelcomeAnimation(Context context) {
        super(context);
        init();
    }

    public  WelcomeAnimation (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WelcomeAnimation (Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        paint = new Paint();

        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w, h, cx, cy;
        w = getWidth();
        h = getHeight();
        cx = w/2;
        cy = h/2;
        float shaderCx = cx;
        float shaderCy = cy;
        SweepGradient gradient = new SweepGradient(shaderCx, shaderCy, Color.MAGENTA, Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);
        paint.setShader(gradient);
        canvas.rotate(degree, cx, cy);
        canvas.drawCircle(cx, cy, 550, paint);
    }

    @Override
    public void run() {
        while (this.isEnabled()) {//while this view is enabled
            try {
                degree = degree + 1;
                Thread.sleep(30);//30 fps
                this.postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
