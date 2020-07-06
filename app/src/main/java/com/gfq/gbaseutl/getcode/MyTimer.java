package com.gfq.gbaseutl.getcode;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @created GaoFuq
 * @Date 2020/6/18 17:52
 * @Descaption
 */
public class MyTimer implements LifecycleObserver {

    private static final String TAG = "MyTimer";
    private CountDownTimer timer;

    public MyTimer(long total, long interval, CallBack callBack) {
        Log.e(TAG, "new Timer: ");
        timer = new CountDownTimer(total, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(callBack!=null){
                    callBack.onTick(millisUntilFinished/1000);
                }
            }

            @Override
            public void onFinish() {
                if(callBack!=null){
                    callBack.onFinish();
                }
                timer.cancel();
            }
        };
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        Log.e(TAG, "destroy: ");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void start(){
        Log.e(TAG, "start: ");
        if(timer!=null){
            timer.start();
        }
    }


    public interface CallBack {
        void onTick(long second);

        void onFinish();
    }
}
