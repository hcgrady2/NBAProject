package com.hcworld.nbalive.UI.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.CalendarActivity;
import com.hcworld.nbalive.UI.activity.MatchDetailActivity;
import com.hcworld.nbalive.UI.adapter.ScheduleAdapter;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.viewInterface.MatchLiveView;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.UI.widget.SupportRecyclerView;
import com.hcworld.nbalive.event.DateStateChange;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.match.LiveDetail;
import com.hcworld.nbalive.http.bean.match.LiveIndex;
import com.hcworld.nbalive.http.match.Matchs;
import com.hcworld.nbalive.support.SpaceItemDecoration;
import com.hcworld.nbalive.utils.DateUtils;
import com.hcworld.nbalive.utils.DimenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class LiveFragment extends StateFragment {
    @BindView(R.id.live_main)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.live_recyclerview)
    SupportRecyclerView recyclerView;

    @BindView(R.id.live_date_down)
    ImageView live_date_down;

    @BindView(R.id.live_date_up)
    ImageView live_date_up;

    @BindView(R.id.live_date_show)
    TextView live_date_show;

    @BindView(R.id.fragment_live_linear)
    LinearLayout fragment_live_linear;



/*    @BindView(R.id.emptyView)
    View emptyView;*/

    private ScheduleAdapter adapter;
    private List<Matchs.MatchsDataBean.MatchesBean> list = new ArrayList<>();

    private MatchLiveView liveView;
    private List<String> index = new ArrayList<>();
    private String firstId = "";
    private String lastId = "";
    private String mid;

    private String year = "2019";
    String date;
    String week;
    Date temp;
    SimpleDateFormat sdf;
    SimpleDateFormat weekSdf;
    /**
     * todo
     * 加上日历
     * @return
     */

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_live;
    }

    @Override
    public void initValue() {
        super.initValue();
        EventBus.getDefault().register(this);
        //顶部的日期显示
        date = DateUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");
        week = DateUtils.format(System.currentTimeMillis(), "E");
        live_date_show.setText(date.toString() +" " + week);
        adapter = new ScheduleAdapter(list, this.getActivity(), R.layout.live_fragment_item);
        adapter.setOnItemClickListener(new OnListItemClickListener<Matchs.MatchsDataBean.MatchesBean>() {
            @Override
            public void onItemClick(View view, int position, Matchs.MatchsDataBean.MatchesBean data) {
                Boolean cctv = false;
                if (data.matchInfo.broadcasters != null && data.matchInfo.broadcasters.contains("CCTV")){
                    cctv = true;
                }
                String startData = data.matchInfo.startTime.substring(0,10);
                MatchDetailActivity.start(LiveFragment.this.getActivity(), data.matchInfo.mid, year,data.matchInfo.rightName,cctv,startData);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(DimenUtils.dpToPxInt(3)));
      //  requestMatchs(date,false);
         sdf = new SimpleDateFormat("yyyy-MM-dd");
         weekSdf = new SimpleDateFormat("E");
         temp  = null;
      //这里会有一个异常，所以要用try catch捕获异常
        try {
            temp  = sdf.parse(date.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestMatchs(date,false);
            }
        });
    }


    @Override
    public void initEvent() {
        super.initEvent();

        live_date_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    temp  = sdf.parse(date);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(temp);
                calendar.add(calendar.DATE,-1);
                Date ttt = calendar.getTime();
                // date = calendar.getTime().toString();
                date = sdf.format(ttt);
                String week = weekSdf.format(ttt);
                live_date_show.setText(date + " " + week);
                requestMatchs(date,false);
            }
        });

        live_date_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    temp  = sdf.parse(date);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(temp);
                calendar.add(calendar.DATE,1);
                Date ttt = calendar.getTime();
                // date = calendar.getTime().toString();
                date = sdf.format(ttt);
                String week = weekSdf.format(ttt);
                live_date_show.setText(date + " " + week);
                requestMatchs(date,false);
            }
        });

        fragment_live_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LiveFragment.this.getContext(), CalendarActivity.class));
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setRefreshing(true);
        requestMatchs(temp.toString(),false);
    }

    private void requestMatchs(String date, boolean isRefresh) {
        TencentService.getMatchsByDate(date, isRefresh, new RequestCallback<Matchs>() {
            @Override
            public void onSuccess(Matchs matchs) {
                list.clear();
                List<Matchs.MatchsDataBean.MatchesBean> mList = matchs.getData().matches;
                if (mList != null && !mList.isEmpty()) {
                    for (Matchs.MatchsDataBean.MatchesBean bean : mList) {
                        list.add(bean);
                    }
                }
                complete();
            }

            @Override
            public void onFailure(String message) {
                //complete();
            }
        });
    }

    //这个是查询直播的
    public void getLiveIndex() {
        TencentService.getMatchLiveIndex(mid, new RequestCallback<LiveIndex>() {
            @Override
            public void onSuccess(LiveIndex liveIndex) {
                if (liveIndex.data != null && liveIndex.data.index != null && !liveIndex.data.index.isEmpty()) {
                    index.clear();
                    index.addAll(liveIndex.data.index);

                    String ids = "";
                    for (int i = 0; i < 20 && i < index.size(); i++) { // 每次最多请求20条
                        if (index.get(i).equals(firstId)) {
                            break;
                        } else {
                            ids += index.get(i) + ",";
                            lastId = index.get(i);
                        }
                    }
                    if (ids.length() > 1) {
                        ids = ids.substring(0, ids.length() - 1);
                        getLiveContent(ids, true);
                    } else {
                        liveView.showError("暂无数据");
                    }
                } else {
                    liveView.showError("暂无数据");
                }
            }

            @Override
            public void onFailure(String message) {
                liveView.showError(message);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void getLiveContent(String ids, final boolean front) {
        TencentService.getMatchLiveDetail(mid, ids, new RequestCallback<LiveDetail>() {
            @Override
            public void onSuccess(LiveDetail liveDetail) {
                firstId = index.get(0);
                liveView.addList(liveDetail.data.detail, front);
            }

            @Override
            public void onFailure(String message) {
                liveView.showError(message);
            }
        });
    }

    private void complete() {
       // recyclerView.setEmptyView(emptyView);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(DateStateChange newDate) {
        Log.i("TestDateDemo", "Event: " + newDate.getDate());
        //加一个动画
        requestMatchs(newDate.getDate(),false);
     //   mText.setText(messageEvent.getMessage());
        //设置title 单独抽离
        try {
            temp  = sdf.parse(newDate.getDate());
        }catch (Exception e){
            e.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(temp);
        Date ttt = calendar.getTime();
        date = sdf.format(ttt);
        String week = weekSdf.format(ttt);
        live_date_show.setText(date + " " + week);



    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}

