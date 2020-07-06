package com.gfq.gbaseutl.views.refresh;

import androidx.lifecycle.ViewModel;


import java.util.ArrayList;
import java.util.List;

/**
 * @created GaoFuq
 * @Date 2020/7/2 11:52
 * @Descaption 对于有分页数据的 viewModel
 */
public class RefreshVM extends ViewModel {

    private List<Object> data;//数据

    public void refreshData(List<Object> list) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.clear();
        if (list != null) {
            data.addAll(list);
        }
    }

    public void loadMoreData(List<Object> list) {
        data.addAll(list);
    }

    public List<Object> getData() {
        return data;
    }



}
