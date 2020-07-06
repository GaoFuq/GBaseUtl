package com.gfq.gbaseutl.views.refresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gfq.gbaseutl.BR;
import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.a_gfq_test.Ad;
import com.gfq.gbaseutl.databinding.RVBindingAdapter;
import com.gfq.gbaseutl.databinding.SuperBindingViewHolder;
import com.gfq.gbaseutl.net.APIService;
import com.gfq.gbaseutl.net.OnCallBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simple extends AppCompatActivity /*or Fragment*/ {
    FrameLayout container;
    RefreshView<Ad> refreshView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
        container = new FrameLayout(this);
        setContentView(container);
    }

    public  void main() {
        RefreshViewUtil<Ad> util = new RefreshViewUtil<>(this, container);
        refreshView = util.getRefreshView();
        util.createAdapter(R.layout.item_rv, BR._all).handleRefresh().setCallBack(new RefreshViewUtil.AllCallBack<Ad>() {
            @Override
            public void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<Ad> adapter) {
                doLoadMore(currentPage,pageSize,layout,adapter);
            }

            @Override
            public void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<Ad> adapter) {
               doRefresh(currentPage,pageSize,layout,adapter);
            }

            @Override
            public void setPresentor(SuperBindingViewHolder holder, Ad homeInfo, int position) {

            }
        });

        refreshView.autoRefresh();

    }

    private void doRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<Ad> adapter) {
        APIService.call(APIService.api().getAdUrl(""), new OnCallBack<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> ads) {
                if (ads != null && ads.size() > 0) {
                    refreshView.removeNoDataView();
                    adapter.refresh(ads);
                } else {
                    refreshView.showNoDataView();
                }
                layout.finishRefresh(true);
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void doLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<Ad> adapter) {
        APIService.call(APIService.api().getAdUrl(""), new OnCallBack<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> ads) {
                if (ads != null && ads.size() > 0) {
                    adapter.loadMore(ads);
                    layout.finishLoadMore(true);
                } else {
                    layout.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onError(String e) {
                layout.finishLoadMore(false);
            }
        });
    }



}
