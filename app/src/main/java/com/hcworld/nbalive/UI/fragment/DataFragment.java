package com.hcworld.nbalive.UI.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.widget.ScaleTransitionPagerTitleView;
import com.hcworld.nbalive.event.DataRefreshEvent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class DataFragment extends StateFragment   {

    @BindView(R.id.data_viewPager)
    ViewPager mPager;

    @BindView(R.id.data_indicator)
    MagicIndicator magicIndicator;

   // private ArrayList<StateFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            " 得 分 ", " 篮 板 ", " 助 攻 ", " 盖 帽 ", " 抢 断 "
    };
    private MyPagerAdapter mAdapter;
    private Toolbar toolbar;

    Activity mActivity;
    AppCompatActivity mAppCompatActivity;
    private final static String EVERYDAY = "1";
    private final static String REGULAR = "3";
    private final static String PLAYOFFS = "2";
    public static String statsType = EVERYDAY;
    public static String seasonId = "2018";


    public static final int DATA_TYPE_POINT = 0;
    public static final int DATA_TYPE_REBOUND = 1;
    public static final int DATA_TYPE_ASSISTS = 2;
    public static final int DATA_TYPE_BLOCK = 3;
    public static final int DATA_TYPE_STEALS = 4;


    /**
     *  todo 没有网络时，界面
     * @return
     */


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_data;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//加上这句话，menu才会显示出来

    }


    @Override
    public void initValue() {
        super.initValue();
        //界面相关
        if (mAdapter == null){
            mAdapter = new MyPagerAdapter(getChildFragmentManager());
        }



        magicIndicator.setBackgroundColor(Color.WHITE);
        final CommonNavigator commonNavigator = new CommonNavigator(DataFragment.this.getContext().getApplicationContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTitles == null ? 0 : mTitles.length;
            }
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mTitles[index]);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
                return indicator;
            }
        });
        //设置缓存5个
        mPager.setOffscreenPageLimit(5);
        mPager.setAdapter(mAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
             //   magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
        mPager.setCurrentItem(0);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        mAppCompatActivity = (AppCompatActivity) mActivity;
        toolbar = (Toolbar) mAppCompatActivity.findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.stats_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    //这里应该刷新
                    //eventbus 刷新
                    case R.id.stats_everyday:
                        setStatsType(EVERYDAY);
                        //seasonId = "2018";
                        seasonId = "2018";
                        break;

                    case R.id.stats_regular:
                        setStatsType(REGULAR);
                        //seasonId = "2018";
                        seasonId = "2018";
                        break;

                    case R.id.stats_playoffs:
                       // seasonId = "2017";
                        seasonId = "2018";
                        setStatsType(PLAYOFFS);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {

            DataListFragment dataListFragment = null;

            switch (position){
                case 0:
                    dataListFragment= DataListFragment.newInstance(DataFragment.DATA_TYPE_POINT);
                    break;
                case 1:
                    dataListFragment= DataListFragment.newInstance(DataFragment.DATA_TYPE_REBOUND);
                    break;
                case 2:
                    dataListFragment=  DataListFragment.newInstance(DataFragment.DATA_TYPE_ASSISTS);
                    break;
                case 3:
                    dataListFragment= DataListFragment.newInstance(DataFragment.DATA_TYPE_BLOCK);
                    break;
                case 4:
                    dataListFragment= DataListFragment.newInstance(DataFragment.DATA_TYPE_STEALS);
                    break;
                default:
                    dataListFragment= DataListFragment.newInstance(DataFragment.DATA_TYPE_STEALS);
                    break;
            }
            return dataListFragment;

        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stats_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void setStatsType(String type){
        statsType = type;
       // dataListFragment
        EventBus.getDefault().post(new DataRefreshEvent());
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

}

