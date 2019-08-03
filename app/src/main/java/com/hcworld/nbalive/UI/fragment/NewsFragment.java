package com.hcworld.nbalive.UI.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.widget.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import butterknife.BindView;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class NewsFragment extends StateFragment {
    private static final String TAG = "HomeActivityDemo";

    private final static int fragmentSize = 5;

    @BindView(R.id.news_viewPager)
    ViewPager mPager;

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

   // private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "今日头条", "新闻资讯", "视频集锦", "最佳进球", "赛场花絮"
    };
    private MyPagerAdapter mAdapter;

    public static final int NEWS_TYPE_TOUTIAO = 0;
    public static final int NEWS_TYPE_MESSAGE = 1;
    public static final int NEWS_TYPE_VIDEO = 2;
    public static final int NEWS_TYPE_BESTBALL = 3;
    public static final int NEWS_TYPE_HIGHLIGHTS = 4;

    @Override
    public void initValue() {
        super.initValue();
        if (mAdapter == null){
            mAdapter = new MyPagerAdapter(getChildFragmentManager());
        }
     /*   //这个要优化一下，再尝试一下懒加载
        if (mFragments.size() != 3){
            mFragments.clear();
            mFragments.add(NewslistFragment.newInstance(NEWS_TYPE_TOUTIAO));
            mFragments.add(NewslistFragment.newInstance(NEWS_TYPE_MESSAGE));
            mFragments.add(NewslistFragment.newInstance(NEWS_TYPE_VIDEO));
            mFragments.add(NewslistFragment.newInstance(NEWS_TYPE_BESTBALL));
            mFragments.add(NewslistFragment.newInstance(NEWS_TYPE_HIGHLIGHTS));
        }*/
        magicIndicator.setBackgroundColor(Color.WHITE);
        final CommonNavigator commonNavigator = new CommonNavigator(NewsFragment.this.getContext().getApplicationContext());
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
        mPager.setCurrentItem(0);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_news;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onCreate: 1");
        super.onDestroy();
    }

    //FragmentStatePagerAdapter
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
            NewslistFragment newslistFragment = null;
            switch (position){
                case 0:
                    newslistFragment=  NewslistFragment.newInstance(NEWS_TYPE_TOUTIAO);
                    break;
                case 1:
                    newslistFragment= NewslistFragment.newInstance(NEWS_TYPE_MESSAGE);
                    break;
                case 2:
                    newslistFragment=  NewslistFragment.newInstance(NEWS_TYPE_VIDEO);
                    break;
                case 3:
                    newslistFragment=  NewslistFragment.newInstance(NEWS_TYPE_BESTBALL);
                    break;
                case 4:
                newslistFragment= NewslistFragment.newInstance(NEWS_TYPE_HIGHLIGHTS);
                    break;
                    default:
                        newslistFragment= NewslistFragment.newInstance(NEWS_TYPE_TOUTIAO);
                      break;
            }
            return newslistFragment;


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
}
