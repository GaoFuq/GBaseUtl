package com.gfq.gbaseutl.ad;

public interface ADOnCallBack<T> {
        void onSuccess(T t);

        void onError(String e);


}
