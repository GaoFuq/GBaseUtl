package com.gfq.gbaseutl.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gfq.gbaseutl.R;


/**
 * 自定义评价星星类(任何形状)
 * yueleng  on 2017/1/9.
 * 设置显示几颗星
 * 5.0f代表五颗星，4.2代表四颗星加上第五颗星得五分之一
 * star.setMark(2.0f);
 * star.getMark();
 * <p>
 * <p>
 * 设置显示几颗星
 * 5.0f代表五颗星，4.2代表四颗星加上第五颗星得五分之一
 * <p>
 * star.setMark(2.0f);
 * <p>
 * star.getMark();
 * <p>
 * 设置控件监听
 * <p>
 * <p>
 * star.setStarChangeLister(newStar.OnStarChangeListener() {
 *
 * @Override public voidonStarChange(Float mark) {
 * <p>
 * }
 * <p>
 * });
 */

/**
 *设置显示几颗星
 * 5.0f代表五颗星，4.2代表四颗星加上第五颗星得五分之一
 *
 *         star.setMark(2.0f);
 *
 *         star.getMark();
 */


/**
 *设置控件监听
 *
 *
 *         star.setStarChangeLister(newStar.OnStarChangeListener() {
 *
 *              @Override
 *              public voidonStarChange(Float mark) {
 *
 *               }
 *
 *         });

 */

/**
 *                          <com.cq1080.weike.view.Star
 *                             android:id="@+id/quality_star"
 *                             android:layout_width="wrap_content"
 *                             android:layout_height="wrap_content"
 *                             android:layout_marginStart="@dimen/dp_15"
 *                             app:isStatic="true"
 *                             app:starBackground="@mipmap/ic_star_2"
 *                             app:starClickable="false"
 *                             app:starDistance="3dp"
 *                             app:starDrawBackground="@mipmap/ic_star_1"
 *                             app:starHeight="18dp"
 *                             app:starWidth="18dp"
 *                             app:starsNum="5" />
 */


public class Star extends View {
    //星星评分
    private float starMark = 0.0F;
    //星星个数
    private int starNum = 5;
    //星星高度
    private int starHeight;
    //星星宽度
    private int starWidth;
    //星星间距
    private int starDistance;
    //星星背景
    private Drawable starBackgroundBitmap;
    //动态星星
    private Bitmap starDrawDrawable;
    //星星变化监听
    private OnStarChangeListener changeListener;
    //是否可以点击
    private boolean isClick = true;
    //画笔
    private Paint mPaint;
    private boolean isStatic = false;//静止模式，即只显示
    private boolean isInt = false;//评分只能为整数

    public Star(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        init(mContext, attrs);
    }

    public Star(Context mContext, AttributeSet attrs, int defStyleAttr) {
        super(mContext, attrs, defStyleAttr);
        init(mContext, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {

        //初始化控件属性
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.Star);
        starNum = typedArray.getInteger(R.styleable.Star_starsNum, 5);
        starHeight = (int) typedArray.getDimension(R.styleable.Star_starHeight, 0);
        isStatic = typedArray.getBoolean(R.styleable.Star_isStatic, false);
        starWidth = (int) typedArray.getDimension(R.styleable.Star_starWidth, 0);
        starDistance = (int) typedArray.getDimension(R.styleable.Star_starDistance, 0);
        isClick = typedArray.getBoolean(R.styleable.Star_starClickable, true);
        starBackgroundBitmap = typedArray.getDrawable(R.styleable.Star_starBackground);
        starDrawDrawable = drawableToBitmap(typedArray.getDrawable(R.styleable.Star_starDrawBackground));
        typedArray.recycle();

        setClickable(isClick);
        //初始化画笔
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(starDrawDrawable, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starWidth * starNum + starDistance * (starNum - 1), starHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == starDrawDrawable || null == starBackgroundBitmap) {
            return;
        }
        for (int i = 0; i < starNum; i++) {
            starBackgroundBitmap.setBounds(starDistance * i + starWidth * i, 0, starWidth * (i + 1) + starDistance * i, starHeight);
            starBackgroundBitmap.draw(canvas);
        }
        if (starMark > 1) {
            canvas.drawRect(0, 0, starWidth, starHeight, mPaint);
            if (starMark - (int) (starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starWidth, 0);
                    canvas.drawRect(0, 0, starWidth, starHeight, mPaint);
                }
            } else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starWidth, 0);
                    canvas.drawRect(0, 0, starWidth, starHeight, mPaint);
                }
                canvas.translate(starDistance + starWidth, 0);
                canvas.drawRect(0, 0, starWidth * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starHeight, mPaint);
            }
        } else {
            canvas.drawRect(0, 0, starWidth * starMark, starHeight, mPaint);
        }
    }


    /**
     * //静止模式，即只显示
     */
    public void setStaticMode() {
        isStatic = true;
    }

    /**
     * 设置评分为整数
     */
    public void setIntMode() {
        isInt = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isStatic) {
            return false;
        }
        if (isInt) {
            int x = (int) event.getX();
            if (x < 0)
                x = 0;
            if (x > getMeasuredWidth())
                x = getMeasuredWidth();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setMark(x / (getMeasuredWidth() / starNum));
                    break;
                case MotionEvent.ACTION_MOVE:
                    setMark(x / (getMeasuredWidth() / starNum));
                    break;
                case MotionEvent.ACTION_UP:
                    setMark(x / (getMeasuredWidth() / starNum));
                    break;
            }
        } else {
            int x = (int) event.getX();
            if (x < 0)
                x = 0;
            if (x > getMeasuredWidth())
                x = getMeasuredWidth();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starNum));
                    break;
                case MotionEvent.ACTION_MOVE:
                    setMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starNum));
                    break;
                case MotionEvent.ACTION_UP:
                    setMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starNum));
                    break;
            }
        }
        return true;
    }

    /**
     * 设置分数
     */
    public void setMark(Float mark) {
        starMark = Math.round(mark * 10) * 1.0f / 10;
        if (null != changeListener) {
            changeListener.onStarChange(starMark);
        }
        invalidate();
    }

    /**
     * 设置分数
     */
    public void setMark(int mark) {
        starMark = Math.round(mark * 10) * 1.0f / 10;
        if (null != changeListener) {
            changeListener.onStarChange(starMark);
        }
        invalidate();
    }

    /**
     * 设置监听
     */
    public void setStarChangeLister(OnStarChangeListener starChangeLister) {
        changeListener = starChangeLister;
    }

    /**
     * 获取分数
     */
    public float getMark() {
        return starMark;
    }

    /**
     * 星星数量变化监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(Float mark);
    }

    /**
     * drawable转bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(starWidth, starHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, starWidth, starHeight);
        drawable.draw(canvas);
        return bitmap;
    }

}
