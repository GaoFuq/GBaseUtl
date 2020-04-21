package com.gfq.gbaseutl.ad;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gfq.gbaseutl.R;


public class ShowADActivity2 extends AppCompatActivity {
    AdView adView;
    NoticeView noticeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adView = new AdView(this);
        noticeView = new NoticeView(this);
        setContentView(R.layout.adview);
        FrameLayout container = findViewById(R.id.container);
        container.addView(adView);
        container.addView(noticeView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adView.releaseVideo();
    }
}
