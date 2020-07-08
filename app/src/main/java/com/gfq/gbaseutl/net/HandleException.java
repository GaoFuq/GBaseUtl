package com.gfq.gbaseutl.net;

import android.content.Intent;

import com.gfq.gbaseutl.App;
import com.gfq.gbaseutl.a_gfq_test.MainActivity;
import com.gfq.gbaseutl.util.ComUtil;


public class HandleException extends Exception {
    private int code;

    public HandleException(String message, int code) {
        super(message);
        this.code = code;

        if(code==401){//返回登录activity，重新登陆
//            ShareUtil.clear();
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setClass(App.appContext, MainActivity.class);
            App.appContext.startActivity(intent);
            ComUtil.toast("该账号已在其他设备登录！");
        }
    }


}
