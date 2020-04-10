package com.gfq.gbaseutl.util.wifi;

/**
 * create by 高富强
 * on {2020/3/26} {11:08}
 * desctapion:
 */
public interface WifiStateListener {
    void connected();
    void failed();
    void connecting();
    void opened();
    void disable();
}
