package com.hcworld.nbalive.UI.presenter;

import android.content.Context;

import com.hcworld.nbalive.UI.fragment.DataFragment;
import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.StatsRankView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.utils.Constant;

import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public class StatsRankPresenterImpl implements Presenter {

    private Context context;
    private StatsRankView rankView;


    public StatsRankPresenterImpl(Context context, StatsRankView rankView) {
        this.context = context;
        this.rankView = rankView;
    }


    @Override
    public void initialized() {
      //  rankView.showStatsRank(interactor.getTabs(), interactor.getS·tats());
    }

    public void requestStatsRank(Constant.StatType curStat, String curTab) {
        TencentService.getStatsRank(curStat, 20, curTab, DataFragment.seasonId, true, new RequestCallback<StatsRank>() {
            @Override
            public void onSuccess(StatsRank statsRank) {
                List<StatsRank.RankItem> list = statsRank.rankList;
                if (list != null && !list.isEmpty()) {
                    rankView.showStatList(list);
                } else {
                    rankView.showError("暂无数据");
                }
            }

            @Override
            public void onFailure(String message) {
                rankView.showError(message);
            }
        });
    }
}

