package com.gfq.gbaseutl;

import android.app.Application;
import android.content.ComponentCallbacks2;

/**
 * create by 高富强
 * on {2019/11/5} {15:56}
 * desctapion:
 */
public class App extends Application implements ComponentCallbacks2 {

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
//            Glide.get(this).clearMemory();
        }
//        Glide.get(this).trimMemory(level);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        Glide.get(this).clearMemory();
    }
}
