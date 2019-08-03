package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.MatchPlayerDataAdapter;
import com.hcworld.nbalive.UI.adapter.MatchStatisticsAdapter;
import com.hcworld.nbalive.UI.base.BaseLazyFragment;
import com.hcworld.nbalive.UI.presenter.MatchDataPresenter;
import com.hcworld.nbalive.UI.view.MatchDataView;
import com.hcworld.nbalive.event.RefreshEvent;
import com.hcworld.nbalive.http.bean.match.MatchBaseInfo;
import com.hcworld.nbalive.http.match.MatchStat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/19.
 * Copyright©hcw.All rights reserved.
 */

public class MatchDataFragment extends BaseLazyFragment implements MatchDataView{


  //  @BindView(R.id.snlScrollView)
  //  ScrollView snlScrollView;

    @BindView(R.id.llMatchPoint)
    LinearLayout llMatchPoint;
    @BindView(R.id.llMatchTeamStatistics)
    LinearLayout llMatchTeamStatistics;
    @BindView(R.id.llGroundStats)
    LinearLayout llGroundStats;

    @BindView(R.id.tvMatchPoint)
    TextView tvMatchPoint;
    @BindView(R.id.tvMatchTeamStatistics)
    TextView tvMatchTeamStatistics;

    @BindView(R.id.tvRecentTitleLeft)
    TextView tvRecentTitleLeft;
    @BindView(R.id.tvRecentTitleRight)
    TextView tvRecentTitleRight;

    @BindView(R.id.lvMatchTeamStatistics)
    ListView lvMatchTeamStatistics;
    @BindView(R.id.lvGroundStats)
    ListView lvGroundStats;

    @BindView(R.id.llMatchPointHead)
    LinearLayout llMatchPointHead;
    @BindView(R.id.llMatchPointLeft)
    LinearLayout llMatchPointLeft;
    @BindView(R.id.llMatchPointRight)
    LinearLayout llMatchPointRight;

    @BindView(R.id.ivMatchPointLeft)
    ImageView ivMatchPointLeft;
    @BindView(R.id.ivMatchPointRight)
    ImageView ivMatchPointRight;


    public final static String BUNDLE_MID = "mid";
    public final static String BUNDLE_INFO = "info";
    private MatchDataPresenter presenter;

    private List<MatchStat.TeamStats> teamStats = new ArrayList<>();
    private MatchStatisticsAdapter adapter;
    private MatchPlayerDataAdapter playerDataAdapter;

    private List<MatchStat.PlayerStats> playerDataList = new ArrayList<>();
    private int teamCurrent = 0;



    public static MatchDataFragment newInstance(String mid, MatchBaseInfo.BaseInfo info) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_MID, mid);
        args.putSerializable(BUNDLE_INFO, info);
        MatchDataFragment fragment = new MatchDataFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_match_data);
        EventBus.getDefault().register(this);

        presenter = new MatchDataPresenter(MatchDataFragment.this.getContext(), this);
        presenter.getMatchStats(getArguments().getString(BUNDLE_MID), "1");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        presenter.getMatchStats(getArguments().getString(BUNDLE_MID), "1");
    }


    public int getLayoutResID() {
        return R.layout.fragment_match_data;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showMatchPoint(List<MatchStat.Goals> list, MatchStat.MatchTeamInfo teamInfo) {
        Log.i("MatchDataDemo", "showMatchPoint:asd " + teamInfo.toString());


     //   ivMatchPointLeft.setController(FrescoUtils.getController(teamInfo.leftBadge, ivMatchPointLeft));
     //   ivMatchPointRight.setController(FrescoUtils.getController(teamInfo.rightBadge, ivMatchPointRight));

        Glide.with(getContext()).load(teamInfo.leftBadge).into(ivMatchPointLeft);
        Glide.with(getContext()).load(teamInfo.rightBadge).into(ivMatchPointRight);

        MatchStat.Goals goals = list.get(0);
        List<String> head = goals.head;
        List<String> left = goals.rows.get(0);
        List<String> right = goals.rows.get(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        for (int i = 0; i < head.size() && i < left.size() && i < right.size(); i++) {
            if (llMatchPointRight.getChildAt(i + 1) != null) {
                TextView tv = (TextView) llMatchPointHead.getChildAt(i + 1);
                tv.setText(head.get(i));
            } else {
                TextView tv = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                tv.setText(head.get(i));
                tv.setLayoutParams(params);
                llMatchPointHead.addView(tv, i + 1);
            }
            if (llMatchPointLeft.getChildAt(i + 1) != null) {
                TextView tv1 = (TextView) llMatchPointLeft.getChildAt(i + 1);
                tv1.setText(left.get(i));
            } else {
                TextView tv1 = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                tv1.setText(left.get(i));
                tv1.setLayoutParams(params);
                llMatchPointLeft.addView(tv1, i + 1);
            }

            if (llMatchPointRight.getChildAt(i + 1) != null) {
                TextView tv2 = (TextView) llMatchPointRight.getChildAt(i + 1);
                tv2.setText(right.get(i));
            } else {
                TextView tv2 = (TextView) inflater.inflate(R.layout.tab_match_point, null);
                tv2.setText(right.get(i));
                tv2.setLayoutParams(params);
                llMatchPointRight.addView(tv2, i + 1);
            }
        }
        llMatchPoint.setVisibility(View.VISIBLE);
        //停止 dialog

    }

    @Override
    public void showTeamStatistics(List<MatchStat.TeamStats> teamStats) {
        Log.i("MatchDataDemo", "showMatchPoint: fffff" + teamStats.toString());
        this.teamStats.clear();
        this.teamStats.addAll(teamStats);
        if (adapter == null) {
            adapter = new MatchStatisticsAdapter(this.teamStats, mActivity, R.layout.item_list_match_team_statistics);
            lvMatchTeamStatistics.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        llMatchTeamStatistics.setVisibility(View.VISIBLE);
       // complete();
    }

    @Override
    public void showGroundStats(final MatchStat.GroundStats groundStats) {
        Log.i("MatchDataDemo", "showMatchPoint:gbbb " + groundStats.toString());
        MatchBaseInfo.BaseInfo info = (MatchBaseInfo.BaseInfo) getArguments().getSerializable(BUNDLE_INFO);

        if (info != null) {
            tvRecentTitleLeft.setText(info.leftName);
            tvRecentTitleRight.setText(info.rightName);
        }

        if (playerDataAdapter == null) {
            playerDataAdapter = new MatchPlayerDataAdapter(playerDataList, mActivity);
            lvGroundStats.setAdapter(playerDataAdapter);
        }

        updatePlayerData(groundStats.left);

        tvRecentTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamCurrent != 0) {
                    updatePlayerData(groundStats.left);
                    tvRecentTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    tvRecentTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    teamCurrent = 0;
                }
            }
        });
        tvRecentTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamCurrent == 0) {
                    updatePlayerData(groundStats.right);
                    tvRecentTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    tvRecentTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    teamCurrent = 1;
                }
            }
        });
        llGroundStats.setVisibility(View.VISIBLE);

    }
    private void updatePlayerData(List<MatchStat.GroundStats.TeamBean> players) {
        playerDataList.clear();
        for (MatchStat.GroundStats.TeamBean bean : players) {
            if (bean.head != null && bean.head.size() > 0 && !"球员".equals(bean.head.get(0))) {
                bean.head.add(0, "球员");
                bean.head.add(1, "首发");
            } else if (bean.row != null && bean.row.size() > 0 && !bean.playerName.equals(bean.row.get(0))) {
                bean.row.add(0, bean.playerName);
                bean.row.add(1, "否");
            }
            playerDataList.add(new MatchStat.PlayerStats(bean.head, bean.row, bean.playerId, bean.detailUrl));
        }

        playerDataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().unregister(this);
    }
}
