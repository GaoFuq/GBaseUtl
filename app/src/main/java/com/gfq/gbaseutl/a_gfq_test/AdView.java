package com.gfq.gbaseutl.a_gfq_test;

import android.content.Context;
import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

//import com.bumptech.glide.Glide;
//import com.shuyu.gsyvideoplayer.GSYVideoManager;
//import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
//import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import java.util.ArrayList;
import java.util.List;



public class AdView extends FrameLayout {


    private ViewPager mViewPager;
    private Adapter mAdapter;
    private boolean paused = false;
    CleanVideoView videoPlayer;
    List<Ad> mAds = new ArrayList<>();
    private boolean xxx = true;

    public AdView(Context context) {
        super(context);
        initView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAd();
    }

    private void getAd() {

        mAds.clear();
        Ad ad1 = new Ad();
        ad1.setType("视频");
        ad1.setPath("https://video.hibixin.com/video/standard/b71a0303-fc4e-41f3-8f30-a7f44c40a4d4.mp4");
        ad1.setTime(15);

        Ad ad2 = new Ad();
        ad2.setType("视频");
        ad2.setPath("https://tvideo.bxapp.cn/video/standard/c7f095c2-2893-4f8a-84b3-a5f686f343d3.mp4");
        ad2.setTime(15);

      /*  Ad ad3 = new Ad();
        ad3.setType("图片");
        ad3.setPath("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg");
        ad3.setTime(3);*/


        mAds.add(ad1);
        mAds.add(ad2);
//        mAds.add(ad3);

        Log.e("TAG", "数据ad1" + mAds.get(0).getPath());
        Log.e("TAG", "数据ad2" + mAds.get(1).getPath());
        mAdapter.notifyDataSetChanged();
        next(mViewPager.getCurrentItem());


    }

    private void initView() {
        mViewPager = new ViewPager(getContext());
        addView(mViewPager);
        mAdapter = new Adapter();
//        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                next(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        initPlayer();
    }

    private void removeVideoFromParent(){
        if (videoPlayer == null){
            return;
        }
        if (videoPlayer.getParent() == null){
            return;
        }
        ViewGroup parent = (ViewGroup) videoPlayer.getParent();
        parent.removeView(videoPlayer);
    }

    private void initPlayer() {
        videoPlayer = new CleanVideoView(getContext());
        videoPlayer.clean();
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置旋转
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setVisibility(INVISIBLE);
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        videoPlayer.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {

            }

            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {

            }

            @Override
            public void onClickStartError(String url, Object... objects) {

            }

            @Override
            public void onClickStop(String url, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickResume(String url, Object... objects) {

            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                removeVideoFromParent();
                next(mViewPager.getCurrentItem()+1);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {
                removeVideoFromParent();
                next(mViewPager.getCurrentItem()+1);
            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {

            }

            @Override
            public void onClickBlank(String url, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {

            }
        });
    }

    private void next(int i) {
        if (paused) {
            return;
        }
        int position = i % mAds.size();
        Ad item = mAds.get(position);

        if (item.getType().equals("视频")) {
            Log.e("next", "next: ");
            removeVideoFromParent();
            videoPlayer.setUp(item.getPath(),true,item.getType());
            videoPlayer.startPlayLogic();
            videoPlayer.clean();
            addView(videoPlayer);
            mViewPager.setCurrentItem(i);
            return;
        }
        mViewPager.setCurrentItem(i);

        int time = 5000;
        if (item.getTime() != 0) {
            time = item.getTime() * 1000;
        }
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                //转场效果
                next(mViewPager.getCurrentItem()+1);

            }
        }, time);
    }

    public void releaseVideo() {
        if (videoPlayer != null) {
            videoPlayer.setVideoAllCallBack(null);
        }
        paused = true;
    }

    public void onPause() {
        if (videoPlayer != null) {
            videoPlayer.onVideoPause();
        }
        paused = true;
    }

    public void onResume() {
        if (videoPlayer != null && mAds.size() > 0) {
            Ad ad = mAds.get(mViewPager.getCurrentItem() % mAds.size());
            if (ad.getType().equals("视频")) {
                videoPlayer.onVideoResume();
            }
        }
        paused = false;
        if (mViewPager != null) {
            if (mAds.size() > 0) {
                next(mViewPager.getCurrentItem());
            }
        }
    }

    public static final int mLooperCount = 500;

    private int getRealCount() {
        return mAds == null ? 0 : mAds.size();
    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return o == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.e("TAG", "适配器开始工作");
            if (mAds.size() == 0) {
                return new ImageView(container.getContext());
            }
            int newPosition = position % mAds.size();
            Ad item = mAds.get(newPosition);
            if (item.getType().equals("图片")) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                String path = "http://192.168.10.171:8888" + "/" + item.getPath();
                Log.e("TAG", "图片地址" + path);
//                Glide.with(container.getContext()).load(item.getPath()).into(imageView);//-------------------------------
                container.addView(imageView);
                return imageView;
            }
            return new ImageView(container.getContext());
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);

        }
    }

    @Override
    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
    }


}
