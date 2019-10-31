package com.gfq.gbaseutl.web;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;


/**
 * 作者：高富强
 * 日期：2019/8/28 11:11
 * 描述：
 */
public class JsCallAndroid {

    private LWebView webView;

    public JsCallAndroid(LWebView c) {
        webView = c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void goBackBtn() {
        ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView.canGoBack()) {
                    webView.goBack();//返回上一页面
                } else {
                    ((Activity) webView.getContext()).finish();
                }
            }
        });
    }

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        webView.getContext().startActivity(intent);

    }


}
