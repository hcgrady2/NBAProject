package com.hcworld.nbalive.http.api.tencent;

import android.text.TextUtils;
import android.util.Log;

import com.hcworld.nbalive.BuildConfig;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.http.bean.match.LiveDetail;
import com.hcworld.nbalive.http.bean.match.LiveIndex;
import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;
import com.hcworld.nbalive.http.bean.news.NewsDetail;
import com.hcworld.nbalive.http.bean.news.NewsIndex;
import com.hcworld.nbalive.http.bean.news.NewsItem;
import com.hcworld.nbalive.http.bean.team.TeamsRank;
import com.hcworld.nbalive.http.bean.video.MatchVideo;
import com.hcworld.nbalive.http.match.MatchStat;
import com.hcworld.nbalive.http.match.Matchs;
import com.hcworld.nbalive.http.okhttp.OkHttpHelper;
import com.hcworld.nbalive.http.utils.MyJsonParser;
import com.hcworld.nbalive.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class TencentService {

    static Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.TENCENT_SERVER)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(OkHttpHelper.getTecentClient()).build();
    static TencentApi api = retrofit.create(TencentApi.class);



/**
     * 根据球队Id及年份月份获取赛程
     *
     * @param teamId    球队ID，默认-1为查询所有
     * @param year      年份
     * @param month     月份
     * @param isRefresh 是否重新请求数据
     * @param cbk
     *//*

    public static void getMatchCalendar(int teamId, int year, int month, boolean isRefresh, final RequestCallback<MatchCalendar> cbk) {
        final String key = "getMatchCalendar" + teamId + year + month;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            MatchCalendar match = (MatchCalendar) obj;
            cbk.onSuccess(match);
            return;
        }

        Call<String> call = api.getMatchCalendar(teamId, year, month);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    MatchCalendar match = JsonParser.parseWithGson(MatchCalendar.class, jsonStr);
                    cbk.onSuccess(match);
                    cache.put(key, match);
                    LogUtils.i("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    */
/**
     * 根据日期查询赛程
     *
     * @param date      日期。格式：YYYY-MM-DD
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */

    public static void getMatchsByDate(String date, boolean isRefresh, final RequestCallback<Matchs> cbk) {

        //不用缓存
        /*    final String key = "getMatchsByDate" + date;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            Matchs matchs = (Matchs) obj;
            cbk.onSuccess(matchs);
            return;
        }*/

        Call<String> call = api.getMatchsByData(date);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    Log.i("MatchDetailDemo", "onResponse: " + jsonStr);
                 //   LogUtils.d("resp:" + jsonStr);
                    Matchs matchs = MyJsonParser.parseWithGson(Matchs.class, jsonStr);
                    cbk.onSuccess(matchs);
                 //   cache.put(key, matchs);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


/**
     * 获取比赛信息
     *
     * @param mid
     * @param cbk
     */

    public static void getMatchBaseInfo(String mid, final RequestCallback<MatchBaseInfo.BaseInfo> cbk) {
        Call<String> call = api.getMatchBaseInfo(mid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    MatchBaseInfo info = MyJsonParser.parseWithGson(MatchBaseInfo.class, jsonStr);
                    cbk.onSuccess(info.data);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

/**
     * 获取比赛前瞻信息
     *
     * @param mid
     * @param  ：比赛数据  2：技术统计  3：比赛前瞻
     * @param cbk
     */

    public static void getMatchStat(String mid, String tabType, final RequestCallback<MatchStat> cbk) {
        Call<String> call = api.getMatchStat(mid, tabType);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    // if(!jsonStr.contains("\"type\":\"16\"")) {
                    // TODO 居然出现相同key而且数据格式不一样的json..后续再处理
                    MatchStat matchStat = MyJsonParser.parseWithGson(MatchStat.class, jsonStr);
                    cbk.onSuccess(matchStat);
                    //}
                  //  LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    public static void getMatchVideo(String mid, final RequestCallback<MatchVideo> cbk) {
        Call<String> call = api.getMatchVideo(mid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    MatchVideo videos = MyJsonParser.parseWithGson(MatchVideo.class, jsonStr);
                    cbk.onSuccess(videos);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


    public static void getMatchLiveIndex(String mid, final RequestCallback<LiveIndex> cbk) {
        Call<String> call = api.getMatchLiveIndex(mid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    LiveIndex liveIndex = MyJsonParser.parseWithGson(LiveIndex.class, jsonStr);
                    cbk.onSuccess(liveIndex);
                   // LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    public static void getMatchLiveDetail(String mid, String ids, final RequestCallback<LiveDetail> cbk) {
        Call<String> call = api.getMatchLiveDetail(mid, ids);
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    LiveDetail liveIndex = MyJsonParser.parseMatchLiveDetail(jsonStr);
                    cbk.onSuccess(liveIndex);
                  //  LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


/**
     * 获取所有新闻索引
     *
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */

    public static void getNewsIndex(Constant.NewsType newsType, boolean isRefresh, final RequestCallback<NewsIndex> cbk) {
      //  final String key = "getNewsIndex" + newsType;

        //不缓存
        /*  final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsIndex index = (NewsIndex) obj;
            cbk.onSuccess(index);
            return;
        }*/

        Call<String> call = api.getNewsIndex(newsType.getType());
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsIndex index = MyJsonParser.parseWithGson(NewsIndex.class, jsonStr);
                    cbk.onSuccess(index);
                   // cache.put(key, index);
                   // LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 根据索引获取新闻列表
     *
     * @param articleIds 索引值。多个索引以“,”分隔
     * @param isRefresh  是否重新请求数据
     * @param cbk
     */
    public static void getNewsItem(Constant.NewsType newsType, String articleIds, boolean isRefresh, final RequestCallback<NewsItem> cbk) {
        //这是缓存的，可以不用，采用 reaml 来缓存吧
        /*    final String key = "getNewsItem" + articleIds;
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsItem newsItem = (NewsItem) obj;
            cbk.onSuccess(newsItem);
            return;
        }*/

        Call<String> call = api.getNewsItem(newsType.getType(), articleIds);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsItem newsItem = MyJsonParser.parseNewsItem(jsonStr);
                    cbk.onSuccess(newsItem);
                  //  cache.put(key, newsItem);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


    /**
     * 获取新闻的详细内容
     *
     * @param newsType  文章类型
     * @param articleId 文章id
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */
    public static void getNewsDetail(Constant.NewsType newsType, String articleId, boolean isRefresh, final RequestCallback<NewsDetail> cbk) {
//        final String key = "getNewsDetail" + articleId;
 /*       final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            NewsDetail detail = (NewsDetail) obj;
            cbk.onSuccess(detail);
            return;
        }*/

        Call<String> call = api.getNewsDetail(newsType.getType(), articleId);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    NewsDetail detail = MyJsonParser.parseNewsDetail(jsonStr);
                    cbk.onSuccess(detail);
                   // cache.put(key, detail);
                 //   LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }


     /**
     * 获取NBA数据排名
     *
     * @param statType  数据类型。{@See Contast#StatType}
     * @param num       查询数据条数。建议25
     * @param tabType   比赛类型。
     * @param seasonId  赛季。格式：YYYY
     * @param isRefresh 是否重新请求数据
     * @param cbk
     */

    public static void getStatsRank(Constant.StatType statType, int num, String tabType, String seasonId, boolean isRefresh, final RequestCallback<StatsRank> cbk) {
     //   final String key = "getStatsRank" + statType + tabType + seasonId;
        Call<String> call = api.getStatsRank(statType.getType(), num, tabType, seasonId);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    StatsRank rank = MyJsonParser.parseStatsRank(jsonStr);
                    cbk.onSuccess(rank);

                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

     /**
     * 获取球队战绩排名
     *
     * @param cbk
     */

    public static void getTeamsRank(boolean isRefresh, final RequestCallback<TeamsRank> cbk) {
        final String key = "getTeamsRank";

        Call<String> call = api.getTeamsRank();
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    TeamsRank rank = MyJsonParser.parseTeamsRank(jsonStr);
                    if (rank != null) {
                        rank.all = new ArrayList<>();
                        TeamsRank.TeamBean eastTitle = new TeamsRank.TeamBean();
                        eastTitle.type = 1;
                        eastTitle.name = "东部联盟";
                        rank.all.add(eastTitle);
                        rank.all.addAll(rank.east);

                        TeamsRank.TeamBean westTitle = new TeamsRank.TeamBean();
                        westTitle.type = 2;
                        westTitle.name = "西部联盟";
                        rank.all.add(westTitle);
                        rank.all.addAll(rank.west);
                        cbk.onSuccess(rank);

                    } else {
                        cbk.onFailure("数据解析失败");

                    }
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    /**
     * 获取所有球员信息
     *
     * @param isRefresh 是否重新请求数据
     *//*
    public static void getPlayerList(boolean isRefresh, final RequestCallback<Players> cbk) {
        final String key = "getPlayerList";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            Players players = (Players) obj;
            cbk.onSuccess(players);
            return;
        }

        Call<String> call = api.getPlayerList();
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    Players players = JsonParser.parsePlayersList(jsonStr);
                    cbk.onSuccess(players);
                    cache.put(key, players);
                    LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    *//**
     * 获取所有球队信息
     *
     * @param isRefresh 是否重新请求数据
     *//*
    public static void getTeamList(boolean isRefresh, final RequestCallback<Teams> cbk) {

        final String key = "getTeamList";
        final ACache cache = ACache.get(AppUtils.getAppContext());
        Object obj = cache.getAsObject(key);
        if (obj != null && !isRefresh) {
            Teams teams = (Teams) obj;
            cbk.onSuccess(teams);
            return;
        }

        Call<String> call = api.getTeamList();
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String jsonStr = response.body();
                    Teams teams = JsonParser.parseWithGson(Teams.class, jsonStr);
                    cbk.onSuccess(teams);
                    cache.put(key, teams);
                    LogUtils.d("resp:" + jsonStr);
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                cbk.onFailure(t.getMessage());
            }
        });
    }

    public static void getVideoRealUrl(String vid, final RequestCallback<VideoRealUrl> cbk) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.TECENT_URL_SERVER)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(OkHttpHelper.getTecentClient()).build();
        TencentVideoApi api = retrofit.create(TencentVideoApi.class);

        Call<String> call = api.getVideoRealUrl(vid);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String xmlStr = response.body();
                    PullRealUrlParser parser = new PullRealUrlParser();
                    try {
                        VideoRealUrl url = parser.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
                        cbk.onSuccess(url);
                    } catch (Exception e) {
                        LogUtils.e("解析xml异常:" + e.getMessage());
                        cbk.onFailure("解析出错");
                    }
                } else {
                    cbk.onFailure("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtils.e(t.getMessage());
                cbk.onFailure(t.getMessage());
            }
        });
    }

    public static void getVideoRealUrls(String vid, final RequestCallback<VideoInfo> cbk) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.TECENT_URL_SERVER_1)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(OkHttpHelper.getTecentClient()).build();
        TencentVideoApi api = retrofit.create(TencentVideoApi.class);

        Call<String> call = api.getVideoRealUrls(vid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String resp = response.body()
                            .replaceAll("QZOutputJson=", "")
                            .replaceAll(" ", "")
                            .replaceAll("\n", "");
                    if (resp.endsWith(";"))
                        resp = resp.substring(0, resp.length() - 1);

                    LogUtils.d(resp);

                    VideoInfo info = new Gson().fromJson(resp, VideoInfo.class);
                    cbk.onSuccess(info);

                } else {
                    cbk.onFailure("");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogUtils.e("getVideoRealUrls:" + t.toString());
                cbk.onFailure(t.getMessage());
            }
        });
    }*/
}
