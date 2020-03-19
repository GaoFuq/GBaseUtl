package com.gfq.gbaseutl.util;

/**
 * create by 高富强
 * on {2020/3/16} {10:51}
 * desctapion:
 */

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * android Q 之前可用
 *
 * WiFi连接管理
 * 申请权限
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * 动态权限
 * Manifest.permission.ACCESS_COARSE_LOCATION
 * Manifest.permission.ACCESS_FINE_LOCATION
 */
public class WifiUtils {
    private static WifiUtils utils = null;

    public WifiUtils(Context context) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiUtils getInstance(Context context) {
        if (utils == null) {
            synchronized (WifiUtils.class) {
                if (utils == null) {
                    utils = new WifiUtils(context);
                }
            }
        }
        return utils;
    }

    private WifiManager wifiManager;

    /**
     * wifi是否打开
     *
     * @return
     */
    public boolean isWifiEnable() {
        boolean isEnable = false;
        if (wifiManager != null) {
            if (wifiManager.isWifiEnabled()) {
                isEnable = true;
            }
        }
        return isEnable;
    }

    /**
     * 打开WiFi
     */
    public void openWifi() {
        if (wifiManager != null && !isWifiEnable()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WiFi
     */
    public void closeWifi() {
        if (wifiManager != null && isWifiEnable()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取WiFi列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        List<ScanResult> resultList = new ArrayList<>();
        if (wifiManager != null && isWifiEnable()) {
            resultList.addAll(wifiManager.getScanResults());
        }
        return resultList;
    }

    /**
     * 有密码连接
     *
     * @param ssid
     * @param pws
     */
    public void connectWifiPws(String ssid, String pws) {
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        int netId = wifiManager.addNetwork(getWifiConfig(ssid, pws, true));
        wifiManager.enableNetwork(netId, true);
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    //连接上一次连接的wifi
    public void reConnect(int networkId) {
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks == null || configuredNetworks.size() == 0) return;
        for (int i = 0; i < configuredNetworks.size(); i++) {
            if (networkId == -1) {
                wifiManager.enableNetwork(configuredNetworks.get(i).networkId, true);
            } else if (networkId == configuredNetworks.get(i).networkId) {
                wifiManager.enableNetwork(networkId, true);
            }
        }
    }

    /**
     * 无密码连接
     *
     * @param ssid
     */
    public void connectWifiNoPws(String ssid) {
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        int netId = wifiManager.addNetwork(getWifiConfig(ssid, "", false));
        wifiManager.enableNetwork(netId, true);
    }

    /**
     * wifi设置
     *
     * @param ssid
     * @param pws
     * @param isHasPws
     */
    private WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        WifiConfiguration tempConfig = isExist(ssid);
        if (tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }
        if (isHasPws) {
            config.preSharedKey = "\"" + pws + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /**
     * 得到配置好的网络连接
     *
     * @param ssid
     * @return
     */
    private WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        if (configs != null && configs.size() > 0) {
            for (WifiConfiguration config : configs) {
                if (config.SSID.equals("\"" + ssid + "\"")) {
                    return config;
                }
            }
        }
        return null;
    }
}
