package com.hcworld.nbalive.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.MatchPagerAdapter;
import com.hcworld.nbalive.UI.base.BaseSwipeBackCompatActivity;
import com.hcworld.nbalive.UI.presenter.MatchDetailPresenter;
import com.hcworld.nbalive.UI.view.MatchDetailView;
import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.hcworld.nbalive.app.AppApplication.getContext;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */


/**
 * 为了防止滑动冲突，这里不使用滑动返回了，但是需要任务栈的异常
 */
public class MatchDetailActivity extends BaseSwipeBackCompatActivity implements MatchDetailView {

    @BindView(R.id.detail_bg)
    ImageView detail_bg;

    @BindView(R.id.tv_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.detail_tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.detail_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.tvLeftRate)
    TextView tvLeftRate;

    @BindView(R.id.tvRightRate)
    TextView tvRightRate;

    @BindView(R.id.tvMatchState)
    TextView tvMatchState;

    @BindView(R.id.tvMatchStartTime)
    TextView tvMatchStartTime;

    @BindView(R.id.tvMatchLeftScore)
    TextView tvMatchLeftScore;

    @BindView(R.id.tvMatchRightScore)
    TextView tvMatchRightScore;

    @BindView(R.id.ivMatchLeftTeam)
    ImageView ivMatchLeftTeam;

    @BindView(R.id.ivMatchRightTeam)
    ImageView ivMatchRightTeam;

    @BindView(R.id.tvMatchType)
    TextView tvMatchType;

    @BindView(R.id.toolbar_live)
    ImageView toolbar_live;

    public static final String INTENT_MID = "mid";
    public static final String INTENT_YEAR = "year";
    public static final String INTENT_HOSTNAME = "hostName";
    private String mid;
    private String year;
    private String startData;
    private boolean cctv;
    private String hostName;
    private MatchDetailPresenter presenter;
    private boolean isNeedUpdateTab = true;

    //private LayoutInflater mInflater;
    private ArrayList<String> mTitles = new ArrayList<>();//页卡标题集合
    private View view1, view2,view3;
    //private ArrayList<View> mViewList = new ArrayList<>();//页卡视图集合
  //  private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private MatchPagerAdapter adapter;

    //查询比赛数据的参数
    MatchBaseInfo.BaseInfo info;
    private  boolean isMatchStart = false;


    public static void start(Context context, String mid, String year,String hostName,boolean haveCctv,String startData) {
        Intent intent = new Intent(context, MatchDetailActivity.class);
        intent.putExtra(MatchDetailActivity.INTENT_MID, mid);
        intent.putExtra(MatchDetailActivity.INTENT_YEAR, year);
        intent.putExtra(MatchDetailActivity.INTENT_HOSTNAME, hostName);
        intent.putExtra("cctv",haveCctv);
        intent.putExtra("startData",startData);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewLayoutID() {
        return  R.layout.activity_game_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        mid = getIntent().getStringExtra(INTENT_MID);
        year = getIntent().getStringExtra(INTENT_YEAR);
        hostName = getIntent().getStringExtra(INTENT_HOSTNAME);
        cctv = getIntent().getBooleanExtra("cctv",false);
        startData = getIntent().getStringExtra("startData");

        presenter = new MatchDetailPresenter(this, this);
        presenter.getMatchBaseInfo(mid);

        setSupportActionBar(toolbar);
        //去掉标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //事件抽离，todo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });


        RequestOptions mRequestOptions = RequestOptions.bitmapTransform(new BlurTransformation(20,3) )
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                .skipMemoryCache(true)
                .placeholder(R.drawable.staples)
                .error(R.drawable.staples)
                .fallback(R.drawable.staples);

        Glide.with(getContext()).load(R.drawable.staples).apply(mRequestOptions).into(detail_bg);

        toolbar_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MatchDetailActivity.this, MatchLiveActivity.class);
                intent.putExtra(MatchDetailActivity.INTENT_MID, mid);
                intent.putExtra(MatchDetailActivity.INTENT_HOSTNAME, hostName);
                intent.putExtra("cctv",cctv);
                intent.putExtra("startData",startData);
             //   intent.putExtra(MatchDetailActivity.INTENT_YEAR, year);
                 startActivity(intent);

                /*  if (isMatchStart){
                }else {
                    ToastUtils.showShort(getContext(),"比赛还为开始");
                }*/

            }
        });
    }


    @Override
    public void showLoading(String msg) {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(String msg) {
    }

    /**
     * 这里更新底部 tab
     * @param names, title
     * @param isStart
     */
    @Override
    public void showTabViewPager(ArrayList<String> names, boolean isStart) {
        isNeedUpdateTab = false;
        mTitles = names;

      //  fragmentArrayList.add( MatchDataFragment.newInstance(mid, info));
        //fragmentArrayList.add(new MatchStatusFragment());
        //fragmentArrayList.add(new MatchGraphicFragment());
        isMatchStart = isStart;
        adapter = new MatchPagerAdapter(getSupportFragmentManager(),mTitles,isStart,info,mid);
        //adapter = new VPGameDetailAdapter(this, names, getSupportFragmentManager(), mid, isStart, info);
        mViewPager.setAdapter(adapter);//给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(3);
       // mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager,false);
       // mTabLayout.setTabMode(TabLayout.MODE_FIXED);


    }

    @Override
    public void showMatchInfo(MatchBaseInfo.BaseInfo matchBaseInfo) {
        info = matchBaseInfo;
        Log.i("MatchInfoDetail", "showMatchInfo: " + matchBaseInfo.toString());
        toolbar_title.setText(info.leftName + " @ " + info.rightName);
        //int marginLeft = (DimenUtils.getScreenWidth() - toolbar_title.getWidth())/2;
        //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       // layoutParams.leftMargin = marginLeft;
     //   toolbar_title.setLayoutParams(layoutParams);

        if (!TextUtils.isEmpty(info.leftWins) && !TextUtils.isEmpty(info.leftLosses))
            tvLeftRate.setText(info.leftWins + "胜" + info.leftLosses + "负");
        if (!TextUtils.isEmpty(info.rightWins) && !TextUtils.isEmpty(info.rightLosses))
            tvRightRate.setText(info.rightWins + "胜" + info.rightLosses + "负");


        String startTime = info.startDate + info.startHour;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM月dd日HH:mm");
        // 当前比赛状态
        String state = "未开始";
        try {
            Date startDate = format.parse(year + startTime);
            Date todayDate = new Date();
            if (startDate.getTime() > todayDate.getTime()) { // 未开始
                if (isNeedUpdateTab) { // 更新TAB
                    presenter.getTab(false);
                }
            } else {
                state = info.quarterDesc;
                // 第四节 00:00 或 加时n 00:00 表示比赛已经结束
                if (((state.contains("第4节") || state.contains("加时"))
                        && !info.leftGoal.equals(info.rightGoal))
                        && state.contains("00:00")) {
                    state = "已结束";
                }
                if (isNeedUpdateTab) {
                    presenter.getTab(true);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvMatchState.setText(state);
        tvMatchType.setText(info.desc);
        tvMatchStartTime.setText(info.startDate + "   " + info.startHour + "   " + info.venue);
        tvMatchLeftScore.setText(info.leftGoal);
        tvMatchRightScore.setText(info.rightGoal);


       // ivMatchLeftTeam.setController(FrescoUtils.getController(info.leftBadge, ivMatchLeftTeam));
        //ivMatchRightTeam.setController(FrescoUtils.getController(info.rightBadge, ivMatchRightTeam));

        Glide.with(getContext()).load(info.leftBadge).into(ivMatchLeftTeam);
        Glide.with(getContext()).load(info.rightBadge).into(ivMatchRightTeam);

    }



}