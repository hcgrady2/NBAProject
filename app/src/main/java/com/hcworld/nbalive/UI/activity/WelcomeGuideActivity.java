package com.hcworld.nbalive.UI.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.GuideViewPagerAdapter;
import com.hcworld.nbalive.utils.DimenUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WenTong on 2019/1/18.
 */

public class WelcomeGuideActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;

    // 引导页图片资源
    private static final int[] pics = {
            R.layout.guide_view1,
            R.layout.guide_view2,
            R.layout.guide_view3
    };

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        int height = DimenUtils.getScreenWidth() ;
        int width = DimenUtils.getScreenWidth();

        requestOptions = new RequestOptions().fitCenter()
                .override(width,height)
                .placeholder(R.drawable.nba_default)
                .error(R.drawable.nba_default)
                .fallback(R.drawable.nba_default);

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


     /*   RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                //  .skipMemoryCache(true)
                .placeholder(R.drawable.nba_default)
                .error(R.drawable.nba_default)
                .fallback(R.drawable.nba_default);*/





        views = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);
            switch (i){
                case 0:
                    ImageView imageView = view.findViewById(R.id.guide_view_one);
                    Glide.with(WelcomeGuideActivity.this)
                            .load(R.mipmap.welcome_one)
                            .apply(requestOptions)
                            .into(imageView);
                    break;
                case 1:
                    ImageView imageView2 = view.findViewById(R.id.guide_view_two);
                    Glide.with(WelcomeGuideActivity.this)
                            .load(R.mipmap.welcome_two)
                            .apply(requestOptions)
                            .into(imageView2);
                    break;
                case 2:
                    ImageView imageView3 = view.findViewById(R.id.guide_view_three);
                    Glide.with(getApplicationContext())
                            .load(R.mipmap.welcome_three)
                            .apply(requestOptions)
                            .into(imageView3);
                    break;
                default:
                    break;
            }

            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_enter);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }

            views.add(view);

        }

        vp = (ViewPager) findViewById(R.id.vp_guide);
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PageChangeListener());

        initDots();

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
        // 如果切换到后台，就设置下次不进入功能引导页
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true); // 设置为白色，即选中状态

    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > pics.length || currentIndex == position) {
            return;
        }
        dots[position].setEnabled(true);
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterMainActivity() {
        Intent intent = new Intent(WelcomeGuideActivity.this, HomeActivity.class);
        startActivity(intent);
        SharedPreferencesUtil.putBoolean(WelcomeGuideActivity.this, SharedPreferencesUtil.FIRST_OPEN, true);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int position) {

        }

        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }

}