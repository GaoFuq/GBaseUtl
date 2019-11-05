package com.gfq.gbaseutl.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

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
 *    int column = 2;
 *   binding.refreshView.setGridLayoutManager(column)
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
 *                 .setRefreshViewListener(new RefreshView.RefreshViewListener() {
 *             @Override
 *             public void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter adapter) {
 *                 APIService.call(APIService.api().getWangYiNews(currentPage + "", pageSize+""), data -> {
 *                     layout.finishLoadMore();
 *                     adapter.loadMore(data.getResult());
 *                 });
 *             }
 *
 *             @Override
 *             public void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter adapter) {
 *                 APIService.call(APIService.api().getWangYiNews(currentPage + "", pageSize+""), data -> {
 *                     layout.finishRefresh();
 *                     adapter.refresh(data.getResult());
 *                 });
 *             }
 *         });
 */
public class RefreshView extends FrameLayout {

    private Context context;

    private int currentPage = 0;//当前页
    private int pageSize = 10;//每页数据条数
    private int totalPage = 100;//总页数
    private int totalCount = 1000;//数据总量
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private RVBindingAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int colum=2;


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
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);//默认垂直LinearLayoutManager

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(context));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(context));

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (refreshViewListener != null) {
                    currentPage++;
                    if (currentPage > totalPage) {
                        currentPage = totalPage;
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        return;
                    }
                    refreshViewListener.requestLoadMore(currentPage, pageSize, refreshLayout, adapter);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (refreshViewListener != null) {
                    currentPage++;
                    if (currentPage > totalPage) {
                        currentPage = totalPage;
                        refreshLayout.finishRefreshWithNoMoreData();
                        return;
                    }
                    refreshViewListener.requestRefresh(currentPage, pageSize, refreshLayout, adapter);
//                   adapter.clear();
//                   adapter.setDataList(list);
                }
            }
        });
        smartRefreshLayout.autoRefresh();
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
        this.colum=column;
        recyclerView.setLayoutManager(new GridLayoutManager(context,column));
        return this;
    }

    //必须要GridLayoutManager
    public RefreshView addItemDec(int spacing, boolean includeEdge){
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(colum,spacing,includeEdge));
        return this;
    }
    public RefreshView setGridLayoutManager(int column,int orientation) {
        this.colum=column;
        recyclerView.setLayoutManager(new GridLayoutManager(context,column,orientation,false));
        return this;
    }


    public RefreshView setAdapter(RecyclerView.Adapter adapter) {
        if (adapter instanceof RVBindingAdapter) {
            this.adapter = (RVBindingAdapter) adapter;
        }
        recyclerView.setAdapter(adapter);
        return this;
    }

    public RefreshView setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
        return this;
    }


    public interface RefreshViewListener<T> {
        void requestLoadMore(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter adapter);

        void requestRefresh(int currentPage, int pageSize, RefreshLayout layout, RVBindingAdapter adapter);
    }

    private RefreshViewListener refreshViewListener;

    public void setRefreshViewListener(RefreshViewListener refreshViewListener) {
        this.refreshViewListener = refreshViewListener;
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

}
