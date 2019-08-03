package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by hcw on 2018/9/9.
 * Copyright©hcw.All rights reserved.
 */

public abstract class BaseFragment extends Fragment {
    public View parentView;
    private boolean isViewPrepared; // 标识fragment视图已经初始化完毕
    private boolean hasFetchData; // 标识已经触发过懒加载数据


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(getLayoutResID(), container, false);
        ButterKnife.bind(this, parentView);
        initViews();
        return parentView;
    }


    public abstract int getLayoutResID();

   /* protected abstract
    @LayoutRes
    int getLayoutId();*/

    protected abstract void initViews();

    @Override
    public void onDestroy() {
        super.onDestroy();
        parentView = null;
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) parentView.findViewById(id);
    }


    protected abstract void lazyFetchData();

    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            lazyFetchData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasFetchData = false;
        isViewPrepared = false;
    }

}
