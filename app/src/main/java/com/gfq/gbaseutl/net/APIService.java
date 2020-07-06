package com.gfq.gbaseutl.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.gfq.gbaseutl.ad.ADOnCallBack;
import com.gfq.gbaseutl.net.download.ProgressInterceptor;
import com.gfq.gbaseutl.net.download.ProgressListener;
import com.gfq.gbaseutl.util.ComUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gfq.gbaseutl.net.NetworkManager.isNetworkConnected;


public class APIService {
    private static APIInterface apiInterface;
    // 缓存文件最大限制大小50M
    private static final long cacheSize = 1024 * 1024 * 50;
    // 设置缓存文件路径
    private static String cacheDirectory = Environment.getExternalStorageDirectory() + "/yzh_caches";
    private static Cache cache = new Cache(new File(cacheDirectory), cacheSize);
    private static OkHttpClient mClient;
    private static String token = "";


    public static final String BASE_URL = "待定义";

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                .writeTimeout(30, TimeUnit.SECONDS)// 设置写入超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取数据超时时间
                .retryOnConnectionFailure(true)// 设置进行连接失败重试
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("token", token)
                            .build();
                    return chain.proceed(request);
                })
                .cache(cache)// 设置缓存
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiInterface = retrofit.create(APIInterface.class);
    }

    public static String msg;

    public static void setToken(String tt) {
        token = tt;
    }

    public static APIInterface api() {
        return apiInterface;
    }

    public static <T> void call(Observable<API<T>> apiObservable, OnCallBack<T> onCallBack) {
        Log.e("APIService token = ", token);
        apiObservable.compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(tApi -> {
                    msg = tApi.getMsg();
                    if (tApi.getStatus() == 200 || tApi.getStatus() == 204) {
                        return tApi.getData();
                    } else {
                        throw new RuntimeException("xxx");
                    }
                }))
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        onCallBack.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ComUtil.toast(msg);
                        onCallBack.onError(msg);
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    public static void downLoadFile(String url, DownloadCallBack<ResponseBody> downloadCallBack) {
        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                .writeTimeout(30, TimeUnit.SECONDS)// 设置写入超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取数据超时时间
                .retryOnConnectionFailure(true)// 设置进行连接失败重试
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("token", token)
                            .build();
                    return chain.proceed(request);
                }).addInterceptor(new ProgressInterceptor(new ProgressListener() {
                    @Override
                    public void update(String url, long bytesRead, long contentLength, boolean done) {
                        if (downloadCallBack != null) {
                            float l = (float) bytesRead / contentLength;
                            int progress = (int) (l * 100);
                            if (progress > 0 && progress <= 100) {
                                Log.e("APIService", "update: progress" + progress);
                                downloadCallBack.onProgress(progress);
                            }
                        }
                    }
                }))
                .cache(cache)// 设置缓存
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(mClient)
                .baseUrl(BASE_URL)
                .build();
        APIInterface apiInterface = retrofit.create(APIInterface.class);
        apiInterface.executeDownload(url).subscribe(new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody t) {
                downloadCallBack.next(t);
            }

            @Override
            public void onError(Throwable e) {
                ComUtil.toast(msg);
                downloadCallBack.onError(msg);
            }

            @Override
            public void onComplete() {
            }
        });
    }
    public interface DownloadCallBack<T> {

        void onProgress(int progress);

        void next(T data);

        void onError(String msg);

    }

    public static <T> void callForAD(Observable<API<T>> apiObservable, Context context, ADOnCallBack<T> onCallBack) {
        apiObservable.compose(upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(tApi -> {
                    if (tApi.getStatus() == 200) {
                        return tApi.getData();
                    } else {
                        throw new ExceptionHandle.ServerException(tApi.getStatus(), tApi.getMsg());
                    }
                }))
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T t) {
                        onCallBack.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCallBack.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

}
