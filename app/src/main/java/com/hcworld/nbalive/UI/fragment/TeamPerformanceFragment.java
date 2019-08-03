package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.BaseWebActivity;
import com.hcworld.nbalive.UI.adapter.TeamsRankAdapter;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.presenter.TeamSortPresenter;
import com.hcworld.nbalive.UI.view.TeamSortView;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.UI.widget.SupportRecyclerView;
import com.hcworld.nbalive.http.bean.team.TeamsRank;
import com.hcworld.nbalive.support.SpaceItemDecoration;
import com.hcworld.nbalive.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class TeamPerformanceFragment extends StateFragment  implements TeamSortView {
   /**
    *  Todo
    *  1、东西部联盟分页
    *  2、点击查看详情
   *
   * */

    @BindView(R.id.performance_main)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.performance_recyclerview)
    SupportRecyclerView recyclerView;

    private TeamsRankAdapter adapter;
    private List<TeamsRank.TeamBean> list = new ArrayList<>();

    private TeamSortPresenter presenter;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_performance;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void showError(String msg) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showTeamSort(List<TeamsRank.TeamBean> teamlist) {
        list.clear();
        list.addAll(teamlist);
        for (int i = 0; i < teamlist.size(); i ++){
            Log.i("TeamSort", "showTeamSort: " + teamlist.get(i).name);

        }
        complete();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new TeamSortPresenter(getContext().getApplicationContext(), this);
        presenter.requestTeamsRank(false);

    }

    @Override
    public void initValue() {
        super.initValue();

        adapter = new TeamsRankAdapter(list, getContext().getApplicationContext(), R.layout.perform_fragment_item, R.layout.perform_fragment_item_title);
        adapter.setOnItemClickListener(new OnListItemClickListener<TeamsRank.TeamBean>() {
            @Override
            public void onItemClick(View view, int position, TeamsRank.TeamBean data) {
                BaseWebActivity.start(TeamPerformanceFragment.this.getContext(), data.detailUrl, data.name, true, true);
               // ToastUtils.makeShortText("item:" + data.name,getContext().getApplicationContext());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpaceItemDecoration(DimenUtils.dpToPxInt(1)));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              //  requestMatchs(date,false);
                presenter.requestTeamsRank(false);
            }
        });
    }

    private void complete() {
       // recyclerView.setEmptyView(emptyView);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
      // materialRefreshLayout.finishRefresh();
       // materialRefreshLayout.finishRefreshLoadMore();

    }


}
