/*
package com.gfq.gbaseutl.a_gfq_test;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;


public class MainActivity extends BaseActivity {
    private AdView mAdView;
    public static String name;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        getSp();
        initAdView();

    }

//    private void getSp() {
//        SharedPreferences data = getSharedPreferences("data", MODE_PRIVATE);
//        name = data.getString("name", "");
//        token = data.getString("token", "");
//    }




    private void initAdView() {
        mAdView = new AdView(this);
        ((ViewGroup) findViewById(R.id.ad_wrapper)).addView(mAdView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.releaseVideo();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.onPause();
        }
    }

    @Override
    protected void onBeat() {
        super.onBeat();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.onResume();
        }

    }



}
*/
