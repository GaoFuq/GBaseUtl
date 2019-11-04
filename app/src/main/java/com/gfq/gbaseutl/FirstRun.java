package com.gfq.gbaseutl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.KeyEvent;

/**
 * create by 高富强
 * on {2019/11/4} {14:49}
 * desctapion:
 */
public class FirstRun {
    public static void firstRun(Activity activity,Class<? super Activity> firstRunClazz,Class<? super Activity> otherClazz) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("firstRun", Context.MODE_PRIVATE);
        boolean firstRun = sharedPreferences.getBoolean("first", true);
        if (firstRun) {
            sharedPreferences.edit().putBoolean("first", false).apply();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.startActivity(new Intent(activity,firstRunClazz));
                    activity.finish();
                }
            }, 2000);
        } else {
            activity.startActivity(new Intent(activity, otherClazz));
        }
    }

//    onKeyDown{//监听返回键，使返回键同home键
    /* if(KeyEvent.KEYCODE_BACK==keyCode){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }*/
//    }
}
