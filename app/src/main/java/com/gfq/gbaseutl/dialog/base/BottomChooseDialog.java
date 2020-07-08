package com.gfq.gbaseutl.dialog.base;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.BottomChooseDialogBinding;
import com.gfq.gbaseutl.views.wheelview.WheelAdapter;
import com.gfq.gbaseutl.views.wheelview.WheelView;

import java.util.List;

/**
 * create by 高富强
 * on {2019/10/17} {15:26}
 * desctapion:
 */
public abstract class BottomChooseDialog<T> extends BaseBottomRoundDialog {
    private BottomChooseDialogBinding binding;
    private List<T> dataList;
    private T content;

    public BottomChooseDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_choose_dialog, null);
        binding = DataBindingUtil.bind(view);
        setContentView(view);

        binding.tvTitle.setText(getTitle());
        binding.tvCancel.setOnClickListener(v->dismiss());
        binding.tvConfirm.setOnClickListener(v->{
            dismiss();
            onConfirmClicked(content);
        });

        dataList = getDataList();
        content = dataList.get(1);
        WheelAdapter<T> adapter = new WheelAdapter<T>() {
            @Override
            public int getItemsCount() {
                return dataList == null ? 0 : dataList.size();
            }

            @Override
            public T getItem(int index) {
                return dataList.get(index);
            }

            @Override
            public int indexOf(T o) {
                return dataList.indexOf(o);
            }
        };
        binding.wheelView.setAdapter(adapter);
        binding.wheelView.setCurrentItem(0);
        binding.wheelView.setCyclic(false);
        binding.wheelView.setOnItemSelectedListener(index -> content = dataList.get(index));
        binding.wheelView.setTextColorCenter(Color.parseColor("#333333"));
        binding.wheelView.setTextColorOut(Color.parseColor("#666666"));
        binding.wheelView.setTextSize(14);
        binding.wheelView.setDividerColor(Color.parseColor("#cccccc"));
        binding.wheelView.setLineSpacingMultiplier(3);
        binding.wheelView.setDividerType(WheelView.DividerType.WRAP);


    }

    protected abstract String getTitle();
    public abstract List<T> getDataList() ;

    protected abstract void onConfirmClicked(T content);








}
