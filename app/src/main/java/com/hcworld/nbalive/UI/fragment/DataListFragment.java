package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.BaseWebActivity;
import com.hcworld.nbalive.UI.adapter.StatsRankAdapter;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.presenter.StatsRankPresenterImpl;
import com.hcworld.nbalive.UI.view.StatsRankView;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.event.DataRefreshEvent;
import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.support.SpaceItemDecoration;
import com.hcworld.nbalive.utils.Constant;
import com.hcworld.nbalive.utils.DimenUtils;
import com.hcworld.nbalive.utils.NetworkUtils;
import com.hcworld.nbalive.utils.ProgressBarUtils;
import com.hcworld.nbalive.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by hcw on 2019/2/16.
 * Copyright©hcw.All rights reserved.
 */

public class DataListFragment extends StateFragment implements StatsRankView{

    public StatsRankPresenterImpl presenter;


    @BindView(R.id.point_main)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.point_recyclerview)
    RecyclerView recyclerView;

    private List<StatsRank.RankItem> mList = new ArrayList<>();
    private StatsRankAdapter adapter;

    public static final String INTENT_INT_INDEX = "intent_int_index";
    Constant.StatType statType= Constant.StatType.POINT;
    private ProgressBarUtils mProgress;


    public static DataListFragment newInstance(int type) {
        DataListFragment dataListFragment = new DataListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DataListFragment.INTENT_INT_INDEX, type);
        Constant.StatType statTypeBundle;
        switch (type) {
            case 0:
                statTypeBundle = Constant.StatType.POINT;
                break;
            case 1:
                 statTypeBundle = Constant.StatType.REBOUND;
                break;
            case 2:
                statTypeBundle = Constant.StatType.ASSIST;
                break;
            case 3:
                statTypeBundle = Constant.StatType.BLOCK;
                break;
            case 4:
            default:
                statTypeBundle = Constant.StatType.STEAL;
                break;
        }
        bundle.putSerializable(NewslistFragment.INTENT_INT_INDEX, statTypeBundle);
        dataListFragment.setArguments(bundle);

        return dataListFragment;
    }



    @Override
    public int getLayoutResID() {
        return R.layout.fragment_point;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        //http://sportsnba.qq.com/player/statsRank?statType=point&num=20&tabType=1&seasonId=2019
        Log.i("ProgressDemo", "onViewCreated: ");
        if (mProgress == null){
            mProgress = new ProgressBarUtils(DataListFragment.this.getContext());
            mProgress.initProgress();
        }

        if(NetworkUtils.hasNetwork(getContext().getApplicationContext())){
            statType = (Constant.StatType) getArguments().getSerializable(INTENT_INT_INDEX);
            mProgress.show();
            presenter.requestStatsRank(statType, DataFragment.statsType);
        }else {
            ToastUtils.makeShortText(getString(R.string.has_no_network),getContext().getApplicationContext());
        }

    }

    @Override
    public void initValue() {
        super.initValue();
        adapter = new StatsRankAdapter(mList, this.getActivity(), R.layout.data_stats_item);
        adapter.setOnItemClickListener(new OnListItemClickListener<StatsRank.RankItem>() {
            @Override
            public void onItemClick(View view, int position, StatsRank.RankItem data) {
                BaseWebActivity.start(DataListFragment.this.getActivity(), data.playerUrl, data.playerName, true, true);
                //   BaseWebActivity.start(mActivity, data.playerUrl, data.playerName, true, true);
            }
        });
        if (mProgress == null){
            mProgress = new ProgressBarUtils(DataListFragment.this.getContext());
            mProgress.initProgress();

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(DimenUtils.dpToPxInt(3)));
        presenter = new StatsRankPresenterImpl(getContext().getApplicationContext(), this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProgress.show();
                presenter.requestStatsRank(statType, DataFragment.statsType);
            }
        });
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
        mProgress.hide();
    }

    @Override
    public void showError(String msg) {
        mProgress.hide();
    }

    @Override
    public void showStatsRank(Map<String, Constant.TabType> tab, Map<String, Constant.StatType> stat) {

    }

    @Override
    public void showStatList(List<StatsRank.RankItem> list) {
        mList.clear();
        mList.addAll(list);
        complete();
    }
    private void complete() {
        //   recyclerView.setEmptyView(emptyView);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        mProgress.hide();
    }




    // 处理登录信息的 Event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(DataRefreshEvent loginEvent){
        Log.i("PorgressDemo", "onLoginEvent: ");

        swipeRefreshLayout.setRefreshing(true);
         presenter.requestStatsRank(statType, DataFragment.statsType);

        if (swipeRefreshLayout != null){
            Log.i("PorgressDemo", "swipeRefreshLayout not null : ");

        }

        if (presenter == null){
            Log.i("PorgressDemo", "presenter is null: ");

        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getChildFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        EventBus.getDefault().unregister(this);
    }
}

