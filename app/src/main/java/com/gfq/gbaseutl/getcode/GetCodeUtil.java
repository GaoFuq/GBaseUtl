package com.gfq.gbaseutl.getcode;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;


import com.gfq.gbaseutl.util.ComUtil;

import java.util.HashMap;


/**
 * @created GaoFuq
 * @Date 2020/6/22 10:39
 * @Descaption 获取验证码
 */
public class GetCodeUtil {


    private MyTimer timer;

    public void getCode(String phone, String scene,TextView textView, Lifecycle lifecycle) {
        if (TextUtils.isEmpty(phone)) {
            ComUtil.toast("请输入手机号码");
            return;
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("scene", scene);
//        APIService.call(APIService.api().sendCode(APIUtil.requestMap(map)), new OnCallBack<Object>() {
//            @Override
//            public void onSuccess(Object o) {
//                ComUtil.toast(msg);
//                startTimer(textView,lifecycle);
//            }
//
//            @Override
//            public void onError(String e) {
//
//            }
//        });

    }

    private void startTimer(TextView textView, Lifecycle lifecycle) {
        if (timer == null) {
            timer = new MyTimer(60 * 1000, 1000, new MyTimer.CallBack() {
                @Override
                public void onTick(long second) {
                    String s = second + "s";
                    textView.setText(s);
                }

                @Override
                public void onFinish() {
                    textView.setText("获取验证码");
                    textView.setEnabled(true);
                }
            });
            textView.setEnabled(false);
            lifecycle.addObserver(timer);
        }
        timer.start();
    }
}



