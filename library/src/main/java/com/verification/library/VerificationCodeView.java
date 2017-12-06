package com.verification.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rankaifeng on 2017/12/5.
 */

public class VerificationCodeView extends View {
    private Paint mPaint;
    private Rect mBound;  
    /**
     * 绘制文本内容
     */
    private String mTitleText = "";
    /**
     * 文字颜色
     */
    private int mTextColor = 0;
    /**
     * 文字大小
     */
    private int mTextSize = 0;

    /**
     * 背景色
     *
     * @param context
     */
    private int mBackgroundColor = 0;


    public VerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        mTitleText = typedArray.getString(R.styleable.CustomView_titleText);
        mTextColor = typedArray.getColor(R.styleable.CustomView_textColor, Color.RED);//默认红色
        mBackgroundColor = typedArray.getColor(R.styleable.CustomView_backgroundColor, Color.YELLOW);//背景默认为黄色
        // 默认设置为16sp，TypeValue也可以把sp转化为px
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomView_textSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        typedArray.recycle();
        initPaint();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleText = randomText();
                postInvalidate();
            }
        });
    }

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔 获取绘制文本的宽高
     */
    private void initPaint() {
        mBound = new Rect();
        mPaint = new Paint();
        /*设置画笔的大小*/
        mPaint.setTextSize(mTextSize);
        /*获得绘制文本的宽高*/
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);

        /*画笔抗锯齿*/
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制黄色的背景
         */
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        /*重新设置画笔的颜色用于绘制中间的文字*/
        mPaint.setColor(mTextColor);
        /**
         * 绘制中间的文字
         * 1.x坐标为黄色背景宽度的一半减去绘制文本宽度的一半
         * 2.y左边为黄色背景高度的一半加上绘制文本高度的一半
         * 这样才能保证绘制的文本在黄色背景的正中央
         */
        canvas.drawText(mTitleText,
                getWidth() / 2 - mBound.width() / 2,
                getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    /**
     * 重写此方法是为了适配在布局文件中的 对控件设置wrap_content这属性
     * 如果不重写此方法 就算在布局文件中设置了wrap_content但实际效果还是
     * 会填充满整个屏幕
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int mWidth, mHeight;
        if (mWidthMode == MeasureSpec.EXACTLY) {
            mWidth = mWidthSize;
        } else {
            int textWidth = mBound.width();
            mWidth = getPaddingLeft() + textWidth + getPaddingRight();
        }
        if (mHeightMode == MeasureSpec.EXACTLY) {
            mHeight = mHeightSize;
        } else {
            int textHeight = mBound.height();
            mHeight = getPaddingTop() + textHeight + getPaddingBottom();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * 获取随机四位数并拼接好
     *
     * @return
     */
    private String randomText() {
        Random random = new Random();
        List<Integer> integerList = new ArrayList<>();
        while (integerList.size() < 4) {
            int randomInt = random.nextInt(10);
            integerList.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : integerList) {
            sb.append("" + i);
        }
        return sb.toString();
    }
}
