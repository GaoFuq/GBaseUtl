package com.gfq.gbaseutl.views.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.RVBindingAdapter;
import com.gfq.gbaseutl.databinding.SuperBindingViewHolder;
import com.gfq.gbaseutl.views.DensityUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * @created GaoFuq
 * @Date 2020/6/16 12:54
 * @Descaption
 */
public class RefreshViewUtil<T> {
    private final Context context;
    private  AllCallBack<T> callBack;
    private RefreshView<T> refreshView;
    private RVBindingAdapter<T> adapter;

    public RefreshView<T> getRefreshView() {
        return refreshView;
    }

    public interface AllCallBack<T>{
        void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter);
        void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter);
        void setPresentor(SuperBindingViewHolder holder, T data, int position);
    }

    public void setCallBack(AllCallBack<T> callBack) {
        this.callBack = callBack;
    }

    public RefreshViewUtil(Context context, ViewGroup parent) {
        this.context = context;
        refreshView = new RefreshView<>(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = DensityUtil.dp2px(15);
        params.rightMargin = DensityUtil.dp2px(15);
        parent.addView(refreshView,params);
        View view = LayoutInflater.from(context).inflate(R.layout.refresh_nodata, null, false);
        refreshView.setNoDataView(view);
        handleNet();
    }

    public RefreshViewUtil<T> setMargin(int dpLeft,int dpTop,int dpRight,int dpBottom){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = DensityUtil.dp2px(dpLeft);
        params.rightMargin = DensityUtil.dp2px(dpRight);
        params.topMargin = DensityUtil.dp2px(dpTop);
        params.bottomMargin = DensityUtil.dp2px(dpBottom);
        refreshView.setLayoutParams(params);
        return this;
    }

    public RefreshViewUtil<T> handleRefresh() {
        refreshView.setAdapter(adapter)
                .setRefreshViewListener(new RefreshView.RefreshViewListener<T>() {
                    @Override
                    public void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter) {
                        callBack.requestLoadMore(currentPage,pageSize,layout,adapter);
                    }

                    @Override
                    public void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter) {
                        callBack.requestRefresh(currentPage,pageSize,layout,adapter);
                    }
                });

        return  this;
    }



    public RefreshViewUtil<T> createAdapter(int itemLayout,int var) {
        adapter = new RVBindingAdapter<T>(context,var) {

            @Override
            public void setPresentor(SuperBindingViewHolder holder, int position) {
                callBack.setPresentor(holder,getDataList().get(position),position);
            }

            @Override
            public int getLayoutId() {
                return itemLayout;
            }
        };
        return this;
    }




    public RefreshViewUtil<T> handleNet() {
        //处理网络断开
        refreshView.setNetDisconnectedView(new NetDisconnectedView(context) {
            @Override
            protected View setRetryView() {
                View contentView = getContentView();
                return contentView.findViewById(R.id.tv_retry);
            }

            @Override
            protected SmartRefreshLayout setSmartRefreshLayout() {
                return refreshView.getSmartRefreshLayout();
            }

            @Override
            protected int setContentView() {
                return R.layout.refresh_no_net;
            }
        });
        return this;
    }


}
