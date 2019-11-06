package com.gfq.gbaseutl.util;

import androidx.annotation.Nullable;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * create by 高富强
 * on {2019/11/1} {10:19}
 * desctapion:
 * 调用示例：
 *         UpLoadFileUtil.getInstance()
 *                 .addFormDataPart("name","value")
 *                 .header("token","tokenV")
 *                 .url("url")
 *                 .postUpLoad();
 *         UpLoadFileUtil.getInstance().setCallBack(new OnPostUpLoadCallBack() {
 *             @Override
 *             public void onFailure(Call call, IOException e) {
 *
 *             }
 *
 *             @Override
 *             public void onResponse(Call call, Response response) {
 *
 *             }
 *         });
 *
 *         =========================================================================================
 *       private void uploadImage(String step,String filePath) {
 *          File file = new File(filePath);
 *          if(!file.exist()){
 *              Toast("文件不存在");
 *              return;
 *          }
 *         MultipartBody.Builder builder = new MultipartBody.Builder();
 *         builder.addFormDataPart("image", UUID.randomUUID().toString().replaceAll("-","")+".jpg", RequestBody.create(MediaType.parse("image/jpeg"), file));
 *         APIService.call(APIService.api().uploadStepImage(getApp().getCurrentOrder().getNo(), step, builder.build()), this, new OnCallBack<String>() {
 *             @Override
 *             public void onSuccess(String s) {
 *
 *             }
 *         });
 *     }
 *
 */
public class UpLoadFileUtil {
    private MultipartBody.Builder builder;
    private OkHttpClient.Builder okHttpClientBuilder;
    private Request.Builder requesetBuilder;
    private MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");

    public MediaType getMutilPart_Form_Data() {
        return MutilPart_Form_Data;
    }

    public void setMutilPart_Form_Data(MediaType mutilPart_Form_Data) {
        MutilPart_Form_Data = mutilPart_Form_Data;
    }

    private static UpLoadFileUtil instance;

    private UpLoadFileUtil() {
        builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requesetBuilder=new Request.Builder();
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS); // 设置连接超时时间
        okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);// 设置写入超时时间
        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);// 设置读取数据超时时间
        okHttpClientBuilder.retryOnConnectionFailure(true);// 设置进行连接失败重试
//        okHttpClientBuilder.cache(cache);
        okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

    }

    public static UpLoadFileUtil getInstance() {
        if (null == instance) {
            synchronized (UpLoadFileUtil.class) {
                if (null == instance) {
                    instance = new UpLoadFileUtil();
                }
            }
        }
        return instance;
    }


    /**
     *上传String key-value
     * @param name   接口的字段名
     * @param value  对应的值
     * @return
     */
    public UpLoadFileUtil addFormDataPart(String name,String value){
        builder.addFormDataPart(name,value);
        return this;
    }

    /**
     *上传文件
     * @param name          接口的字段名
     * @param fileName      文件名
     * @param file          文件
     * @return
     */
    public UpLoadFileUtil addFormDataPart(String name,String fileName,File file){
        builder.addFormDataPart(name,fileName,RequestBody.create(MutilPart_Form_Data, file));
        return this;
    }
    /**
     *上传文件
     * @param name          接口的字段名
     * @param fileName      文件名
     * @param filePath      文件路径
     * @return
     */
    public UpLoadFileUtil addFormDataPart(String name,String fileName,String filePath){
        File file = new File(filePath);
        if(file.exists()) {
            builder.addFormDataPart(name, fileName, RequestBody.create(MutilPart_Form_Data, file));
        }else {
            throw new RuntimeException("file can not be null");
        }
        return this;
    }

    /**
     * 设置请求头
     * @return
     */
    public UpLoadFileUtil url(String url){
        requesetBuilder.url(url);
        return this;
    }

    public UpLoadFileUtil header(String headerName,String headerValue){
        requesetBuilder.header(headerName,headerValue);
        return this;
    }

    /**
     * POST 上传
     * Looper.prepare();
     * Looper.loop();
     */
    public void postUpLoad() {
        Request request = requesetBuilder.post(builder.build()).build();
        okHttpClientBuilder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                if(callBack!=null){
                    callBack.onFailure(call,e);
                }
            }

            @Override
            public void onResponse(@Nullable Call call,@Nullable Response response) {
                if(callBack!=null){
                    callBack.onResponse(call,response);
                }

            }
        });
    }

    public interface OnPostUpLoadCallBack{
        void onFailure(Call call, IOException e);
        void onResponse(Call call, Response response);
    }
    private OnPostUpLoadCallBack callBack;

    public void setCallBack(OnPostUpLoadCallBack callBack) {
        this.callBack = callBack;
    }
}
