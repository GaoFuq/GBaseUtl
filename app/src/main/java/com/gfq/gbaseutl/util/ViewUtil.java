package com.gfq.gbaseutl.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gfq.gbaseutl.App;

import java.util.List;

/**
 * create by 高富强
 * on {2020/3/19} {10:50}
 * desctapion:
 */
public class ViewUtil {
    /**
     * 隐藏软键盘
     * @param viewList 放的是当前界面所有触发软键盘弹出的控件
     */
    public static void hideSoftKeyboard(List<View> viewList) {
        if (viewList == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) App.appContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static int dip2px(float dpValue) {
        final float scale = App.appContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenW() {
        return App.appContext.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenH() {
        return App.appContext.getResources().getDisplayMetrics().heightPixels;
    }

}
