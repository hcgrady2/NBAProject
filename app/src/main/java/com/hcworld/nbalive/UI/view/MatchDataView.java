package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public interface MatchDataView extends BaseView {

    void showMatchPoint(List<MatchStat.Goals> list, MatchStat.MatchTeamInfo teamInfo);

    void showTeamStatistics(List<MatchStat.TeamStats> teamStats);

    void showGroundStats(MatchStat.GroundStats groundStats);
}
