package com.hcworld.nbalive.http.bean.team;

import com.hcworld.nbalive.http.bean.base.Base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public class TeamsRank extends Base {

    public List<TeamBean> all;

    public List<TeamBean> east;
    public List<TeamBean> west;

    public static class TeamBean implements Serializable {
        public String teamId;
        public String name;
        public String badge;
        public String serial;
        public String color;
        public String detailUrl;

        public int win;
        public int lose;
        public String rate;
        public String difference; //胜场差

        public int type = 0; // 0：具体排名数据   1：东部    2：西部
    }
}
