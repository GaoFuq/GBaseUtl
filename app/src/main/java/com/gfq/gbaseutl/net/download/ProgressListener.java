package com.gfq.gbaseutl.net.download;

/**
 * @created GaoFuq
 * @Date 2020/7/3 17:26
 * @Descaption
 */
public interface ProgressListener {
    void update(String url, long bytesRead, long contentLength, boolean done);
}
