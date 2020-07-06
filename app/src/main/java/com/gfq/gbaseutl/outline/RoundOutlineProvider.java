package com.gfq.gbaseutl.outline;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

import com.gfq.gbaseutl.views.DensityUtil;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RoundOutlineProvider extends ViewOutlineProvider {

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        outline.setRoundRect(rect, DensityUtil.dp2px(6));
    }
}
