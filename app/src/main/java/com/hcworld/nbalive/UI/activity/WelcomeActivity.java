package com.hcworld.nbalive.UI.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.utils.DimenUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtil;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by WenTong on 2019/1/18.
 */

public class WelcomeActivity extends AppCompatActivity {

    //yi ge update
    // coordainator https://github.com/saulmm/CoordinatorExamples
    //https://github.com/Rayhahah/EasySports

    ImageView mIVEntry;
    TextView slogan;

    private static final int ANIM_TIME = 2500;

    private static final float SCALE_END = 1.15F;
    private RequestOptions requestOptions;

    private static final int[] Imgs={
            R.drawable.welcome1,R.drawable.welcome2,
            R.drawable.welcome3,R.drawable.welcome4,
            R.drawable.welcome5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 透明状态栏和导航栏，隐藏导航栏
         */
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; //hide navigationBar ;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }



        // 判断是否是第一次开启应用
        boolean isFirstOpen = SharedPreferencesUtil.getBoolean(this, SharedPreferencesUtil.FIRST_OPEN, true);
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_welcome);

        int height = DimenUtils.getScreenWidth() ;
        int width = DimenUtils.getScreenWidth();

        requestOptions = new RequestOptions().fitCenter()
                .override(width,height)
                .skipMemoryCache(true);



        mIVEntry = (ImageView) findViewById(R.id.iv_entry);
        slogan = (TextView) findViewById(R.id.slogan);
        slogan.setVisibility(View.INVISIBLE);
        startMainActivity();
    }
    private void startMainActivity(){

        //判断是否使用启动页
        SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "Setting");
        boolean isSplash = helper.getBoolean("IsUseSplash",true);
        if (isSplash){
            Random random = new Random(SystemClock.elapsedRealtime());//SystemClock.elapsedRealtime() 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）

         //   mIVEntry.setImageResource(Imgs[random.nextInt(Imgs.length)]);

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(Imgs[random.nextInt(Imgs.length)])
                    .apply(requestOptions)
                    .into(mIVEntry);

            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>()
                    {
                        @Override
                        public void call(Long aLong)
                        {
                            startAnim();
                        }
                    });
        }else {
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            WelcomeActivity.this.finish();
        }


    }

    private void startAnim() {
        slogan.setVisibility(View.VISIBLE);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mIVEntry, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mIVEntry, "scaleY", 1f, SCALE_END);
        //创建透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(slogan, "alpha", 0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY).with(alpha);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                WelcomeActivity.this.finish();
            }
        });
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}