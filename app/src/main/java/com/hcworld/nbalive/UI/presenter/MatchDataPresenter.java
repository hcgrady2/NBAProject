package com.hcworld.nbalive.UI.presenter;

import android.content.Context;
import android.util.Log;

import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.MatchDataView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public class MatchDataPresenter implements Presenter {

    private Context context;
    private MatchDataView dataView;

    public MatchDataPresenter(Context context, MatchDataView dataView) {
        this.context = context;
        this.dataView = dataView;
    }

    @Override
    public void initialized() {
    }

    public void getMatchStats(String mid, String tabType) {
        Log.i("MatchDataDemo", "getMatchStats:222 ");
        TencentService.getMatchStat(mid, tabType, new RequestCallback<MatchStat>() {
            @Override
            public void onSuccess(MatchStat matchStat) {
                Log.i("MatchDataDemo", "success");

                MatchStat.MatchStatInfo data = matchStat.data;
                if (data != null && data.stats != null) {
                    List<MatchStat.StatsBean> stats = data.stats;
                    for (MatchStat.StatsBean bean : stats) {
                        if (bean.type.equals("12")) {
                            // 比分
                            if (bean.goals != null && !bean.goals.isEmpty()) {
                                dataView.showMatchPoint(bean.goals, data.teamInfo);
                            }
                        } else if (bean.type.equals("14")) {
                            // 球队统计
                            if (bean.teamStats != null && !bean.teamStats.isEmpty()) {
                                dataView.showTeamStatistics(bean.teamStats);
                            }
                        } else if (bean.type.equals("160")) {
                            // 场上球员
                            if (bean.groundStats != null) {
                                dataView.showGroundStats(bean.groundStats);
                            }
                        } else if (bean.type.equals("13")) {
                            // 本场最佳
                        }
                    }
                } else {
                    dataView.showError("暂无数据");
                }
             //   EventBus.getDefault().post(new RefreshCompleteEvent());
            }

            @Override
            public void onFailure(String message) {
                Log.i("MatchDataDemo", "error");
                dataView.showError(message);
              //  EventBus.getDefault().post(new RefreshCompleteEvent());
            }
        });
    }
}
