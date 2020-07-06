package com.gfq.gbaseutl.net.download;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @created GaoFuq
 * @Date 2020/7/3 17:26
 * @Descaption
 */
public class ProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;

    public ProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(chain.request().url().url().toString(), originalResponse.body(), progressListener))
                .build();
    }
}