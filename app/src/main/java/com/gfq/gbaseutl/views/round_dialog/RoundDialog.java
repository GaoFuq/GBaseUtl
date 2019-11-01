package com.gfq.gbaseutl.views.round_dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.cardview.widget.CardView;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.views.DensityUtil;


/**
 * create by 高富强
 * on {2019/10/15} {17:22}
 * desctapion:
 */
public  class RoundDialog {
    private Context context;
    private View view;
    private AlertDialog roundDialog;
    private View dvd1;
    private CardView cardView;
    private TextView tvTitle;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvContent;
    private FrameLayout container;
    private View childView;

    public RoundDialog(Context context) {
        this.context = context;
        findViews();
        init();
    }

    private void findViews() {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_round, null);
        cardView = view.findViewById(R.id.cardView);
        container = view.findViewById(R.id.container);
        tvTitle = view.findViewById(R.id.tv_title);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvConfirm = view.findViewById(R.id.tv_confirm);
        tvContent = view.findViewById(R.id.tv_content);
        dvd1 = view.findViewById(R.id.dvd1);

        tvCancel.setOnClickListener(v -> roundDialog.dismiss());
        tvConfirm.setOnClickListener(v -> {
            if(listner!=null){
                listner.onConfirmClicked();
            }
            roundDialog.dismiss();
        });
    }

    public TextView getConfirmBtn(){
        return tvConfirm;
    }


    private void init() {
        roundDialog = new AlertDialog.Builder(context).create();
        roundDialog.setView(view,100,0,100,100);
        Window window = roundDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public RoundDialog setCornerRadius(float radius) {
        cardView.setRadius(DensityUtil.dp2px(radius));
        return this;
    }

    public RoundDialog setTitle(String title) {
        tvTitle.setText(title);
        tvTitle.setVisibility(View.VISIBLE);
        dvd1.setVisibility(View.VISIBLE);
        return this;
    }

    public RoundDialog setContent(String content) {
        tvContent.setText(content);
        return this;
    }

    public RoundDialog setContentTextColor(@ColorInt int contentTextColor) {
        tvContent.setTextColor(contentTextColor);
        return this;
    }

    public RoundDialog setContentTextSize(float size) {
        tvContent.setTextSize(size);
        return this;
    }


    public RoundDialog setCancelText(String cancelText) {
        tvCancel.setText(cancelText);
        return this;
    }

    public RoundDialog setCancelTextColor(@ColorInt int cancelTextColor) {
        tvCancel.setTextColor(cancelTextColor);
        return this;
    }

    public RoundDialog setCancelTextSize(float cancelTextSize) {
        tvCancel.setTextSize(cancelTextSize);
        return this;
    }


    public RoundDialog setConfirmText(String confirmText) {
        tvConfirm.setText(confirmText);
        return this;
    }

    public RoundDialog setConfirmTextColor(@ColorInt int confirmTextColor) {
        tvConfirm.setTextColor(confirmTextColor);
        return this;
    }

    public RoundDialog setConfirmTextSize(float confirmTextSize) {
        tvConfirm.setTextSize(confirmTextSize);
        return this;
    }


    public RoundDialog setMyContentView(@LayoutRes int layout) {
        childView = LayoutInflater.from(context).inflate(layout, container);
        container.addView(childView);
        return this;
    }

    public View getMyContentView() {
        return childView;
    }

    public void show() {
        roundDialog.show();
    }

    public void dismiss() {
        roundDialog.dismiss();
    }

    public void setMyView(@LayoutRes int layout){
        View view = LayoutInflater.from(context).inflate(layout,null);
        roundDialog.setView(view,100,0,100,0);
    }

    public boolean isShowing(){
        return roundDialog.isShowing();
    }

    public AlertDialog getAlert(){
        return roundDialog;
    }

    public interface OnRoundDialogConfirmClickListner{
        void onConfirmClicked();
    }
    private OnRoundDialogConfirmClickListner listner;

    public RoundDialog setListner(OnRoundDialogConfirmClickListner listner) {
        this.listner = listner;
        return this;
    }
}


