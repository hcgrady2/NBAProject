package com.hcworld.nbalive.UI.presenter;

import android.content.Context;

import com.hcworld.nbalive.UI.presenter.base.Presenter;
import com.hcworld.nbalive.UI.view.TeamSortView;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.team.TeamsRank;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public class TeamSortPresenter implements Presenter {

    private Context context;
    private TeamSortView sortView;

    public TeamSortPresenter(Context context, TeamSortView sortView) {
        this.context = context;
        this.sortView = sortView;
    }

    @Override
    public void initialized() {

    }

    public void requestTeamsRank(boolean isRefresh) {
        sortView.showLoading("");
        TencentService.getTeamsRank(isRefresh, new RequestCallback<TeamsRank>() {
            @Override
            public void onSuccess(TeamsRank teamsRank) {
                if (teamsRank != null && teamsRank.all != null) {
                    sortView.showTeamSort(teamsRank.all);
                } else {
                    sortView.showError("暂无数据");
                }
            }

            @Override
            public void onFailure(String message) {
                sortView.showError(message);
            }
        });
    }

}
