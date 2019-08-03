package com.hcworld.nbalive.UI.activity;

/**
 * Created by hcw on 2018/12/19.
 * Copyright©hcw.All rights reserved.
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hcworld.nbalive.R;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;


public class SimplePlayer extends AppCompatActivity {

    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_play);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.i("VideoDemo", "onCreate: " + url);
        init();
    }

    private void init() {
        videoPlayer =  (StandardGSYVideoPlayer)findViewById(R.id.video_player);


    /*

        //可用1：   String source1 = "http://223.110.243.155:80/PLTV/3/224/3221227468/1.m3u8";

               2：        String source1 = "http://223.82.250.72/ysten-business/live/cctv-5/index.m3u8";

      3  String source1 = "http://121.31.30.90:8085/ysten-business/live/cctv-5/1.m3u8";

        */


 /*       待测试：



        */


 String url2 = "http://111.20.45.153/wh7f454c46tw2276190695_1478738859/dbiptv.sn.chinamobile.com/PLTV/88888890/224/3221225834/index.m3u8";
 String url3 = "http://39.134.52.171/wh7f454c46tw3773685428_-1812949401/hwottcdn.ln.chinamobile.com/PLTV/88888890/224/3221226035/index.m3u8";
String url4 = "http://223.110.245.167/ott.js.chinamobile.com/PLTV/3/224/3221226942/index.m3u8";
String url5 = "http://223.110.243.142/PLTV/2/224/3221226111/1.m3u8";
String url6 = "http://111.48.34.235/wh7f454c46tw3750732377_2143940495/huaweicdn.hb.chinamobile.com/PLTV/2510088/224/3221225772/index.m3u8";
String url7 = "http://cctv5.txty.5213.liveplay.myqcloud.com/live/cctv5_txty.m3u8";




      //  String source1 = "http://121.31.30.90:8085/ysten-business/live/cctv-5/1.m3u8";


        videoPlayer.setUp(url, true, "测试视频2");

        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.nba_logo);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
