package com.hcworld.nbalive.UI.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.NormalRecyclerViewAdapter;
import com.hcworld.nbalive.http.api.aikanqiu.AikanqiuApi;
import com.hcworld.nbalive.http.bean.live.LiveItemBean;
import com.hcworld.nbalive.http.bean.live.RealmSaveLiveBean;
import com.hcworld.nbalive.utils.Constants;
import com.hcworld.nbalive.utils.DateUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

/**
 * @author        hcw
 * @time          2019/1/29 21:02
 * @description  这里查询直播源，存储到网络，本地等。
*/


public class MatchLiveActivity  extends AppCompatActivity implements NormalRecyclerViewAdapter.ButtonItemClick {
    public static Realm realm;

    public static final String INTENT_MID = "mid";
    public static final String INTENT_HOSTNAME = "hostName";


    @BindView(R.id.tv_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.detail_player)
    StandardGSYVideoPlayer detailPlayer;

    @BindView(R.id.live_cctv5_1)
    Button cctv5_1;

    @BindView(R.id.live_cctv5_2)
    Button cctv5_2;

    @BindView(R.id.live_tencent)
    Button tencent;

  //  @BindView(R.id.button_list)
 //   HorizontalListView mListView;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private boolean isPlay;
    private boolean isPause;


    private OrientationUtils orientationUtils;


    private String mid;
    private String hostName;
    private boolean cctv = false;
    private String startData;

    private boolean isUseLehu;

    private String url = "http://121.31.30.90:8085/ysten-business/live/cctv-5/1.m3u3";

    //bestTv
    private String url3 = "http://124.47.33.200/PLTV/88888888/224/3221225647/index.m3u8";
    // 648 : cctv5  646: cctv5

    //bestTv  23 24 25 26 27
    private String url4 = "http://hwottcdn.ln.chinamobile.com/PLTV/88888890/224/3221226023/index.m3u8";
    //bestTv
    private String url5 = "http://hwottcdn.ln.chinamobile.com/PLTV/88888890/224/3221226027/index.m3u8";

   /// String source1 = "ijkhttphook:http://121.31.30.90:8085/ysten-business/live/cctv-5/1.m3u8";
    String source1 = "rtmp://ivi.bupt.edu.cn/livetv/cctv5hd";

    /// rtmp://ivi.bupt.edu.cn/livetv/cctv5hd 可以用，但是卡,去掉 hd

    LayoutInflater inflater;

    private ArrayList mData;

    NormalRecyclerViewAdapter adapter;
    String currentDate2;
    private boolean cache ;

    private GSYVideoOptionBuilder gsyVideoOptionBuilder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_live);
        ButterKnife.bind(this);
        GSYVideoManager.instance().enableRawPlay(getApplicationContext());
        initVideo();
        initValue();
        initEvent();


      //  initHost("45953");
    }



    private int streamType = 1;


    private void initHost(final String room){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.ssmat.com/app/room/")
                .build();
        AikanqiuApi service = retrofit.create(AikanqiuApi.class);

        Call<ResponseBody> call = service.getSreamType(room);
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println(response.body().string());
                    String ss = response.body().string().toString();
                    JSONObject demoJson = new JSONObject(ss);
                    String type = demoJson.getJSONObject("data").getJSONObject("anchor").getString("stream_type");
                    streamType = Integer.parseInt(type);

                    initLehu(room,streamType);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: error"  +e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: json error"  +e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("JsonError", "fail: error"  +t.toString());

            }
        });


    }


    /**
     * 应该保存起来
     */

    private void initLehu(String room, final int type){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://live.lehuzhibo.cc/")
                .build();
        AikanqiuApi service = retrofit.create(AikanqiuApi.class);

        Call<ResponseBody> call = service.getLeHuJson(room);
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    lehuhostList.clear();
                    String ss = response.body().string();
                    JSONObject demoJson = new JSONObject(ss);
                    JSONObject hosts = demoJson.getJSONObject("hosts");

                    for (int i = 1; i <= hosts.length(); i++){
                        JSONObject hostOne = hosts.getJSONObject(i + "");
                        String hostOneStr  = hostOne.getString("hdl");
                        lehuhostList.add(hostOneStr);
                    }

                    JSONObject lines = demoJson.getJSONObject("lines");

                    for (int i = 1; i <=hosts.length(); i++){
                        JSONObject linesOne = lines.getJSONObject(i + "");
                        String linesOneStr = linesOne.getString("hdl");
                        lehuhostList.set(i-1,lehuhostList.get(i-1) + linesOneStr);

                        //Log.i("StreamDemo", "onResponse,直播源," + "i" + lehuhostList.get(i-1));

                    }

                    Log.i("StreamDemo", "onResponse,直播源," + "i" + lehuhostList.get(type-1));
                //    buttonItemClick.onClick(lehuhostList.get(type-1));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: error"  +e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: json error"  +e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("JsonError", "fail: error"  +t.toString());

            }
        });


    }










    private void initVideo(){

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        //imageView 是封面
        ImageView imageView = new ImageView(this);
        Glide.with(MatchLiveActivity.this).load(R.drawable.nba_logo_wordmark).into(imageView);

        gsyVideoOptionBuilder = new GSYVideoOptionBuilder()
                .setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(source1)
                .setCacheWithPlay(cache)
                .setVideoTitle("直播")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                });
        gsyVideoOptionBuilder.build(detailPlayer);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(MatchLiveActivity.this, true, true);
            }
        });

        detailPlayer.setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        });



    }


    public void initValue() {

        mid = getIntent().getStringExtra(INTENT_MID);
        hostName = getIntent().getStringExtra(INTENT_HOSTNAME);
        cctv = getIntent().getBooleanExtra("cctv",false);
        startData = getIntent().getStringExtra("startData");
        // item 传过了的 data  应该是
        currentDate2 =   DateUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
        if (startData == null || startData.equals("")){
            startData = currentDate2;
        }


        realm = Realm.getDefaultInstance();
        inflater =getLayoutInflater();
        mData = new ArrayList<LiveItemBean>();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

       // dataList = new ArrayList<>();
        recycler_view.setLayoutManager(linearLayoutManager);//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
      //  mListView.setAdapter(adapter);
     //   adapter.setListenner(this);

        adapter = new NormalRecyclerViewAdapter(this,mData);
        recycler_view.setAdapter(adapter);
        adapter.setListenner(this);

        setSupportActionBar(toolbar);
        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //CollapsingToolbarLayout
        toolbar_title.setText("视频直播");

        //事件抽离，todo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);


        //date 精确查询， hname 模糊查询
        RealmQuery<RealmSaveLiveBean> alls = realm.where(RealmSaveLiveBean.class);
        alls.beginGroup();
        alls.equalTo("date", startData);
        alls.contains("hname", hostName);
        alls.endGroup();


        //理论上只有一个结果
     //   RealmResults<RealmSaveLiveBean> realmLiveBeanList =   alls.findAll();
        RealmResults<RealmSaveLiveBean> realmLiveBeanList =   null;

        if (realmLiveBeanList == null || realmLiveBeanList.size() < 1){
            Log.i("LiveDemo", "initValue,realm have no record"  );
           //先更新界面，在插入数据库
            initAikanQiu();
       }else {

            if(!startData.equals(currentDate2)){
                return;
            }
           //更新界面
         //  mData = new ArrayList<LiveItemBean>();
            //找到了，只取第一个就行
            mData.clear();
            //判断是否有 cctv5
            if (cctv){
                LiveItemBean bean = new LiveItemBean("CCTV5","",source1);
                mData.add(bean);
            }

            RealmSaveLiveBean realmLiveBean  = realmLiveBeanList.get(0);
           //只分割 url 出name 和 link 就行     name#room_num#url;
            String[] item = realmLiveBean.getLiveUrl().split(";");
            if (item!= null && item.length >= 1){
                for (int i = 0; i < item.length; i ++){
                    String[] detail = item[i].split("#");
                    //长度应该为 3
                    if(detail.length == 3){
                        //这里也是，只使用 爱看球，不用乐虎了
                        if (detail[0].toString() != null && detail[0].toString().contains("高清直播")){
                            LiveItemBean one = new LiveItemBean(detail[0],detail[1],detail[2]);
                            mData.add(one);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
       }

    }


    /**
     *  第三方登录 ： https://github.com/wildma/UMengThirdPartyShareLogin
     *             ：https://github.com/arvinljw/SocialHelper
     */


    /**
     *
     *
     * 重要代码
     *
     *
    //  String url = "android.resource://" + getPackageName() + "/" + R.raw.test;
    //注意，用ijk模式播放raw视频，这个必须打开
    GSYVideoManager.instance().enableRawPlay(getApplicationContext());

    //断网自动重新链接，url前接上ijkhttphook:
    //String url = "ijkhttphook:https://res.exexm.com/cw_145225549855002";
    String url = "ijkhttphook:https://hdl-ws.live.sjmhw.com/live/stream-12354.flv?wsSecret=40f0df5eba27db983743c31a3293852f&wsABSTime=1548744912";
    //String url = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

    lehu:                String url = "ijkhttphook:https://hdl-ws.live.sjmhw.com/live/stream-23140.flv?wsSecret=352af36110cfe7b32db0a7dfd7896b92&wsABSTime=1548762276";



     */

    //lehu https://www.lehuzhibo.cc/lua-cgi/line-info-v3?id=12354
    //       https://live.lehuzhibo.cc/lua-cgi/line-info-v3?id=12354

    //   https://www.lehuzhibo.cc/app/schedule/basketball.json



    /**
     * 用数据库存储吧，realm
     */
    private void initAikanQiu(){

        Log.i("LiveDemo", "initValue: start to query aikanqiu ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cms.aikanqiu.com/")
                .build();


        Call<ResponseBody> call2 = service.getJson();
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    mData.clear();
                    //判断是否有 cctv5
                    if (cctv){
                        LiveItemBean bean = new LiveItemBean("CCTV5","",source1);
                        mData.add(bean);
                    }
                    //可以用线程池加快速度,注意同步

                    String ss = response.body().string();
                    JSONObject demoJson = new JSONObject(ss);

                    JSONObject dayDate = demoJson.getJSONObject("matches");

                    Iterator matchIt = dayDate.keys();


                    //弟一个 while 表示要便利所有的日期中的比赛
                    while (matchIt.hasNext()){
                        String matchKey = (String)matchIt.next();
                        JSONObject dayDate2  = (JSONObject) dayDate.get(matchKey);
                        //这里是只取了一天，还可以去好多天的，可以存储到bmob
                      //  JSONObject dayDate2 = dayDate.getJSONObject(currentDate);
                        // JSONArray liveData = new JSONArray();
                        Iterator it = dayDate2.keys();
                        //这个遍历要获取每天当中的nba
                        while (it.hasNext()) {
                            String key = (String)it.next();
                            //value 是任何类型的比赛，不一定 Nba
                            JSONObject value = (JSONObject) dayDate2.get(key);
                            if (value.getString("league_name").equals("NBA")){

                                String hname = value.getString("hname");
                                JSONArray chanels = value.getJSONArray("channels");
                                String reamSaveUrl = "";
                                for (int i = 0; i < chanels.length(); i ++){
                                    JSONObject matchItem = chanels.getJSONObject(i);
                                    String link = matchItem.getString("link");
                                    String name = matchItem.getString("name");
                                    String room_num = matchItem.getString("room_num");
                                    //   name#room_num#url;
                                    reamSaveUrl += name + "#" + room_num + "#" + link + ";";

                                    //只有是当天的比赛，才加入直播列表
                                    if (hname.contains(hostName) && matchKey.equals(startData)){
                                        //这里只使用 爱看球，不使用 lehu
                                        if (name.contains("主播")){
                                            if (null == room_num || "null".equals(room_num)){
                                                continue;
                                            }

                                            LiveItemBean one = new LiveItemBean(name,room_num,link);
                                            Log.i("whc", "onResponse，添加了 :" + room_num);
                                            mData.add(one);
                                        }
                                    }
                                }

                                //   这里是保存数据
                                //    RealmSave(itemObject.toString());

                                //2019年3月17日，这里先不保存数据了,只用查询乐虎去吧

                        /*        Log.i("LiveDemo", "开始存入数据库： " + matchKey + ":name" + hname);
                                realm.beginTransaction();
                                RealmSaveLiveBean liveBean = realm.createObject(RealmSaveLiveBean.class);
                                liveBean.setDate(matchKey);
                                liveBean.setHname(hname);
                                liveBean.setLiveUrl(reamSaveUrl);
                                realm.commitTransaction();*/


                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }  catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private void initEvent(){
        cctv5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseWebActivity.start(MatchLiveActivity.this, Constants.CCTV5_1, getString(R.string.cctv5_1), true, true);
            }
        });

        cctv5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseWebActivity.start(MatchLiveActivity.this, Constants.CCTV5_2, getString(R.string.cctv5_2), true, true);
            }
        });

        tencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseWebActivity.start(MatchLiveActivity.this, Constants.TencentLiveServer + mid, getString(R.string.Tencent), true, true);

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        detailPlayer.onVideoPause();
        MobclickAgent.onPause(this);
        isPause = true;


    }

    @Override
    protected void onResume() {
        super.onResume();
        detailPlayer.onVideoResume();
        isPause = false;
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();

        if (orientationUtils != null) {
            orientationUtils.releaseListener();
        }

        if (null != realm) {
            realm.close();
        }

        if (isPlay) {
            getCurPlay().release();
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return  detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }


    @Override
    public void onClick(String url) {
       // Toast.makeText(getApplicationContext(),"url:" + url,Toast.LENGTH_SHORT).show();
        Log.i("StreamDemo", "onClick" + url );

        playVideo(url);
    }

    private void playVideo(String liveUrl) {
        detailPlayer.release();
        gsyVideoOptionBuilder.setUrl(liveUrl)
                .setCacheWithPlay(cache)
                .setVideoTitle("比赛直播")
                .build(detailPlayer);
        gsyVideoOptionBuilder.build(detailPlayer);
        detailPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                detailPlayer.startPlayLogic();
            }
        }, 1000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

}
