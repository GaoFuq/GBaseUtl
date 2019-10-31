package com.gfq.gbaseutl.web;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.statusbar.StatusBarUtils;
import com.gfq.gbaseutl.util.ActivityManager;


/**
 * 作者：高富强
 * 日期：2019/8/27 10:33
 * 描述：
 */
public class WebActivity2 extends AppCompatActivity {
    private String webUrl;

    private LWebView mWebView;
    private ProgressBar mProgressBar;
    private LinearLayout mBar_layout;
    private TextView tvInvestCoop;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView iv_share;
    private boolean isInvestCoop;
    private String title;
    private String inviteUrl;
    private int random2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarDarkTheme(this, true);
        StatusBarUtils.setStatusBarColor(this, Color.WHITE);
//        setContentView(R.layout.activity_web);
        ActivityManager.getScreenManager().addActivity(this);
        webUrl = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("title");
        isInvestCoop = getIntent().getBooleanExtra("isInvestCoop", false);
        boolean needShare = getIntent().getBooleanExtra("needShare", false);
        //  boolean needShareTransportTitle = getIntent().getBooleanExtra("isTransportTitle",false);


        initView();

        if (needShare) {
            iv_share.setVisibility(View.VISIBLE);
        }

      /*  if(needShareTransportTitle){
            title_bar.setVisibility(View.GONE);
            title_bar_transport.setVisibility(View.VISIBLE);
        }else {}*/


        initData();
        initEvent();
    }


    private void initView() {
//        mWebView = findViewById(R.id.webView);
        //mWebView.clearCache(true);
        // mWebView.setBarLayout(mBar_layout);
        //mWebView.setProgressBarView(mProgressBar);


    }
    private void initData() {

            //  mWebView.loadUrl("http://192.168.10.113:38471/#/information_disclosure?publicProjectId=03b374ce8490430c833c05a510f7987e");
            //本地测试的网页
            //mWebView.loadUrl("file:///android_asset/test.html");

    }

    private void initEvent() {
        mWebView.addJavascriptInterface(new JsCallAndroid(mWebView), "jsCallAndroid");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //canGoBack()  有历史项目 为true  回退
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //激活webView的状态，能正常加载网页
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            //mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }


}
