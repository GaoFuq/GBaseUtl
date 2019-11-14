package com.gfq.gbaseutl.examples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.BindingCustomAttribute;
import com.gfq.gbaseutl.views.drop_select_view.SearchBean;
import com.gfq.gbaseutl.web.WebActivity2;
import com.ms.banner.holder.BannerViewHolder;

/**
 * create by 高富强
 * on {2019/11/8} {16:38}
 * desctapion:
 */
public class BannerExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


      /*  APIService.call(APIService.api().getWebPageInfo(Constant.WEB_POPULAR_EVENTS), this, webPageList ->{
            binding.banner.setPages(webPageList.getDataList(), new BannerDiscoverViewHolder()).start())
        });*/

    }


    private class BannerDiscoverViewHolder implements BannerViewHolder<SearchBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
//            View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_item, null);
//            imageView = view.findViewById(R.id.imageView);
//            return view;
            return null;
        }

        @Override
        public void onBind(Context context, int position, SearchBean data) {
            imageView.setOnClickListener(v -> startActivity(new Intent(BannerExampleActivity.this, WebActivity2.class)));
        }
    }
}
