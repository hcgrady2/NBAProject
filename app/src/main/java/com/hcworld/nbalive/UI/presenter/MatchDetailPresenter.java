package com.hcworld.nbalive.UI.presenter;

import android.content.Context;

import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.MatchDetailView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;

import java.util.ArrayList;

/**
 * Created by hcw on 2019/1/13.
 * Copyright©hcw.All rights reserved.
 */

public class MatchDetailPresenter implements Presenter {

    private Context context;
    private MatchDetailView detailView;

    public MatchDetailPresenter(Context context, MatchDetailView detailView) {
        this.context = context;
        this.detailView = detailView;
    }

    @Override
    public void initialized() {
    }

    public void getTab(boolean isStart) {
        ArrayList<String> mTitles = new ArrayList<>();//页卡标题集合
        if (isStart) {
            mTitles.add("比赛数据");
            mTitles.add("技术统计");
            mTitles.add("图文直播");

        } else {
            mTitles.add("比赛前瞻");
            mTitles.add("图文直播");
        }
        detailView.showTabViewPager(mTitles, isStart);
    }

    public void getMatchBaseInfo(String mid) {
        detailView.showLoading("");
        TencentService.getMatchBaseInfo(mid, new RequestCallback<MatchBaseInfo.BaseInfo>() {
            @Override
            public void onSuccess(MatchBaseInfo.BaseInfo matchBaseInfo) {
                detailView.showMatchInfo(matchBaseInfo);
                detailView.hideLoading();
            }

            @Override
            public void onFailure(String message) {
                detailView.hideLoading();
            }
        });
    }
}
