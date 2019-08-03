package com.hcworld.nbalive.UI.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.http.bean.tv.TvBean;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcw on 2019/5/15.
 * Copyright©hcw.All rights reserved.
 */
public class TVActivity extends AppCompatActivity {

    private static final String TAG = "TVActivity";

    List<TvBean> tvList;


    @BindView(R.id.tv_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_player)
    StandardGSYVideoPlayer detailPlayer;

    private GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private OrientationUtils orientationUtils;

    private boolean cache ;

    private boolean isPlay;
    private boolean isPause;



    //bestTv
    private String url3 = "http://124.47.33.200/PLTV/88888888/224/3221225647/index.m3u8";
    // 648 : cctv5  646: cctv5

    //bestTv  23 24 25 26 27
    private String url4 = "http://hwottcdn.ln.chinamobile.com/PLTV/88888890/224/3221226023/index.m3u8";
    //bestTv
    private String url5 = "http://hwottcdn.ln.chinamobile.com/PLTV/88888890/224/3221226027/index.m3u8";

    ///  String source1 = "http://121.31.30.90:8085/ysten-business/live/cctv-5/1.m3u8";
    String source1 = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String url = intent.getStringExtra("url");
        source1 = url;

        Log.i(TAG, "onCreate: 获取的 url 地址是:" + source1);
        setSupportActionBar(toolbar);
        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //CollapsingToolbarLayout
        if(name != null){
            toolbar_title.setText(name);
        }else {
            toolbar_title.setText("电视直播");
        }

        //事件抽离，todo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });


        initVideo();


    }



    private void initVideo(){

        Log.i(TAG, "initVideo: ");

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(TVActivity.this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

       // GSYVideoManager.instance().enableRawPlay(getApplicationContext());

        //imageView 是封面
        ImageView imageView = new ImageView(TVActivity.this.getApplicationContext());
        ///todo 这里 图片改为
        Glide.with(TVActivity.this).load(R.drawable.nba_logo_wordmark).into(imageView);

        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                .setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(true)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(source1)
                .setCacheWithPlay(false)
                .setNeedShowWifiTip(true)
                .setStartAfterPrepared(true)
                .setVideoTitle("")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                });
        gsyVideoOptionBuilder.build(detailPlayer);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(TVActivity.this, true, true);
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });

        detailPlayer.findViewById(R.id.back).setVisibility(View.GONE);

        detailPlayer.startPlayLogic();

    }



    @Override
    protected void onPause() {
        super.onPause();
        detailPlayer.onVideoPause();
        MobclickAgent.onPause(this);
        isPause = true;

    }



    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.onVideoResume();
        isPause = false;
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();

        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }


        if (isPlay) {
            getCurPlay().release();
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return  detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    private void playVideo(String liveUrl) {
        detailPlayer.release();
        gsyVideoOptionBuilder.setUrl(liveUrl)
                .setCacheWithPlay(cache)
                .setVideoTitle("比赛直播")
                .build(detailPlayer);
        gsyVideoOptionBuilder.build(detailPlayer);
        detailPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                detailPlayer.startPlayLogic();
            }
        }, 1000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }



}


