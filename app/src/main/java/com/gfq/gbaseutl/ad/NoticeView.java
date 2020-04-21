package com.gfq.gbaseutl.ad;

import android.content.Context;
import android.graphics.Color;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;


import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.net.APIService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//import com.marquee.dingrui.marqueeviewlib.MarqueeView;

/**
 * 循环滚动的通知
 */
public class NoticeView extends FrameLayout {
    private static final long NOTICE_PIRIOD = 60 * 1000;
    private MarqueeView marqueeView;
    private Context context;
    private Timer noticeLoopTimer;
    private List<Ad> noticeList = new ArrayList<>();
    private int noteIndex = 0;

    public NoticeView(@NonNull Context context) {
        super(context);
        this.context = context;
        initThis();

    }

    private void initThis() {
        setBackgroundColor(Color.TRANSPARENT);
        inflate(context, R.layout.notice_view, this);
        marqueeView = findViewById(R.id.marqueeview);
        marqueeView.setTextSize(35);
        marqueeView.setOnContentBound(new MarqueeView.OnContentBound() {
            @Override
            public void onBound() {
                next();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startNoticeLoop();
    }

    private void startNoticeLoop() {
        if (noticeLoopTimer == null) {
            noticeLoopTimer = new Timer();
            noticeLoopTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getNotice();
                }
            }, 0, NOTICE_PIRIOD);
        }
    }

    private void getNotice() {
//        APIService.callForAD(APIService.api().getAdUrl(""), context, new ADOnCallBack<List<Ad>>() {
//            @Override
//            public void onSuccess(List<Ad> notices) {
//                if (notices == null || notices.size() == 0) {
//                    marqueeView.setVisibility(INVISIBLE);
//                    return;
//                }
//                marqueeView.setVisibility(VISIBLE);
//                noticeList.clear();
//                noticeList.addAll(notices);
//                next();
//            }
//
//            @Override
//            public void onError(String e) {
//                postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        getNotice();
//                    }
//                }, 5000);
//            }
//        });
    }

    private void next() {
        if(noticeList==null||noticeList.size()==0){
            marqueeView.setVisibility(INVISIBLE);
            return ;
        }
        if(noteIndex>noticeList.size()-1){
            noteIndex=0;
        }
        Ad ad = noticeList.get(noteIndex);
        String note = ad.getNote();
        String playSpeed = ad.getPlaySpeed();
        int time = setShowTime(playSpeed);
        marqueeView.setTextSpeed(time);
        marqueeView.setContent(note);
        noteIndex++;
    }


    private int setShowTime(String playSpeed) {
        //QUICK,MEDIUM,SLOW(5,10,15)
        int temp = 0;
        if (playSpeed == null) {
            return 0;
        }
        if (playSpeed.equals("QUICK")) {
            temp = 3;
        } else if (playSpeed.equals("MEDIUM")) {
            temp = 2;
        } else if (playSpeed.equals("SLOW")) {
            temp = 1;
        }
        return temp;

    }
}
