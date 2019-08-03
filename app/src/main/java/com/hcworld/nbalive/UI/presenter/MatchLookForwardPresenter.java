package com.hcworld.nbalive.UI.presenter;

import android.content.Context;

import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.MatchLookForwardView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public class MatchLookForwardPresenter implements Presenter {

    private Context context;
    private MatchLookForwardView forwardView;

    public MatchLookForwardPresenter(Context context, MatchLookForwardView forwardView) {
        this.context = context;
        this.forwardView = forwardView;
    }

    @Override
    public void initialized() {
    }

    public void getMatchStat(String mid, String tabType) {
        TencentService.getMatchStat(mid, tabType, new RequestCallback<MatchStat>() {
            @Override
            public void onSuccess(MatchStat matchStat) {
                MatchStat.MatchStatInfo data = matchStat.data;
                forwardView.showTeamInfo(data.teamInfo);
                List<MatchStat.StatsBean> stats = data.stats;
                if (stats != null){
                    for (MatchStat.StatsBean bean : stats) {
                        if (bean.type.equals("1")) {
                            if (bean.vs != null && !bean.vs.isEmpty()) {
                                forwardView.showHistoryMatchs(bean.vs);
                            }
                        } else if (bean.type.equals("2")) {
                            if (bean.teamMatches != null && bean.teamMatches.left != null && bean.teamMatches.right != null) {
                                forwardView.showRecentMatchs(bean.teamMatches);
                            }
                        } else if (bean.type.equals("3")) {
                            if (bean.teamMatches != null && bean.teamMatches.left != null && bean.teamMatches.right != null) {
                                forwardView.showFutureMatchs(bean.teamMatches);
                            }
                        } else if (bean.type.equals("13")) {
                            if (bean.maxPlayers != null && !bean.maxPlayers.isEmpty()) {
                                forwardView.showMaxPlayer(bean.maxPlayers);
                            }
                        }
                    }
                }

              //  EventBus.getDefault().post(new RefreshCompleteEvent());
            }

            @Override
            public void onFailure(String message) {
                forwardView.showError(message);
              //  EventBus.getDefault().post(new RefreshCompleteEvent());
            }
        });
    }
}
