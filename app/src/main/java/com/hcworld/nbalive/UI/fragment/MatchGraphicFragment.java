package com.hcworld.nbalive.UI.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.MatchGraphicAdapter;
import com.hcworld.nbalive.UI.base.BaseLazyFragment;
import com.hcworld.nbalive.UI.presenter.MatchGraphicPresenter;
import com.hcworld.nbalive.UI.view.MatchGraphicView;
import com.hcworld.nbalive.event.RefreshCompleteEvent;
import com.hcworld.nbalive.event.RefreshEvent;
import com.hcworld.nbalive.http.bean.match.LiveDetail;
import com.hcworld.nbalive.support.OnLvScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/19.
 * Copyright©hcw.All rights reserved.
 */

public class MatchGraphicFragment extends BaseLazyFragment implements MatchGraphicView {

    @BindView(R.id.refresh)
    RelativeLayout materialRefreshLayout;
    @BindView(R.id.snlScrollView)
    ListView lvMatchLive;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.view_line)
    View viewLine;

    MatchGraphicPresenter presenter;

    private int mListViewHeight = 0;

    private List<LiveDetail.LiveContent> list = new ArrayList<>();
    private MatchGraphicAdapter adapter;
    private String mid;

    private boolean isVisibleToUser; // 是否可见。可见才进行刷新

    public static MatchGraphicFragment newInstance(String mid) {
        Bundle args = new Bundle();
        args.putString("mid", mid);
        MatchGraphicFragment fragment = new MatchGraphicFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView( R.layout.fragment_graphic_fragment);
        Log.i("ResumeDemo", "onViewCreated: ");
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
     //   presenter.shutDownTimerTask();
     //   presenter.initialized();
        mid = getArguments().getString("mid");
        adapter = new MatchGraphicAdapter(list, mActivity, R.layout.item_list_match_live);
        lvMatchLive.setAdapter(adapter);
        lvMatchLive.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    lvMatchLive.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    lvMatchLive.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mListViewHeight = lvMatchLive.getHeight();
                lvMatchLive.setOnScrollListener(new OnLvScrollListener(mListViewHeight) {
                    @Override
                    public void onBottom() {
                        presenter.getMoreContent();
                    }
                });
            }
        });

        mid = getArguments().getString("mid");
   //     presenter.initialized();
        presenter = new MatchGraphicPresenter(MatchGraphicFragment.this.getContext(), this, mid);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mActivity != null) {
            mActivity.invalidateOptionsMenu();
        }
    }



    @Override
    public void addList(List<LiveDetail.LiveContent> detail, boolean front) {
        //刷新数据
        Log.i("GraphicFragment", "addList: " + detail.get(0).content);
        viewLine.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new RefreshCompleteEvent());
        if (front)
            list.addAll(0, detail);
        else
            list.addAll(detail);
        adapter.notifyDataSetChanged();
     //   hideLoadingDialog();

    }

    @Override
    public void showError(String message) {
        //presenter.shutDownTimerTask();
        EventBus.getDefault().post(new RefreshCompleteEvent());
     //   hideLoadingDialog();
        lvMatchLive.setEmptyView(emptyView);
    }


    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        if (isVisibleToUser) {
            presenter.shutDownTimerTask();
            presenter.initialized();
        }
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
        presenter.shutDownTimerTask();
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        presenter.shutDownTimerTask();
        presenter.initialized();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        presenter.shutDownTimerTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
