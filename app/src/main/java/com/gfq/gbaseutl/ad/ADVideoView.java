package com.gfq.gbaseutl.ad;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * 用于播放视频
 */
public class ADVideoView extends StandardGSYVideoPlayer {
    public ADVideoView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public ADVideoView(Context context) {
        super(context);
    }

    public ADVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void clean(){
        this.mProgressBar.setVisibility(INVISIBLE);
        this.mCurrentTimeTextView.setVisibility(INVISIBLE);
        this.mTotalTimeTextView.setVisibility(INVISIBLE);
        this.mStartButton.setVisibility(INVISIBLE);
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
