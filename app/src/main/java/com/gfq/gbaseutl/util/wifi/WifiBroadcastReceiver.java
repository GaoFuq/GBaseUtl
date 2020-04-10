package com.gfq.gbaseutl.util.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

//监听wifi状态广播接收器
public class WifiBroadcastReceiver extends BroadcastReceiver {
    private  WifiStateListener listener;
    String TAG = "WifiBroadcastReceiver";

    public WifiBroadcastReceiver(WifiStateListener listener) {

        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {

            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (state) {
                /**
                 * WIFI_STATE_DISABLED    WLAN已经关闭
                 * WIFI_STATE_DISABLING   WLAN正在关闭
                 * WIFI_STATE_ENABLED     WLAN已经打开
                 * WIFI_STATE_ENABLING    WLAN正在打开
                 * WIFI_STATE_UNKNOWN     未知
                 */
                case WifiManager.WIFI_STATE_DISABLED: {
                    Log.d(TAG, "已经关闭");
                    if(listener!=null)listener.disable();
                    break;
                }
                case WifiManager.WIFI_STATE_DISABLING: {
                    Log.d(TAG, "正在关闭");
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLED: {
                    Log.d(TAG, "已经打开");
                    if(listener!=null)listener.opened();
//                        sortScaResult();
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLING: {
                    Log.d(TAG, "正在打开");
                    break;
                }
                case WifiManager.WIFI_STATE_UNKNOWN: {
                    Log.d(TAG, "未知状态");
                    break;
                }
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            Log.d(TAG, "--NetworkInfo--" + info.toString());
            if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                Log.d(TAG, "wifi没连接上");
                if(listener!=null)listener.failed();
            } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                Log.d(TAG, "wifi连接上了");
                if(listener!=null)listener.connected();
            } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                Log.d(TAG, "wifi正在连接");
                if(listener!=null)listener.connecting();
            }
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "网络列表变化了");
        }
    }
}