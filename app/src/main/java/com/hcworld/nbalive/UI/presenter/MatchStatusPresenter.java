package com.hcworld.nbalive.UI.presenter;

import android.content.Context;

import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.MatchStatusView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public class MatchStatusPresenter implements Presenter {

    private Context context;
    private MatchStatusView dataView;
    private String mid;

    public MatchStatusPresenter(Context context, MatchStatusView dataView, String mid) {
        this.context = context;
        this.dataView = dataView;
        this.mid = mid;
    }

    @Override
    public void initialized() {
        dataView.showLoading("");
        TencentService.getMatchStat(mid, "2", new RequestCallback<MatchStat>() {
                    @Override
                    public void onSuccess(MatchStat matchStat) {
                        boolean hasData = false;
                        MatchStat.MatchStatInfo data = matchStat.data;
                        List<MatchStat.StatsBean> stats = data.stats;
                        for (MatchStat.StatsBean bean : stats) {
                            if (bean.type.equals("15")) {
                                if (bean.playerStats != null && !bean.playerStats.isEmpty()) {
                                    dataView.showPlayerData(bean.playerStats);
                                    hasData = true;
                                }
                            }
                        }
                        if (!hasData) {
                            dataView.showError("暂无数据");
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataView.hideLoading();
                        dataView.showError(message);
                    }
                }

        );
    }
}
