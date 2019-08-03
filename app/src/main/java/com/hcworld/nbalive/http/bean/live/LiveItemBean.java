package com.hcworld.nbalive.http.bean.live;

/**
 * Created by hcw on 2019/1/30.
 * Copyright©hcw.All rights reserved.
 */

public class LiveItemBean {

    public String name;
    public String url;
    public String room_num;

    public String getRoom_num() {
        return room_num;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public LiveItemBean(String name, String room_num,String url) {
        this.name = name;
        this.url = url;
        this.room_num  =room_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
