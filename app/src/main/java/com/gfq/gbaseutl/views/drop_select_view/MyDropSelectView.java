package com.gfq.gbaseutl.views.drop_select_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.gfq.gbaseutl.R;

import java.util.List;

/**
 * 作者：高富强
 * 日期：2019/8/16 14:03
 * 描述：
 */
public class MyDropSelectView extends LinearLayout {
    private TextView tvName;
    private ImageView ivArrow;
    private LinearLayout llContainer;
    private Context context;
    private PopupWindow popMenu;
    private String title;
    private int dropIcon;
    private int drop_title_changed_color;
    private Animation animation;
    private Animation animation2;
    private int drop_title_origin_color;
    private ListView popListView;
    private MyListAdapter adapter;

    public List<SearchBean> getMenuData() {
        return menuData;
    }

    public void setMenuData(List<SearchBean> menuData) {
        this.menuData = menuData;
    }

    private List<SearchBean> menuData;

    public MyDropSelectView(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    public MyDropSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();

    }


    private void initView() {
        //通过loadAnimation从XML文件中获取动画   利用startAnimation将动画传递给指定控件
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        animation.setFillAfter(true);
        animation2 = AnimationUtils.loadAnimation(context, R.anim.rotate2);
        animation2.setFillAfter(true);

        LayoutInflater.from(context).inflate(R.layout.my_drop_select_view, this);

        tvName = findViewById(R.id.tv_name);
        ivArrow = findViewById(R.id.iv_arrow);
        llContainer = findViewById(R.id.ll_container);

        if (TextUtils.isEmpty(title)) {
            tvName.setText("无标题");
        } else {
            tvName.setText(title);
        }

        ivArrow.setImageResource(dropIcon);

        llContainer.setOnClickListener(v -> {
            tvName.setTextColor(drop_title_changed_color);
            popMenu.showAsDropDown(llContainer);
            ivArrow.startAnimation(animation);
        });


        AutoTransition transition = new AutoTransition();
        transition.setDuration(500);
        View contentView = View.inflate(context, R.layout.drop_select_popwin_list, null);
        popMenu = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        popMenu.setOutsideTouchable(true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.setFocusable(true);
        //popMenu.setAnimationStyle(R.style.popwin_anim_style);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popMenu.setEnterTransition(transition);
            popMenu.setExitTransition(transition);
        }
        popMenu.setOnDismissListener(() -> {
            tvName.setTextColor(drop_title_origin_color);
            ivArrow.startAnimation(animation2);
        });

        //popWin的内容列表
        popListView = contentView.findViewById(R.id.popwin_supplier_list_lv);
        //底部点击关闭popWin
        contentView.findViewById(R.id.popwin_supplier_list_bottom).setOnClickListener(view -> popMenu.dismiss());

        //adapter = new MyListAdapter(context, getMenuData());
        // popListView.setAdapter(adapter);

        popListView.setOnItemClickListener((adapterView, view, pos, arg) -> {
            popMenu.dismiss();
            SearchBean bean = (SearchBean) adapter.getItem(pos);
            tvName.setText(bean.getName());
            view.findViewById(R.id.iv_check).setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.parseColor("#cdcdcd"));
            for (int i = 0; i < adapter.getCount(); i++) {
                if (i != pos) {
                    popListView.getChildAt(i).findViewById(R.id.iv_check).setVisibility(View.INVISIBLE);
                    popListView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
            if(onItemClick!=null) {
                onItemClick.onItemClick(bean.getName(), pos);
            }
        });


    }

    public void setAdapter(MyListAdapter adapter) {
        this.adapter = adapter;
        popListView.setAdapter(adapter);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyDropSelectView);


        title = typedArray.getString(R.styleable.MyDropSelectView_drop_title);
        dropIcon = typedArray.getResourceId(R.styleable.MyDropSelectView_drop_icon, R.mipmap.ic_drop_view_down);
        drop_title_changed_color = typedArray.getColor(R.styleable.MyDropSelectView_drop_title_changed_color, Color.parseColor("#39ac69"));
        drop_title_origin_color = typedArray.getColor(R.styleable.MyDropSelectView_drop_title_origin_color, Color.parseColor("#333333"));

        // 释放资源
        typedArray.recycle();
    }


    public interface OnItemClick{
        void onItemClick(String content, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private OnItemClick onItemClick;
}
