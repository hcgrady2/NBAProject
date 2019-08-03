package com.hcworld.nbalive.http.bean.live;

import io.realm.RealmObject;

/**
 * Created by hcw on 2019/2/1.
 * Copyright©hcw.All rights reserved.
 */

public class RealmSaveLiveBean  extends RealmObject{
    public String date;
    //  name#room_num#url;
    public String liveUrl;
    //这个也是查询需要的
    public String hname;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }
}
