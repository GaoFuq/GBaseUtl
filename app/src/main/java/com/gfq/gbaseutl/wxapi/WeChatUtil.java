package com.gfq.gbaseutl.wxapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.gfq.gbaseutl.App;
import com.gfq.gbaseutl.R;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class WeChatUtil {

    private static IWXAPI api = WXAPIFactory.createWXAPI(App.appContext, "WX_APP_ID", true);

    /**
     * 创建分享的微信网页
     *
     * @param url
     * @return
     */
    public static SendMessageToWX.Req webObject(String url) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "装修威客APP下载";
        msg.description = "下载链接";
        Bitmap thumbBmp = BitmapFactory.decodeResource(App.appContext.getResources(), R.drawable.ic_launcher_background);
        Log.e("TAG", "webObject: " + thumbBmp);
        //将Bitmap转变为byte数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] images = baos.toByteArray();

        msg.thumbData = images;

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        //req.userOpenId = getOpenId();
        return req;
    }

    /**
     * 微信支付
     * @param wxBean
     */
    public static void WxPay(WxBean wxBean) {
        PayReq request = new PayReq();
        request.appId = wxBean.getAppid();
        request.partnerId = wxBean.getPartnerid();
        request.prepayId = wxBean.getPrepayid();
        request.packageValue = wxBean.getPackageX();
        request.nonceStr = wxBean.getNoncestr();
        request.timeStamp = wxBean.getTimestamp();
        request.sign = wxBean.getSign();
        api.sendReq(request);
    }
}
