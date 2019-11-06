package com.gfq.gbaseutl.a_gfq_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

//import com.serenegiant.xingnongAdViewer.App;
//import com.serenegiant.xingnongAdViewer.util.serial.Packet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//import io.reactivex.disposables.CompositeDisposable;


public class BaseActivity extends AppCompatActivity {

    Integer count = 0;

    Long lastTouch = 0L;

//    private CompositeDisposable mCompositeDisposable;


   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastTouch > 500) {
            count = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            count += h1;
            lastTouch = System.currentTimeMillis();
        }
        if (count >= 10) {
            count = 0;
            playAudio("open_manager");
            //openSetting();
        }
        return super.onTouchEvent(event);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clearDisposable();
//        ((App) getApplication()).unRegisterSerialCallback(this);
        if (player != null) {
            player.stop();
        }
    }


    /*public void openSetting() {
        new ManagerDialog(this).show();
    }*/

    protected Handler mHandler = new Handler();


//    @Override
//    public Handler handler() {
//        return mHandler;
//    }


//    @Override
//    public void onData(App client, Packet packet) {
//
//    }
//
//
//    @Override
//    public void onError(Packet packet) {
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        ((App) getApplication()).registerCallback(this);
    }

    private BroadcastReceiver beatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onBeat();
        }
    };

    protected void onBeat() {
        System.out.println("on beat");
    }


    private int getFile(String name) {
        return getResources().getIdentifier(name,
                "raw", getPackageName());
    }


    protected void playAudio(String name) {
        playAudio(name, null);
    }


    MediaPlayer player;


    protected void playAudio(String name, Runnable runnable) {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
        player = MediaPlayer.create(this, getFile(name));
        if (runnable != null) {
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    runnable.run();
                }
            });
        }

        player.start();
    }


//    public App getApp() {
//        return (App) getApplication();
//    }


    public void onTimeout() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (getApp().getCurrentMachineId() == null && checkMachineId()) {
            Toast.makeText(this, "该设备还未完成初始化", Toast.LENGTH_LONG).show();
            //openSetting();
        }*/
        LocalBroadcastManager.getInstance(this).registerReceiver(beatReceiver, new IntentFilter("magic_mirror_beat_success"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(beatReceiver);
    }

    public boolean checkMachineId() {
        return true;
    }


}
