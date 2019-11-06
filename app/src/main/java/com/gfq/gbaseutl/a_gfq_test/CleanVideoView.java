/*
package com.gfq.gbaseutl.a_gfq_test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.code.mvvm.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class CleanVideoView extends StandardGSYVideoPlayer {
    public CleanVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CleanVideoView(Context context) {
        super(context);
    }

    public CleanVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void clean(){
        this.mProgressBar.setVisibility(INVISIBLE);
        this.mCurrentTimeTextView.setVisibility(INVISIBLE);
        this.mTotalTimeTextView.setVisibility(INVISIBLE);
        this.mStartButton.setVisibility(INVISIBLE);
        findViewById(R.id.layout_top).setVisibility(INVISIBLE);
        findViewById(R.id.layout_bottom).setVisibility(INVISIBLE);

    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        view.setVisibility(INVISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean performClick() {
        return true;
    }
}
*/
