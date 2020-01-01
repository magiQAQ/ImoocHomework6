package com.magi.imoochomework6.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

public class Cube extends View {

    private Bitmap bitmap;

    private Rect rect;

    public Cube(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect = new Rect(0, 0, w, h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //MeasureSpec.getMode()有三种情况
        //1.MeasureSpec.EXACTLY -> 精确尺寸，控件的宽高指定大小或者为match_parent
        //2.MeasureSpec.AT_MOST -> 最大尺寸，控件的宽高为WRAP_CONTENT，控件大小一般随着控件的子空间或内容进行变化，
        // 但是控件尺寸不能超过父控件允许的最大尺寸
        //3.MeasureSpec.UNSPECIFIED -> 未指定尺寸,需要多少就给多少,比如ScrollView的高度

        //先测量宽度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width;
        //EXACTLY表示用户确定的大小
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();
            //遇到wrap_content之类的情况时
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(needWidth, widthSize);
            } else {//MeasureSpec.UNSPECIFIED
                width = needWidth;
            }
        }

        //再测量高度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(heightSize, needHeight);
            } else {
                height = needHeight;
            }
        }

        setMeasuredDimension(width, height);
    }

    private int measureWidth() {
        return bitmap.getWidth();
    }

    private int measureHeight() {
        return bitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, null);
    }
}
