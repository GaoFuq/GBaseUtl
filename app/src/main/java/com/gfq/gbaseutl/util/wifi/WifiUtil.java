package com.gfq.gbaseutl.util.wifi;

/**
 * create by 高富强
 * on {2020/3/26} {10:38}
 * desctapion:
 */

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.gfq.gbaseutl.App;
import com.gfq.gbaseutl.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 */
public class WifiUtil {

    List<ScanResult> mWifiList = new ArrayList<>();
    /**
     * 扫描附近wifi
     */
    private void scanWifiInfo(ListView listView) {
        String wserviceName = Context.WIFI_SERVICE;
        WifiManager mWifiManager = (WifiManager) App.appContext.getSystemService(wserviceName);

        assert mWifiManager != null;
        mWifiManager.setWifiEnabled(true);
        mWifiManager.startScan();



        mWifiList.clear();
        mWifiList = mWifiManager.getScanResults();

        if(mWifiList != null && mWifiList.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter(listView.getContext(), R.layout.item_query_work_face_dialog);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //连接wifi
                    ScanResult scanResult = mWifiList.get(position);
                    connectWifi(scanResult.SSID, "ILOVEYOU", "WPA");
                }
            });
        }
    }


    //注册广播监听wifi状态变化
   /* @Override
    public void onResume() {
        super.onResume();
        //注册广播
        wifiReceiver = new WifiBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);//监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);//监听wifiwifi连接状态广播
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);//监听wifi列表变化（开启一个热点或者关闭一个热点）
        registerReceiver(wifiReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        //取消监听
        unregisterReceiver(wifiReceiver);
    }*/


    private static final String TAG = "WifiUtils";
    public static void open() {
        WifiManager wifiManager = (WifiManager) App.appContext.getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void close() {
        WifiManager wifiManager = (WifiManager) App.appContext.getSystemService(Context.WIFI_SERVICE);
        assert wifiManager != null;
        if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }




    /**
     * 连接wifi
     * @param targetSsid wifi的SSID
     * @param targetPsd 密码
     * @param enc 加密类型
     */
//    @SuppressLint("WifiManagerLeak")
    public void connectWifi(String targetSsid, String targetPsd, String enc) {
        // 1、注意热点和密码均包含引号，此处需要需要转义引号
        String ssid = "\"" + targetSsid + "\"";
        String psd = "\"" + targetPsd + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        switch (enc) {
            case "WEP":
                // 加密类型为WEP
                conf.wepKeys[0] = psd;
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "WPA":
                // 加密类型为WPA
                conf.preSharedKey = psd;
                break;
            case "OPEN":
                //开放网络
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //3、链接wifi
        WifiManager wifiManager = (WifiManager) App.appContext.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals(ssid)) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }


    /**
     * 获取设备本身的wifi模块的Mac地址，可作为唯一识别码
     * @return
     */
    public static String getWifiMac(){
        String macAddress = null ;
        String str = "" ;
        try {
            //linux下查询网卡mac地址的命令
            Process pp = Runtime.getRuntime().exec( "cat /sys/class/net/wlan0/address" );
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;) {
                str = input.readLine();
                if (str != null ) {
                    macAddress = str.trim();// 去空格
                    break ;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macAddress;
    }
}
