package com.gfq.gbaseutl.base;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.gfq.gbaseutl.net.NetworkReceiver;
import com.gfq.gbaseutl.views.round_dialog.RoundDialog;


/**
 * create by 高富强
 * on {2019/10/16} {9:45}
 * desctapion:
 */
public abstract class BaseActivity<a extends ViewDataBinding> extends AppCompatActivity {
    private RoundDialog loadingDialog;
    protected a binding;
    private NetworkReceiver networkReceiver;

    protected abstract int layout();


    protected abstract void main();

    protected abstract void onClick();


    private void checkNet() {
        networkReceiver = new NetworkReceiver(this, new NetworkReceiver.NetworkCallback() {
            @Override
            public void onAvailable() {

            }

            @Override
            public void onLost() {
                toast("网络已断开");
            }
        });
    }


    public void logd(String msg) {
        Log.d(getClass().getSimpleName(), msg);
    }

    public void toast(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkReceiver.unRegister(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNet();
        binding = DataBindingUtil.setContentView(this, layout());
        initLoadingDialog();
        main();
        onClick();
    }


    TextView textView;
    LinearLayout llBack;
    ProgressBar  bar;
    private   void initLoadingDialog(){
        loadingDialog= new RoundDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
    }


    public void showLoadingDialog(String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
                if(!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
                if(llBack.isShown()){
                    llBack.setVisibility(View.GONE);
                }
                if(!bar.isShown()){
                    bar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public RoundDialog showLoadingDialog(String msg,boolean finishThisActivity){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(msg);
                if(!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
                if(finishThisActivity){
                    llBack.setVisibility(View.VISIBLE);
                    bar.setVisibility(View.GONE);
                }
            }
        });
        return loadingDialog;
    }
    public void dismissLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(loadingDialog!=null && loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }



    /**
     * 监听蓝牙状态变化的系统广播
     */
    class BluetoothStateReceiver extends BroadcastReceiver {

//        private final String TAG = BluetoothStateReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_TURNING_ON:
                    toast("蓝牙已开启");
                    break;

                case BluetoothAdapter.STATE_TURNING_OFF:
                    toast("蓝牙已关闭");
                    break;
            }
            onBluetoothStateChanged(intent);
        }
    }
    /**
     * 蓝牙状态发生变化时回调
     */
    public void onBluetoothStateChanged(Intent intent) {

    }


}
