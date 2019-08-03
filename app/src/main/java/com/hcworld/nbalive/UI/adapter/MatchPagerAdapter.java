package com.hcworld.nbalive.UI.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hcworld.nbalive.UI.fragment.MatchDataFragment;
import com.hcworld.nbalive.UI.fragment.MatchGraphicFragment;
import com.hcworld.nbalive.UI.fragment.MatchLookForwardFragment;
import com.hcworld.nbalive.UI.fragment.MatchStatusFragment;
import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;

import java.util.List;

/**
 * Created by hcw on 2019/1/19.
 * Copyright©hcw.All rights reserved.
 */

public class MatchPagerAdapter  extends FragmentStatePagerAdapter {
    private List<String> tab_titles;
    private List<Fragment> tab_fragments;
    private boolean isStart;
    private String mid;
    private MatchBaseInfo.BaseInfo info;
    MatchDataFragment matchDataFragment;
    MatchStatusFragment matchStatusFragment;
    MatchGraphicFragment matchGraphicFragment;
    MatchLookForwardFragment matchLookForwardFragment;




   // new MatchPagerAdapter(getSupportFragmentManager(),mTitles,isStart,info);
    public MatchPagerAdapter(FragmentManager fm, List<String> tab_titles, boolean isStart, MatchBaseInfo.BaseInfo info,String mid)  {
        super(fm);
        this.tab_titles = tab_titles;
        this.tab_fragments = tab_fragments;
        this.isStart = isStart;
        this.info = info;
        this.mid  = mid;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (isStart) {
            switch (position) {
                case 0:
                    if (matchDataFragment == null){
                        matchDataFragment =   MatchDataFragment.newInstance(mid, info);
                    }
                 //   matchDataFragment =   MatchDataFragment.newInstance(mid, info);
                    fragment = matchDataFragment;
                    break;
                case 1:
                    if (matchStatusFragment == null){
                        matchStatusFragment =   MatchStatusFragment.newInstance(mid);
                    }
                    fragment = matchStatusFragment;

                    break;
                case 2:

                    if (matchGraphicFragment == null){
                   //     matchStatusFragment =   MatchStatusFragment.newInstance(mid);
                        matchGraphicFragment = MatchGraphicFragment.newInstance(mid);
                    }
                 //   matchGraphicFragment = MatchGraphicFragment.newInstance(mid);
                    fragment = matchGraphicFragment;
                    break;

                default:

                    if (matchLookForwardFragment == null){
                        //     matchStatusFragment =   MatchStatusFragment.newInstance(mid);
                        matchLookForwardFragment =  MatchLookForwardFragment.newInstance(mid);
                    }
                  //  matchLookForwardFragment = matchLookForwardFragment.newInstance(mid);
                    fragment = matchLookForwardFragment;
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    if (matchLookForwardFragment == null){
                        //     matchStatusFragment =   MatchStatusFragment.newInstance(mid);
                        matchLookForwardFragment = MatchLookForwardFragment.newInstance(mid);
                    }
                    //matchLookForwardFragment = matchLookForwardFragment.newInstance(mid);
                    fragment = matchLookForwardFragment;
                    break;
                case 1:
                default:
                    if (matchGraphicFragment == null){
                        //     matchStatusFragment =   MatchStatusFragment.newInstance(mid);
                        matchGraphicFragment = MatchGraphicFragment.newInstance(mid);
                    }
                //    matchGraphicFragment = MatchGraphicFragment.newInstance(mid);
                    fragment = matchGraphicFragment;
                    break;
            }
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return tab_titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles.get(position);
    }
}
