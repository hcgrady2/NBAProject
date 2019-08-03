package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.MatchHistoryAdapter;
import com.hcworld.nbalive.UI.adapter.MatchLMaxPlayerdapter;
import com.hcworld.nbalive.UI.adapter.MatchRecentAdapter;
import com.hcworld.nbalive.UI.base.BaseLazyFragment;
import com.hcworld.nbalive.UI.presenter.MatchLookForwardPresenter;
import com.hcworld.nbalive.UI.view.MatchLookForwardView;
import com.hcworld.nbalive.event.RefreshEvent;
import com.hcworld.nbalive.http.match.MatchStat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public class MatchLookForwardFragment extends BaseLazyFragment implements MatchLookForwardView {

    @BindView(R.id.llMaxPlayer)
    LinearLayout llMaxPlayer;
    @BindView(R.id.tvMaxPlayer)
    TextView tvMaxPlayer;
    @BindView(R.id.lvMaxPlayer)
    ListView lvMaxPlayer;
    @BindView(R.id.rlMatchTeam)
    RelativeLayout rlMatchTeam;
    @BindView(R.id.tvLeftTeamName)
    TextView tvLeftTeamName;
    @BindView(R.id.tvRightTeamName)
    TextView tvRightTeamName;

    @BindView(R.id.llHistoryMatchs)
    LinearLayout llHistoryMatchs;
    @BindView(R.id.tvHistoryMatchs)
    TextView tvHistoryMatchs;
    @BindView(R.id.lvHistoryMatchs)
    ListView lvHistoryMatchs;

    @BindView(R.id.llRecentMatchs)
    LinearLayout llRecentMatchs;
    @BindView(R.id.tvRecentMatchs)
    TextView tvRecentMatchs;
    @BindView(R.id.tvRecentTitleLeft)
    TextView tvRecentTitleLeft;
    @BindView(R.id.tvRecentTitleRight)
    TextView tvRecentTitleRight;
    @BindView(R.id.lvRecentMatchs)
    ListView lvRecentMatchs;

    @BindView(R.id.llFutureMatchs)
    LinearLayout llFutureMatchs;
    @BindView(R.id.tvFutureMatchs)
    TextView tvFutureMatchs;
    @BindView(R.id.tvFutureTitleLeft)
    TextView tvFutureTitleLeft;
    @BindView(R.id.tvFutureTitleRight)
    TextView tvFutureTitleRight;
    @BindView(R.id.lvFutureMatchs)
    ListView lvFutureMatchs;
    
    

    private MatchLookForwardPresenter presenter;
    private List<MatchStat.TeamMatchs.TeamMatchsTeam> recentList = new ArrayList<>();
    private MatchRecentAdapter recentAdapter;
    private List<MatchStat.TeamMatchs.TeamMatchsTeam> futureList = new ArrayList<>();
    private MatchRecentAdapter futureAdapter;
    private List<MatchStat.VS> vs = new ArrayList<>();
    private MatchHistoryAdapter hisAdapter;
    private List<MatchStat.MaxPlayers> maxPlayers = new ArrayList<>();
    private MatchLMaxPlayerdapter playerdapter;

    private int recentCurrent = 0;
    private int futureCurrent = 0;


    public static MatchLookForwardFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchLookForwardFragment fragment = new MatchLookForwardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView( R.layout.fragment_match_forward);

        presenter = new MatchLookForwardPresenter(MatchLookForwardFragment.this.getContext(), this);
        presenter.initialized();
        presenter.getMatchStat(getArguments().getString("mid"), "3");

        EventBus.getDefault().register(this);
        lvMaxPlayer.setFocusable(false);


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity!= null) {
            mActivity.invalidateOptionsMenu();
        }
    }



    @Override
    public void showTeamInfo(MatchStat.MatchTeamInfo info) {
        Log.i("LookForwardFragment", "showTeamInfo: " + info.leftName);
        tvLeftTeamName.setText(info.leftName);
        tvRightTeamName.setText(info.rightName);
        tvRecentTitleLeft.setText(info.leftName);
        tvRecentTitleRight.setText(info.rightName);
        tvFutureTitleLeft.setText(info.leftName);
        tvFutureTitleRight.setText(info.rightName);
        rlMatchTeam.setVisibility(View.VISIBLE);
     //   hideLoadingDialog();
    }

    @Override
    public void showMaxPlayer(List<MatchStat.MaxPlayers> maxPlayers) {
        Log.i("LookForwardFragment", "showTeamInfo: " + maxPlayers.get(0).text);
        this.maxPlayers.clear();
        this.maxPlayers.addAll(maxPlayers);
        if(playerdapter == null) {
            playerdapter = new MatchLMaxPlayerdapter(this.maxPlayers, mActivity, R.layout.item_list_maxplayer);
            lvMaxPlayer.setAdapter(playerdapter);
        }
        playerdapter.notifyDataSetChanged();
        llMaxPlayer.setVisibility(View.VISIBLE);

    }

    @Override
    public void showHistoryMatchs(List<MatchStat.VS> vs) {
        Log.i("LookForwardFragment", "showTeamInfo: " + vs.get(0).leftName);
        this.vs.clear();
        this.vs.addAll(vs);
        if(hisAdapter == null) {
            hisAdapter = new MatchHistoryAdapter(this.vs, mActivity, R.layout.item_list_match_recent);
            lvHistoryMatchs.setAdapter(hisAdapter);
        }
        hisAdapter.notifyDataSetChanged();
        llHistoryMatchs.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecentMatchs(final MatchStat.TeamMatchs teamMatches) {
        Log.i("LookForwardFragment", "showTeamInfo: " + teamMatches.right);

        recentList.clear();
        recentList.addAll(teamMatches.left);
        if (recentAdapter == null)
            recentAdapter = new MatchRecentAdapter(true, recentList, mActivity, R.layout.item_list_match_recent);
        lvRecentMatchs.setAdapter(recentAdapter);
        tvRecentTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recentCurrent != 0) {
                    recentList.clear();
                    recentList.addAll(teamMatches.left);
                    recentAdapter.notifyDataSetChanged();
                    tvRecentTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    tvRecentTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    recentCurrent = 0;
                }
            }
        });
        tvRecentTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recentCurrent == 0) {
                    recentList.clear();
                    recentList.addAll(teamMatches.right);
                    recentAdapter.notifyDataSetChanged();
                    tvRecentTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    tvRecentTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    recentCurrent = 1;
                }
            }
        });
        llRecentMatchs.setVisibility(View.VISIBLE);

    }

    @Override
    public void showFutureMatchs(final MatchStat.TeamMatchs teamMatches) {
        Log.i("LookForwardFragment", "showTeamInfo: " + teamMatches.right);
        futureList.clear();
        futureList.addAll(teamMatches.left);
        if (futureAdapter == null)
            futureAdapter = new MatchRecentAdapter(false, futureList, mActivity, R.layout.item_list_match_recent);
        lvFutureMatchs.setAdapter(futureAdapter);
        tvFutureTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (futureCurrent != 0) {
                    futureList.clear();
                    futureList.addAll(teamMatches.left);
                    futureAdapter.notifyDataSetChanged();
                    tvFutureTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    tvFutureTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    futureCurrent = 0;
                }
            }
        });
        tvFutureTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (futureCurrent == 0) {
                    futureList.clear();
                    futureList.addAll(teamMatches.right);
                    futureAdapter.notifyDataSetChanged();
                    tvFutureTitleRight.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
                    tvFutureTitleLeft.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.entity_layout));
                    futureCurrent = 1;
                }
            }
        });
        llFutureMatchs.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
       // hideLoadingDialog();
    }


    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        presenter.getMatchStat(getArguments().getString("mid"), "3");
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
