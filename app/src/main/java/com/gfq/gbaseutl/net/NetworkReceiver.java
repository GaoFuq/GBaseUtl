package com.gfq.gbaseutl.net;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

//private void initNetworkReceiver(){
//     NetworkReceiver networkReceiver=new NetworkReceiver(this,new NetworkReceiver.NetworkCallback(){
//          @Override
//          public void onAvailable(){
//
//          }
//
//          @Override
//          public void onLost(){
//
//          }
//      });
// }

public class NetworkReceiver extends BroadcastReceiver {

    private NetworkCallback networkCallback;
    private ConnectivityManager.NetworkCallback callback;

    public NetworkReceiver() {
    }

    public NetworkReceiver(Activity activity, NetworkCallback networkCallback) {
        this.networkCallback = networkCallback;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

            callback = new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    networkCallback.onAvailable();
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    networkCallback.onLost();
                }
            };
            connManager.registerDefaultNetworkCallback(callback);
        } else {
            IntentFilter intentFilter = new IntentFilter();
            //intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            //intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

            activity.registerReceiver(this, intentFilter);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (NetworkManager.isNetworkConnected(context)) {
//                this.networkCallback.onAvailable();
                if (isWifiConnected(context)) {
                    this.networkCallback.onAvailable();
                }
            } else {
                if (!isWifiConnected(context)) {
                    this.networkCallback.onLost();
                }
            }
        }
    }

    /**
     * 取消网络监听
     *
     * @param activity
     */
    public void unRegister(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            connManager.unregisterNetworkCallback(callback);
        } else {
            activity.unregisterReceiver(this);
        }
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo != null && wifiNetworkInfo.isConnected();
    }


    /**
     * 网络状态回调
     */
    public interface NetworkCallback {

        /**
         * 网络可用
         */
        void onAvailable();

        /**
         * 网络不可用，丢失连接
         */
        void onLost();

    }


}
