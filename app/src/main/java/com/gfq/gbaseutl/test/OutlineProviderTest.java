package com.gfq.gbaseutl.test;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

/**
 * create by 高富强
 * on {2019/11/8} {17:48}
 * desctapion:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class OutlineProviderTest extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        //裁剪成一个圆形
        int left0 = (view.getWidth() - view.getHeight()) / 2;
        int top0 = 0;
        int right0 = left0 + view.getHeight() ;
        int bottom0 =  view.getHeight() ;
        outline.setOval(left0, top0, right0, bottom0);
    }

   /* private float mRadius;

    public OutlineProviderTest(float mRadius) {
        this.mRadius = mRadius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin,
                rect.bottom - rect.top - topMargin);
        outline.setRoundRect(selfRect, mRadius);
    }*/
}
