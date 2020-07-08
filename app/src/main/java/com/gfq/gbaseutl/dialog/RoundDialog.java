package com.gfq.gbaseutl.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.cardview.widget.CardView;
import androidx.databinding.ViewDataBinding;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.DialogRoundBinding;
import com.gfq.gbaseutl.dialog.base.BaseRoundDialog;
import com.gfq.gbaseutl.views.DensityUtil;


/**
 * create by 高富强
 * on {2019/10/15} {17:22}
 * desctapion:
 */
public  class RoundDialog {
   private Context context;

    public RoundDialog(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        BaseRoundDialog<DialogRoundBinding> dialog = new BaseRoundDialog<>(context);
        DialogRoundBinding binding = dialog.bindView(R.layout.dialog_round);
        binding.tvConfirm.setOnClickListener(v -> confirm());
        binding.tvCancel.setOnClickListener(v -> cancel());
    }

    private void cancel() {

    }

    private void confirm() {

    }
}


