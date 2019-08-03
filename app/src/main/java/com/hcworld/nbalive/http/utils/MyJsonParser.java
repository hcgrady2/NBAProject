package com.hcworld.nbalive.http.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hcworld.nbalive.http.bean.base.Base;
import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.http.bean.match.LiveDetail;
import com.hcworld.nbalive.http.bean.news.NewsDetail;
import com.hcworld.nbalive.http.bean.news.NewsItem;
import com.hcworld.nbalive.http.bean.team.TeamsRank;
import com.hcworld.nbalive.http.match.MatchStat;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hcw on 2018/12/23.
 * Copyright©hcw.All rights reserved.
 */

public class MyJsonParser {

    static Gson gson = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(MatchStat.MaxPlayers.MatchPlayerInfo.class, new MatchPlayerInfoDefaultAdapter())
            .registerTypeHierarchyAdapter(List.class, new ListDefaultAdapter())
            .create();


    public static String parseBase(Base base, String jsonStr) {
        JSONObject jsonObj = JSON.parseObject(jsonStr);
        String data = "{}";
        for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
            if (entry.getKey().equals("code")) {
                base.code = Integer.parseInt(entry.getValue().toString());
            } else if (entry.getKey().equals("version")) {
                base.version = entry.getValue().toString();
            } else {
                data = entry.getValue().toString();
            }
        }
        return data;
    }


    //解析 NewsItem,JSON 是 阿里的 fastJson
    public static NewsItem parseNewsItem(String jsonStr) {
        NewsItem newsItem = new NewsItem();
        JSONObject data = JSON.parseObject(MyJsonParser.parseBase(newsItem, jsonStr)); // articleIds=    NullPoint
        //没有网，查不到
        if (data == null)
            return null;
        List<NewsItem.NewsItemBean> list = new ArrayList<NewsItem.NewsItemBean>();
        //Set<String> keySet = data.keySet();
        for (Map.Entry<String, Object> item : data.entrySet()) {
            Gson gson = new Gson();
            NewsItem.NewsItemBean bean = gson.fromJson(item.getValue().toString(), NewsItem.NewsItemBean.class);
            bean.index = item.getKey();
            list.add(bean);
        }
        // 由于fastjson获取出来的entrySet是乱序的  所以这边重新排序
        Collections.sort(list, new Comparator<NewsItem.NewsItemBean>() {
            @Override
            public int compare(NewsItem.NewsItemBean lhs, NewsItem.NewsItemBean rhs) {
                return rhs.index.compareTo(lhs.index);
            }
        });
        newsItem.data = list;
        return newsItem;
    }

    public static <T> T parseWithGson(Class<T> classOfT, String jsonStr) {
        return gson.fromJson(jsonStr, classOfT);
    }

    public static LiveDetail parseMatchLiveDetail(String jsonStr) {
        LiveDetail detail = new LiveDetail();
        detail.data = new LiveDetail.LiveDetailData();
        String dataStr = MyJsonParser.parseBase(detail, jsonStr);
        JSONObject data = JSON.parseObject(dataStr);
        List<LiveDetail.LiveContent> list = new ArrayList<>();
        for (Map.Entry<String, Object> item : data.entrySet()) {
            if (item.getKey().equals("teamInfo")) {
                String teamInfo = item.getValue().toString();
                detail.data.teamInfo = new Gson().fromJson(teamInfo, LiveDetail.TeamInfo.class);
            } else if (item.getKey().equals("detail")) {
                JSONObject details = JSON.parseObject(item.getValue().toString());
                if (details != null) {
                    for (Map.Entry<String, Object> entry : details.entrySet()) {
                        LiveDetail.LiveContent content;
                        String contentStr = entry.getValue().toString();
                        Gson gson = new Gson();
                        content = gson.fromJson(contentStr, LiveDetail.LiveContent.class);
                        content.id = entry.getKey();
                        list.add(content);
                    }
                }
            }
        }
        // 由于fastjson获取出来的entrySet是乱序的  所以这边重新排序
        Collections.sort(list, new Comparator<LiveDetail.LiveContent>() {
            @Override
            public int compare(LiveDetail.LiveContent lhs, LiveDetail.LiveContent rhs) {
                return rhs.id.compareTo(lhs.id);
            }
        });
        detail.data.detail = list;
        return detail;
    }

    public static TeamsRank parseTeamsRank(String jsonStr) {
        TeamsRank rank = new TeamsRank();
        String dataStr = MyJsonParser.parseBase(rank, jsonStr);
        rank.east = new ArrayList<>();
        rank.west = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(dataStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                org.json.JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象
                String title = item.getString("title");
                JSONArray teamsArray = item.optJSONArray("rows");
                for (int j = 0; j < teamsArray.length(); j++) {
                    JSONArray teamsInfo = teamsArray.getJSONArray(j);
                    Gson gson = new Gson();
                    TeamsRank.TeamBean bean = gson.fromJson(teamsInfo.getString(0), TeamsRank.TeamBean.class);
                    bean.win = teamsInfo.optInt(1);
                    bean.lose = teamsInfo.optInt(2);
                    bean.rate = teamsInfo.optString(3);
                    bean.difference = teamsInfo.optString(4);
                    if (title.equals("东部联盟")) {
                        rank.east.add(bean);
                    } else {
                        rank.west.add(bean);
                    }
                }
            }
            return rank;
        } catch (Exception e) {

        }
        return null;
    }


    public static StatsRank parseStatsRank(String jsonStr) {
        StatsRank rank = new StatsRank();
        String dataStr = MyJsonParser.parseBase(rank, jsonStr);
        JSONObject data = JSON.parseObject(dataStr);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            rank.statType = entry.getKey();
            String rankStr = entry.getValue().toString();
            Gson gson = new Gson();
            rank.rankList = gson.fromJson(rankStr, new TypeToken<List<StatsRank.RankItem>>() {
            }.getType());
        }
        return rank;
    }

    public static NewsDetail parseNewsDetail(String jsonStr) {
        NewsDetail detail = new NewsDetail();
        String dataStr = MyJsonParser.parseBase(detail, jsonStr);
        JSONObject data = JSON.parseObject(dataStr);
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getKey().equals("title")) {
                    detail.title = entry.getValue().toString();
                } else if (entry.getKey().equals("abstract")) {
                    detail.abstractX = entry.getValue().toString();
                } else if (entry.getKey().equals("content")) {
                    String contentStr = entry.getValue().toString();
                    try {
                        List<Map<String, String>> list = new LinkedList<>();
                        JSONArray jsonArray = new JSONArray(contentStr);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject item = jsonArray.getJSONObject(i); // 得到每个对象
                            Map<String, String> map = new HashMap<>();
                            if (item.get("type").equals("text")) {
                                map.put("text", item.get("info").toString());
                            } else if (item.get("type").equals("img")) {
                                String imgStr = item.get("img").toString();
                                JSONObject imgObj = JSON.parseObject(imgStr);
                                for (Map.Entry<String, Object> imgItem : imgObj.entrySet()) {
                                    if (imgItem.getKey().toString().startsWith("imgurl") && !TextUtils.isEmpty(imgItem.getValue().toString())) {
                                        JSONObject imgUrlObj = JSON.parseObject(imgItem.getValue().toString());
                                        String url = imgUrlObj.getString("imgurl");
                                        map.put("img", url);
                                        break;
                                    }
                                }
                            }
                            list.add(map);
                        }
                        detail.content = list;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (entry.getKey().equals("url")) {
                    detail.url = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl")) {
                    detail.imgurl = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl1")) {
                    detail.imgurl1 = entry.getValue().toString();
                } else if (entry.getKey().equals("imgurl2")) {
                    detail.imgurl2 = entry.getValue().toString();
                } else if (entry.getKey().equals("pub_time")) {
                    detail.time = entry.getValue().toString();
                } else if (entry.getKey().equals("atype")) {
                    detail.atype = entry.getValue().toString();
                } else if (entry.getKey().equals("commentId")) {
                    detail.commentId = entry.getValue().toString();
                } else {
                    detail.newsAppId = entry.getValue().toString();
                }
            }
        }
        return detail;
    }


}
