package com.gfq.gbaseutl.wxapi;

import android.app.Activity;

import java.util.Map;

/**
 * @created GaoFuq
 * @Date 2020/7/8 9:54
 * @Descaption
 */
public class Simple extends Activity {
    //支付宝支付
    private void payByZfb() {
        ZfbUtil.zfbPay(this, "orderInfo");
    }

    //微信支付
    private void payByWx() {
        WeChatUtil.WxPay(new WxBean());
    }
}
