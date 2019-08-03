package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.MatchPlayerDataAdapter;
import com.hcworld.nbalive.UI.base.BaseLazyFragment;
import com.hcworld.nbalive.UI.presenter.MatchStatusPresenter;
import com.hcworld.nbalive.UI.view.MatchStatusView;
import com.hcworld.nbalive.event.RefreshCompleteEvent;
import com.hcworld.nbalive.event.RefreshEvent;
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

public class MatchStatusFragment extends BaseLazyFragment implements MatchStatusView{


    @BindView(R.id.tvPlayerDataLeft)
    TextView tvPlayerDataLeft;
    @BindView(R.id.lvPlayerDataLeft)
    ListView lvPlayerDataLeft;

    @BindView(R.id.tvPlayerDataRight)
    TextView tvPlayerDataRight;
    @BindView(R.id.lvPlayerDataRight)
    ListView lvPlayerDataRight;


    private MatchStatusPresenter presenter;


    private List<MatchStat.PlayerStats> left = new ArrayList<>();
    private List<MatchStat.PlayerStats> right = new ArrayList<>();
    private MatchPlayerDataAdapter leftAdapter;
    private MatchPlayerDataAdapter rightAdapter;


    public static MatchStatusFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchStatusFragment fragment = new MatchStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContentView( R.layout.fragment_match_status);
        EventBus.getDefault().register(this);

        leftAdapter = new MatchPlayerDataAdapter(left, mActivity);
        lvPlayerDataLeft.setAdapter(leftAdapter);
        rightAdapter = new MatchPlayerDataAdapter(right, mActivity);
        lvPlayerDataRight.setAdapter(rightAdapter);


        presenter = new MatchStatusPresenter(MatchStatusFragment.this.getContext(), this, getArguments().getString("mid"));
        presenter.initialized();
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
    public void showPlayerData(List<MatchStat.PlayerStats> playerStatses) {
        Log.i("showPlayerData", "showPlayerData: " + playerStatses.get(0).subText);
        boolean isLeft = false;
        boolean isRight = false;
        left.clear();
        right.clear();
        for (MatchStat.PlayerStats item : playerStatses) {
            if (item.subText != null && !isLeft) {
                isLeft = true;
                tvPlayerDataLeft.setText(item.subText);
            } else if (item.subText != null && isLeft) {
                isRight = true;
                tvPlayerDataRight.setText(item.subText);
            } else {
                if (isRight) {
                    right.add(item);
                } else {
                    left.add(item);
                }
            }
        }
        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();


    }


    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        EventBus.getDefault().post(new RefreshCompleteEvent());
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
