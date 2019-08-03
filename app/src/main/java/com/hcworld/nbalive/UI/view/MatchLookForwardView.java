package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */
public interface MatchLookForwardView {

    void showTeamInfo(MatchStat.MatchTeamInfo info);

    void showMaxPlayer(List<MatchStat.MaxPlayers> maxPlayers);

    void showHistoryMatchs(List<MatchStat.VS> vs);

    void showRecentMatchs(MatchStat.TeamMatchs teamMatches);

    void showFutureMatchs(MatchStat.TeamMatchs teamMatches);

    void showError(String message);

}
