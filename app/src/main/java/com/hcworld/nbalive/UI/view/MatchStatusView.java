package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.match.MatchStat;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public interface MatchStatusView extends BaseView {

    void showPlayerData(List<MatchStat.PlayerStats> playerStatses);
}

