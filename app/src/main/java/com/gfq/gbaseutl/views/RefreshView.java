package com.gfq.gbaseutl.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.databinding.RVBindingAdapter;
import com.gfq.gbaseutl.util.recycleview_util.GridSpacingItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * create by 高富强
 * on {2019/11/5} {10:30}
 * desctapion: 实现自动刷新加载数据
 *         int column = 2;
 *         RefreshView.NetDisconnectedView netDisconnectedView = binding.refreshView.getNetDisconnectedView();
 *         netDisconnectedView.setImage(R.mipmap.ic_launcher);
 *         netDisconnectedView.setRetryBackground(R.drawable.bg_retry);
 *         netDisconnectedView.setTipText("网络已断开，请检查网络连接");
 *         netDisconnectedView.setRetryText("立即重试");
 *
 *         binding.refreshView.setGridLayoutManager(column)
 *                 .setAdapter(new RVBindingAdapter<News.ResultBean>(this, BR.news) {
 *                     @Override
 *                     public void setPresentor(SuperBindingViewHolder holder, int position) {
 *
 *                     }
 *
 *                     @Override
 *                     public int getLayoutId() {
 *                         return R.layout.activity_item2;
 *                     }
 *                 })
 *                 .addItemDec(20,true)
 *                 .setRefreshViewListener(new RefreshView.RefreshViewListener<News.ResultBean>() {
 *             @Override
 *             public void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<News.ResultBean> adapter) {
 *                 APIService.call(APIService.api().getWangYiNews(currentPage + "", pageSize+""), data -> {
 *                     layout.finishLoadMore();
 *                     adapter.loadMore(data.getResult());
 *                 });
 *             }
 *
 *             @Override
 *             public void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<News.ResultBean> adapter) {
 *                 APIService.call(APIService.api().getWangYiNews(currentPage + "", pageSize+""), data -> {
 *                     layout.finishRefresh();
 *                     adapter.refresh(data.getResult());
 *                 });
 *             }
 *         });
 */
public class RefreshView extends FrameLayout {

    private Context context;

    private int currentPage = 1;//当前页
    private int pageSize = 10;//每页数据条数
    private int totalPage = 100;//总页数
    private int totalCount = 1000;//数据总量
    private MyRefreshRV recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private FrameLayout container;
    private RVBindingAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private NetDisconnectedView netDisconnectedView;
    private boolean isAutoRefreshOnStart = true;

    public NetDisconnectedView getNetDisconnectedView() {
        return netDisconnectedView;
    }

    public View setNetDisconnectedView(@LayoutRes int layoutResId) {
        this.netDisconnectedView.setContentView(layoutResId);
        return this.netDisconnectedView;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public RefreshView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initThis();
    }

    private void initThis() {
        View view = inflate(context, R.layout.refreshview, this);
        smartRefreshLayout = view.findViewById(R.id.smartrefresh);
        recyclerView = view.findViewById(R.id.recycleView);
        container = view.findViewById(R.id.container);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);//默认垂直LinearLayoutManager

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(context));

        netDisconnectedView = new NetDisconnectedView(context);

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!isNetworkConnected(context.getApplicationContext())) {
                    Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                    ViewGroup parent = (ViewGroup) netDisconnectedView.getParent();
                    if (parent == null) {
                        container.addView(netDisconnectedView);
                    } else {
                        Log.e("RefreshView", "netDisconnectedView parent != null");
                    }
                    netDisconnectedView.addOnRetryLoadMoreListener();
                    refreshLayout.finishLoadMore(false);
                    return;
                } else {
                    ViewGroup parent = (ViewGroup) netDisconnectedView.getParent();
                    if (parent != null) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                container.removeView(netDisconnectedView);
                            }
                        },500);
                    } else {
                        Log.e("RefreshView", "netDisconnectedView parent == null");
                    }
                }
                if (refreshViewListener != null) {
                    currentPage++;
                    if (currentPage > totalPage) {
                        currentPage = totalPage;
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        return;
                    }
                    if(adapter==null){
                        Log.e("RefreshView", "adapter == null");
                        return;
                    }
                    refreshViewListener.requestLoadMore(currentPage, pageSize, refreshLayout, adapter);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!isNetworkConnected(context.getApplicationContext())) {
                    Toast.makeText(context, "网络已断开", Toast.LENGTH_SHORT).show();
                    ViewGroup parent = (ViewGroup) netDisconnectedView.getParent();
                    if (parent == null) {
                        container.addView(netDisconnectedView);
                    } else {
                        Log.e("RefreshView", "netDisconnectedView parent != null");
                    }
                    netDisconnectedView.addOnRetryRefreshListener();
                    refreshLayout.finishRefresh(false);
                    return;
                } else {
                    ViewGroup parent = (ViewGroup) netDisconnectedView.getParent();
                    if (parent != null) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                container.removeView(netDisconnectedView);
                            }
                        },500);
                    } else {
                        Log.e("RefreshView", "netDisconnectedView parent == null");
                    }
                }
                if (refreshViewListener != null) {
                    currentPage++;
                    if (currentPage > totalPage) {
                        currentPage = totalPage;
                        refreshLayout.finishRefreshWithNoMoreData();
                        return;
                    }
                    if(adapter==null){
                        Log.e("RefreshView", "adapter == null");
                        return;
                    }
                    refreshViewListener.requestRefresh(currentPage, pageSize, refreshLayout, adapter);
                }
            }
        });

        if(isAutoRefreshOnStart) {
            smartRefreshLayout.autoRefresh();
        }
    }

    public void setAutoRefreshOnStart(boolean boo){
        isAutoRefreshOnStart = boo;
    }

    public void antuRefresh() {
        if (smartRefreshLayout != null)
            smartRefreshLayout.autoRefresh();
    }

    public void antuLoadMore() {
        if (smartRefreshLayout != null)
            smartRefreshLayout.autoLoadMore();
    }


    public RefreshView setV_LinearLayoutManager() {
        recyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    public RefreshView setH_LinearLayoutManager() {
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        return this;
    }

    public RefreshView setGridLayoutManager(int column) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, column));
        return this;
    }

    public RefreshView addItemDec(int spanCount, int spacing, boolean includeEdge) {
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        return this;
    }

    public RefreshView setGridLayoutManager(int column, int orientation) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, column, orientation, false));
        return this;
    }


    public RefreshView setAdapter(RVBindingAdapter adapter) {
        this.adapter=adapter;
        recyclerView.setAdapter(adapter);
        return this;
    }

    public RefreshView setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
        return this;
    }


    public interface RefreshViewListener<T> {
        void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter);

        void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter<T> adapter);

//        void onNetDisconnected();
    }

    private RefreshViewListener refreshViewListener;

    public void setRefreshViewListener(RefreshViewListener refreshViewListener) {
        this.refreshViewListener = refreshViewListener;
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = null;
            if (mConnectivityManager != null) {
                mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            }
            //获取连接对象
            if (mNetworkInfo != null) {
                //判断是TYPE_MOBILE网络
                if (ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()) {
//                    LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_MOBILE");
                    //判断移动网络连接状态
                    NetworkInfo.State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == NetworkInfo.State.CONNECTED) {
//                        LogManager.i("AppNetworkMgrd", "网络连接类型为：TYPE_MOBILE, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //判断是TYPE_WIFI网络
                if (ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()) {
//                    LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI");
                    //判断WIFI网络状态
                    NetworkInfo.State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == NetworkInfo.State.CONNECTED) {
//                        LogManager.i("AppNetworkMgr", "网络连接类型为：TYPE_WIFI, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }


    public class NetDisconnectedView extends FrameLayout {
        private final int REFRESH = 100;
        private final int LOADMORE = 200;
        private int type;
        private Context context;
        private TextView tvTip;
        private TextView tvRetry;
        private ImageView imageView;
        private View contentView;

        public NetDisconnectedView(@NonNull Context context) {
            this(context, null);
        }

        public NetDisconnectedView(@NonNull Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public NetDisconnectedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            this.context = context;
            initThis();
        }

        private void initThis() {
            contentView = inflate(context, R.layout.no_net, this);
            tvTip = contentView.findViewById(R.id.tv_tip);
            tvRetry = contentView.findViewById(R.id.tv_retry);
            imageView = contentView.findViewById(R.id.image);

        }

        private void addOnRetryLoadMoreListener() {
            type = LOADMORE;
            tvRetry.setOnClickListener(v -> smartRefreshLayout.autoLoadMore());
        }

        private void addOnRetryRefreshListener() {
            type = REFRESH;
            tvRetry.setOnClickListener(v -> smartRefreshLayout.autoRefresh());
        }

        public NetDisconnectedView setImage(@DrawableRes int resId) {
            imageView.setImageResource(resId);
            return this;
        }

        public NetDisconnectedView setTipText(String tipText) {
            tvTip.setText(tipText);
            return this;
        }

        public NetDisconnectedView setRetryText(String retryText) {
            tvRetry.setText(retryText);
            return this;
        }

        public NetDisconnectedView setRetryBackground(@DrawableRes int retryBg) {
            tvRetry.setBackgroundResource(retryBg);
            return this;
        }

        public NetDisconnectedView setTipTextColor(@ColorInt int color) {
            tvTip.setTextColor(color);
            return this;
        }

        public NetDisconnectedView setTipTextSize(float size) {
            tvTip.setTextSize(size);
            return this;
        }


        public TextView getTvRetry() {
            return tvRetry;
        }

        public View setContentView(@LayoutRes int layoutResid) {
            contentView = inflate(context, layoutResid, this);
            return contentView;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = tvRetry.getX();
            float y = tvRetry.getY();
            int w = tvRetry.getWidth();
            int h = tvRetry.getHeight();
            boolean touched = event.getX() < x + w + 100 && event.getX() > x - 100 && event.getY() < y + h + 100 && event.getY() > y - 100;
            if (type == REFRESH && touched) {
                smartRefreshLayout.autoRefresh();
            }
            if (type == LOADMORE && touched) {
                smartRefreshLayout.autoLoadMore();
            }
            return true;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }



    public void setOpenNested(boolean openNested) {
       recyclerView.setOpenNested(openNested);
    }

}
