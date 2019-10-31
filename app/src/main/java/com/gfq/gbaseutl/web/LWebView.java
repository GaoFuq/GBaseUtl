package com.gfq.gbaseutl.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 作者：高富强
 * 日期：2019/8/28 9:37
 * 描述：
 */
public class LWebView extends WebView {
  //  private Activity mActivity;
   // private MyProgressBar mProgressBar;
    private ProgressBar mProgressBar;
    private TextView mTextViewTitle;
    private View mBarLayout;
    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();
    private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();

    public LWebView(Context context) {
        super(context);
      //  mActivity = (Activity) context;
        init(context);
    }


    public LWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        //a WebSettings object that can be used to control this WebView's setting 该webview的设置
        WebSettings webSettings = this.getSettings();
        //true if the WebView should execute JavaScript  允许执行js
        webSettings.setJavaScriptEnabled(true);
        //whether the WebView should support zoom  开启缩放
        webSettings.setSupportZoom(true);
        //whether to enable support for the viewport meta tag  支持meta标记
        webSettings.setUseWideViewPort(true);
        //webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置可以访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        getSettings().setBlockNetworkImage(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

       // webSettings.setTextZoom(200);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //默认情况下，点击webview中的链接，会使用Android系统自带的浏览器打开这个链接。如果希望点击链接继续在我们自己的Browser中响应，必须覆盖 webview的WebViewClient对象
        this.setWebViewClient(mWebViewClientBase);

        this.setWebChromeClient(mWebChromeClientBase);
      //  this.onResume();
    }


    private class WebViewClientBase extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            log("默认情况下，点击webview中的链接，会使用Android系统自带的浏览器打开这个链接。如果希望点击链接继续在我们自己的Browser中响应，必须覆盖 webview的WebViewClient对象");
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //Notify the host application that a page has started loading  开始加载页面
            log("WebViewClient 开始加载页面 onPageStarted url="+url +" favicon="+favicon);
            if(null != mBarLayout){
                mBarLayout.setVisibility(View.VISIBLE);
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Notify the host application that a page has finished loading  页面加载完成
            if(null != mBarLayout){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBarLayout.setVisibility(View.GONE);
                    }
                }, 500);
            }
            log("WebViewClient 加载页面完成 onPageStarted url="+url +" favicon="+url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            //Report an error to the host application. These errors are unrecoverable  出错
            log("WebViewClient onReceivedError  errorCode="+errorCode +",description="+description+" failingUrl="+failingUrl);
            if(errorCode==-8){
                Toast.makeText(getContext(), "链接超时", Toast.LENGTH_SHORT).show();
            }else if(errorCode==404){
                view.loadUrl("file:///android_assets/test.html");
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            log("WebViewClient  onReceivedSslError  error="+error);
            super.onReceivedSslError(view, handler, error);
        }
        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            log("WebViewClient onReceivedSslError  ");
            super.onReceivedHttpError(view,request,errorResponse);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url,
                                           boolean isReload) {
            //Notify the host application to update its visited links database.   reload重新加载
            log("WebViewClient doUpdateVisitedHistory  isReload="+isReload+ "  url="+url);
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //Tell the host application the current progress of loading a page. 页面加载进度
            if(null != mProgressBar){
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //Notify the host application of a change in the document title  文档标题
            if(null != mTextViewTitle){
                mTextViewTitle.setText(title);
            }
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            log("WebChromeClient  onCreateWindow  isUserGesture=" + isUserGesture + "    isDialog="+isDialog);
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
    }


   /* public void setProgressBarView(MyProgressBar progressBar){
        this.mProgressBar = progressBar;
    }
*/
    public void setProgressBarView(ProgressBar progressBar){
        this.mProgressBar = progressBar;
    }

    public void setTitleTextView(TextView textView){
        this.mTextViewTitle = textView;
    }

    public void setBarLayout(View layout){
        this.mBarLayout = layout;
    }

    private void log(String log) {
        Log.e("LWebView", log);
    }

}
