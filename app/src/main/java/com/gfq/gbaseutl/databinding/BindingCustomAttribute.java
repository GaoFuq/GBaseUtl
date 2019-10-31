package com.gfq.gbaseutl.databinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * 作者 : 高富强
 * 2019/8/13  17:02
 * 作用 : databing 自定义属性
 */
public class BindingCustomAttribute {


    @BindingAdapter({"url"})
    public static void setImgUrl(ImageView view, String url) {
//        Glide.with(view.getContext()).load(url).apply(getGlideOptions()).into(view);
    }

}
