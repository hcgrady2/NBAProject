package com.hcworld.nbalive.UI.view;


import com.hcworld.nbalive.http.bean.team.TeamsRank;

import java.util.List;

/**
 * @author yuyh.
 * @date 2016/7/5.
 */
public interface TeamSortView extends BaseView {

    void showTeamSort(List<TeamsRank.TeamBean> list);
}
