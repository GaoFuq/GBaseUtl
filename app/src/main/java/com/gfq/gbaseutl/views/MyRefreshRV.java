package com.gfq.gbaseutl.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by 高富强
 * on {2019/11/21} {14:47}
 * desctapion:
 */
public class MyRefreshRV extends RecyclerView {
    private boolean alreadyTop = false;//是否已经吸顶

    public MyRefreshRV(@NonNull Context context) {
        super(context);
    }

    public MyRefreshRV(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshRV(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isOpenNested) {
            if (alreadyTop) {
                return super.onTouchEvent(e);
            }
            return false;
        } else {
            return super.onTouchEvent(e);
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (isOpenNested) {
            if (t > oldt) {
                alreadyTop = true;
            }

            if (t < oldt && t == 0) {
                alreadyTop = false;
            }
        }
    }


    private boolean isOpenNested = false;

    public void setOpenNested(boolean openNested) {
        isOpenNested = openNested;
    }

}