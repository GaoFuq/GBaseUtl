package com.gfq.gbaseutl.views.bottom_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.gfq.gbaseutl.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * create by 高富强
 * on {2019/10/17} {10:05}
 * desctapion:
 */
public class BaseBottomRoundDialog extends FrameLayout {
    private BottomSheetDialog dialog;
    protected Context context;
    private FrameLayout container;

    public BaseBottomRoundDialog(Context context) {
        super(context);
        this.context = context;
        initBase();

    }


    private void initBase() {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_round_corner_view, this, false);
        container = view.findViewById(R.id.container);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
    }


    public BaseBottomRoundDialog setContentView(View view) {
        container.addView(view);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setCanceledOnTouchOutside(boolean boo){
        dialog.setCanceledOnTouchOutside(boo);
    }

}
