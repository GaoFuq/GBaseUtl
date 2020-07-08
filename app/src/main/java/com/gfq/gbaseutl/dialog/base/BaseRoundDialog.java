package com.gfq.gbaseutl.dialog.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.views.DensityUtil;


/**
 * create by 高富强
 * on {2019/10/15} {17:22}
 * desctapion:
 */
public class BaseRoundDialog<T extends ViewDataBinding> {
    private T dialogBinding;
    private Context context;
    private View view;
    private AlertDialog roundDialog;
    private CardView cardView;
    private FrameLayout container;
    private View childView;
    private LayoutInflater layoutInflater;
    public BaseRoundDialog(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        findViews();
        init();
    }

    private void findViews() {
        view = LayoutInflater.from(context).inflate(R.layout.base_dialog_round, null);
        cardView = view.findViewById(R.id.cardView);
        container = view.findViewById(R.id.container);
    }


    private void init() {
        roundDialog = new AlertDialog.Builder(context).create();
        roundDialog.setView(view);
        Window window = roundDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void setCornerRadius(float radius) {
        cardView.setRadius(DensityUtil.dp2px(radius));
    }


    public View setContentView(@LayoutRes int layout) {
        childView = layoutInflater.inflate(layout, null, false);
        container.addView(childView);
        return childView;
    }

    public T bindView(@LayoutRes int layout) {
        dialogBinding = DataBindingUtil.inflate(layoutInflater, layout, null, false);
        container.addView(dialogBinding.getRoot());
        return dialogBinding;
    }


    public void show() {
        roundDialog.show();
    }

    public void dismiss() {
        roundDialog.dismiss();
    }


    public boolean isShowing() {
        return roundDialog.isShowing();
    }

    public AlertDialog getAlert() {
        return roundDialog;
    }

    public void setCanceledOnTouchOutside(boolean boo) {
        roundDialog.setCanceledOnTouchOutside(boo);
    }

}


