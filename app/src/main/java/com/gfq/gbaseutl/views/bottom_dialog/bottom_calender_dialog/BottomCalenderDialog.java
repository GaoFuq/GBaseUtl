package com.gfq.gbaseutl.views.bottom_dialog.bottom_calender_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.BottomCalenderBinding;
import com.gfq.gbaseutl.views.bottom_dialog.BaseBottomRoundDialog;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.Map;

/**
 * create by 高富强
 * on {2019/10/17} {11:21}
 * desctapion:
 */
public class BottomCalenderDialog extends BaseBottomRoundDialog {
    private BottomCalenderBinding binding;
    private Map<String, Calendar> map;
    private int year;
    private int month;
    private int day;


    public BottomCalenderDialog(Context context) {
        super(context);
        init();
    }



    private void init() {
        map = new HashMap<>();
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_calender, null);
        binding = DataBindingUtil.bind(view);
        year = binding.calendarView.getCurYear();
        month = binding.calendarView.getCurMonth();
        binding.tvYear.setText(year + "");
        binding.tvMonth.setText(month + "");
        day = binding.calendarView.getCurDay();
        setContentView(view);
        map.put(getSchemeCalendar(year, month, day, 0xFF3461E8, "今").toString(), getSchemeCalendar(year, month, day, 0xFF3461E8, "今"));
        binding.calendarView.setSchemeDate(map);
        binding.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                 year = calendar.getYear();
                 month = calendar.getMonth();
                binding.tvYear.setText(year + "");
                binding.tvMonth.setText(month + "");
//                Toast.makeText(context, calendar.getDay()+"", Toast.LENGTH_SHORT).show();
                if(onCalenderSelectListener!=null){
                    onCalenderSelectListener.onCalenderSelected(year+"",month+"",calendar.getDay()+"");
                }
            }
        });

        onclick();
    }

    private void onclick() {
        binding.ivLeftBackOne.setOnClickListener(v -> binding.calendarView.scrollToPre(true));
        binding.ivRightBackOne.setOnClickListener(v -> binding.calendarView.scrollToNext(true));

        binding.ivLeftBackDouble.setOnClickListener(v -> {
            String tempYear = binding.tvYear.getText().toString();
            String tempMonth = binding.tvMonth.getText().toString();
            int year = Integer.valueOf(tempYear)-1;
            int month = Integer.valueOf(tempMonth);
            binding.calendarView.scrollToCalendar(year, month,1,true);
        });


        binding.ivRightBackDouble.setOnClickListener(v -> {
            String tempYear = binding.tvYear.getText().toString();
            String tempMonth = binding.tvMonth.getText().toString();
            int year = Integer.valueOf(tempYear)+1;
            int month = Integer.valueOf(tempMonth);
            binding.calendarView.scrollToCalendar(year, month,1,true);
        });
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }


    public interface OnCalenderSelectListener{
        void onCalenderSelected(String year, String month, String day);
    }
    private OnCalenderSelectListener onCalenderSelectListener;

    public void setOnCalenderSelectListener(OnCalenderSelectListener onCalenderSelectListener) {
        this.onCalenderSelectListener = onCalenderSelectListener;
    }
}
