package com.gfq.gbaseutl.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;


import com.gfq.gbaseutl.App;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

public class ComUtil {

    private static long lastClickTime;
    private final static long TIME = 800;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static void toast(String msg) {
        boolean fastDoubleClick = isFastDoubleClick();
        if (!fastDoubleClick)
            if (App.appContext != null) {
                Toast.makeText(App.appContext, msg, Toast.LENGTH_SHORT).show();
            }
    }

    public static String getFileSize(String fileSize) {
        String[] arr = {"Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        float srcsize = Float.parseFloat(fileSize);
        int index = (int) (Math.floor(Math.log(srcsize) / Math.log(1024)));
        double size = srcsize / Math.pow(1024, index);
        size = Double.parseDouble(new DecimalFormat("#.00").format(size));
        return size + arr[index];
    }


    /**
     * 对数据进行分割
     * @param str
     * @param c
     * @return
     */
    public static String[] splitStr(String str,String c){
        return str.split(c);
    }

    public static String splicingStr(List<String> list, char c){
        StringBuilder sb = new StringBuilder();
        for (String str:list){
            sb.append(str);
            sb.append(c);
        }
        return sb.substring(0,sb.length()-1);
    }
    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context,String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
    //获取本地软件版本号
    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
    /**
     * 用来判断服务是否运行.
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context,String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }



}
