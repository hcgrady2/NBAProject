package com.hcworld.nbalive.event;

/**
 * Created by hcw on 2019/1/10.
 * Copyright©hcw.All rights reserved.
 */

public class DateStateChange {
    String date;
    public DateStateChange(String data){
        this.date = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
