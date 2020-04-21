package com.gfq.gbaseutl.ad;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.eftimoff.viewpagertransformers.FlipHorizontalTransformer;
import com.eftimoff.viewpagertransformers.FlipVerticalTransformer;
import com.eftimoff.viewpagertransformers.ZoomInTransformer;
import com.gfq.gbaseutl.net.APIService;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


class AdView extends FrameLayout {
    private static final long AD_PIRIOD = 5 * 60 * 1000;
    private static final String TAG = "AdView";
    ViewPager viewPager;

    ADVideoView adVideoView;

    Timer adLoopTimer;

    List<Ad> adList = new ArrayList<>();
    MyAdapter myAdapter;

    private Context context;
    private boolean paused;
    private boolean vpCanPostDelay = true;
    private View cover;

    public AdView(@NonNull Context context) {
        super(context);
        this.context = context;
        initThis();
        setBackgroundColor(Color.BLACK);
        viewPager.setBackgroundColor(Color.BLACK);
        initCover();
    }

    private void initCover() {
//        cover = inflate(context, R.layout.ad_cover, null);
    }


    private void initThis() {
        viewPager = new ViewPager(context);

        addView(viewPager);

        viewPager.setOnTouchListener((v, event) -> true);


        adVideoView = new ADVideoView(context);
        adVideoView.clean();
        adVideoView.setVideoAllCallBack(new VideoAllCallBack() {
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
                next(viewPager.getCurrentItem() + 1);
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
                next(viewPager.getCurrentItem() + 1);
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

    private void removeVideoFromParent() {
        if (adVideoView == null) {
            return;
        }
        if (adVideoView.getParent() == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) adVideoView.getParent();
        parent.removeView(adVideoView);
    }

    private void next(int i) {
        if (paused || adList.size() == 0) {
          return;
        }
        int position = i % adList.size();
        Ad item = adList.get(position);
        if (item.getContentType().equals("VIDEO")) {
            String url = item.getUrl();
            if (!url.startsWith("http")) {
//                url = BASE_URL + item.getUrl();
            }
            removeVideoFromParent();
            adVideoView.setUp(url, true, "");
            adVideoView.startPlayLogic();
            addView(adVideoView);
            viewPager.setCurrentItem(i, true);
            return;
        }
        if (item.getConversionForm() != null) {
            if (item.getConversionForm().equals("FlipVerticalTransformer") && item.getContentType().equals("IMAGE")) {
                viewPager.setPageTransformer(true, new FlipVerticalTransformer());
            } else if (item.getConversionForm().equals("FlipHorizontalTransformer") && item.getContentType().equals("IMAGE")) {
                viewPager.setPageTransformer(true, new FlipHorizontalTransformer());
            } else if (item.getConversionForm().equals("ZoomInTransformer") && item.getContentType().equals("IMAGE")) {
                viewPager.setPageTransformer(true, new ZoomInTransformer());
            }
        }
        viewPager.setCurrentItem(i, true);

        long time = setShowTime(item.getPlaySpeed());
        if (time != 0) {
            if (vpCanPostDelay) {
                viewPager.postDelayed(() -> {
                    Log.e(TAG, "post next: ");
                    vpCanPostDelay = true;
                    next(viewPager.getCurrentItem() + 1);
                }, time);
            }
            vpCanPostDelay = false;
        }
    }

    private long setShowTime(String playSpeed) {
        //QUICK,MEDIUM,SLOW(5,10,15)
        long temp = 0;
        if (playSpeed == null) {
            return 0;
        }
        if (playSpeed.equals("QUICK")) {
            temp = 5000;
        } else if (playSpeed.equals("MEDIUM")) {
            temp = 10000;
        } else if (playSpeed.equals("SLOW")) {
            temp = 15000;
        }
        return temp;

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow: ");
        startAdLoop();
    }

    private void startAdLoop() {
        if (adLoopTimer == null) {
            adLoopTimer = new Timer();
            adLoopTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getAd();
                }
            }, 0, AD_PIRIOD);
        }
    }

    private void getAd() {
//        String platformId = SharePreUtil.getPlatformId();
//        if (platformId.equals("")) {
//            return;
//        }
        //网络获取广告
//        APIService.callForAD(APIService.api().getAd(Integer.valueOf(platformId), Constants.AD_TYPE_RELEASE), context, new ADOnCallBack<List<Ad>>() {
//            @Override
//            public void onSuccess(List<Ad> ads) {
//                Log.e(TAG, "onSuccess: ");
//                if (ads == null) {
//
//                    return;
//                }
//                adList.clear();
//                adList.addAll(ads);
//                myAdapter = new MyAdapter();
//                viewPager.setAdapter(myAdapter);
//                myAdapter.notifyDataSetChanged();
//                next(viewPager.getCurrentItem());
//            }
//
//            @Override
//            public void onError(String e) {
//                viewPager.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getAd();
//                    }
//                }, 5000);
//            }
//        });
    }

    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.e(TAG, "instantiateItem: position = " + position);
            Log.e(TAG, "instantiateItem: adList size = " + adList.size());
            if (adList.size() == 0) {
                return new ImageView(container.getContext());
            }
            int newPosition = position % adList.size();
            Ad item = adList.get(newPosition);
            String url="";
//            String url = BASE_URL + item.getUrl();
            if (item.getContentType().equals("IMAGE")) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(container.getContext()).load(url).into(imageView);
                container.addView(imageView);
                return imageView;
            }
            if (item.getContentType().equals("VIDEO")) {
                return adVideoView;
            }
            return new ImageView(container.getContext());

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    public void releaseVideo() {
        if (adVideoView != null) {
            adVideoView.setVideoAllCallBack(null);
        }
        paused = true;
        if (adLoopTimer != null) {
            adLoopTimer.cancel();
            adLoopTimer = null;
        }
    }

    public void onPause() {
        if (adVideoView != null) {
            adVideoView.onVideoPause();
        }
        paused = true;
    }

    public void onResume() {
        if (adVideoView != null && adList.size() > 0) {
            Ad ad = adList.get(viewPager.getCurrentItem() % adList.size());
            if (ad.getContentType().equals("VIDEO")) {
                adVideoView.onVideoResume();
            }
        }
        paused = false;
        if (viewPager != null) {
            if (adList.size() > 0) {
                next(viewPager.getCurrentItem());
            }
        }
    }

}
