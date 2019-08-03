package com.hcworld.nbalive.http.bean.news;

import com.google.gson.annotations.SerializedName;
import com.hcworld.nbalive.http.bean.base.Base;

import java.util.List;
import java.util.Map;

/**
 * Created by hcw on 2019/1/11.
 * Copyright©hcw.All rights reserved.
 */
public class NewsDetail extends Base {

    public String title;
    @SerializedName("abstract")
    public String abstractX;
    public List<Map<String, String>> content;

    public String url;
    public String imgurl;
    public String imgurl1;
    public String imgurl2;
    public String time;
    public String atype;
    public String commentId;
    public String newsAppId;
}
