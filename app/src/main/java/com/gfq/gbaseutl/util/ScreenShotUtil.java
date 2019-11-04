package com.gfq.gbaseutl.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * create by 高富强
 * on {2019/11/1} {14:39}
 * desctapion: 屏幕截屏  ScreenShotUtil.shot(activity);  也可以将shot()方法 copy到外部扩展使用
 * 注意：要先获取读写内存卡权限
 */
public class ScreenShotUtil {

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src))
            return false;

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }


    /**
     * 获取当前屏幕截图，不包含状态栏（Status Bar）。
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap screenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
//        View view = activity.getWindow().getContainer().peekDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);
        int width = (int) getDeviceDisplaySize(activity)[0];
        int height = (int) getDeviceDisplaySize(activity)[1];

        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();

        return ret;
    }

    public static float[] getDeviceDisplaySize(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        float[] size = new float[2];
        size[0] = width;
        size[1] = height;

        return size;
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static void shot(Activity activity, String savePath, String saveName) {
        if(!savePath.startsWith("/")){
            savePath="/"+savePath;
        }
         if(!savePath.endsWith("/")){
            savePath=savePath+"/";
        }
        File file = new File(Environment.getExternalStorageDirectory() + savePath);
        if (!saveName.endsWith(".jpg")) {
            saveName = saveName + ".jpg";
        }
        File jpg = new File(Environment.getExternalStorageDirectory() + savePath, saveName);
        Bitmap bitmap = screenShot(activity);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!jpg.exists()) {
                jpg.createNewFile();
            }

            boolean ret = save(bitmap, jpg, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                Toast.makeText(activity, "截图已保持至 " + jpg.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
