package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.utils.Constant;

import java.util.List;
import java.util.Map;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public interface StatsRankView extends BaseView{

    void showStatsRank(Map<String, Constant.TabType> tab, Map<String, Constant.StatType> stat);

    void showStatList(List<StatsRank.RankItem> list);
}
