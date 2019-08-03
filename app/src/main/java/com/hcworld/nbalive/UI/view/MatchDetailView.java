package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;

import java.util.ArrayList;

/**
 * Created by hcw on 2019/1/13.
 * Copyright©hcw.All rights reserved.
 */

public interface MatchDetailView extends BaseView{

    void showTabViewPager(  ArrayList<String> mTitles, boolean isStart);

    void showMatchInfo(MatchBaseInfo.BaseInfo matchBaseInfo);
}
