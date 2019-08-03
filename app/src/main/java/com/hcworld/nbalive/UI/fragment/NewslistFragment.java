package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.BaseWebActivity;
import com.hcworld.nbalive.UI.activity.NewsDetailActivity;
import com.hcworld.nbalive.UI.adapter.NewsAdapter;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.news.NewsIndex;
import com.hcworld.nbalive.http.bean.news.NewsItem;
import com.hcworld.nbalive.utils.Constant;
import com.hcworld.nbalive.utils.NetworkUtils;
import com.hcworld.nbalive.utils.ProgressBarUtils;
import com.hcworld.nbalive.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcw on 2019/2/16.
 * Copyright©hcw.All rights reserved.
 */

public class NewslistFragment extends StateFragment {

    @BindView(R.id.toutiao_main)
    SwipeRefreshLayout swipeRefreshLayout;



    @BindView(R.id.toutiao_recyclerview)
    RecyclerView recyclerView;

    int lastVisibleItemPosition;
    private boolean isSlidingToLast =false;

    Constant.NewsType newsType = Constant.NewsType.BANNER;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    private ProgressBarUtils mProgress;

    public static NewslistFragment newInstance(int type) {

        NewslistFragment newslistFragment = new NewslistFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(NewslistFragment.INTENT_INT_INDEX, type);
        Constant.NewsType newsTypeBundle;
        switch (type) {
            case 0:
                newsTypeBundle = Constant.NewsType.BANNER;
                break;
            case 1:
                newsTypeBundle = Constant.NewsType.NEWS;
                break;
            case 2:
                newsTypeBundle = Constant.NewsType.VIDEO;
                break;
            case 3:
                newsTypeBundle = Constant.NewsType.DEPTH;
                break;
            case 4:
            default:
                newsTypeBundle = Constant.NewsType.HIGHLIGHT;
                break;
        }
        bundle.putSerializable(NewslistFragment.INTENT_INT_INDEX, newsTypeBundle);
        newslistFragment.setArguments(bundle);
        return newslistFragment;
    }

    private List<NewsItem.NewsItemBean> list = new ArrayList<>();
    private List<String> indexs = new ArrayList<>();
    private int start = 0; // 查询数据起始位置
    private int num = 10;

    private WeakReference<NewsAdapter> newsAdapterWeakReference;
    private NewsAdapter newsAdapter ;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_toutiao;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgress = new ProgressBarUtils(NewslistFragment.this.getContext());
        mProgress.initProgress();
        newsType = (Constant.NewsType) getArguments().getSerializable(INTENT_INT_INDEX);
        if (NetworkUtils.hasNetwork(getContext().getApplicationContext())){
            requestIndex(false);
            mProgress.show();
        }else {
            ToastUtils.makeShortText(getString(R.string.has_no_network),getContext().getApplicationContext());
        }

    }

    @Override
    public void initValue() {
        super.initValue();
        newsAdapter = new NewsAdapter(AppApplication.getContext(),list);
        newsAdapterWeakReference = new WeakReference<NewsAdapter>(newsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProgress.show();
                requestIndex(true);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(AppApplication.getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (newsAdapterWeakReference.get() != null) {
            recyclerView.setAdapter(newsAdapterWeakReference.get());
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            /**
             * 当RecyclerView的滑动状态改变时触发
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();
                    //Log.i(TAG, "onScrollStateChanged,lastVisibleItem  "+ lastVisibleItem);
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                        if (isRefreshing) {
                            if (newsAdapterWeakReference.get() != null){
                                newsAdapterWeakReference.get().notifyItemRemoved(newsAdapterWeakReference.get().getItemCount());
                            }
                            return;
                        }
                        String arcIds = parseIds();
                        if (!TextUtils.isEmpty(arcIds)) {
                            requestNews(arcIds, false, true);
                        } else {
                            ToastUtils.makeShortText("已经到底啦",getContext().getApplicationContext());
                            complete();
                        }

                    }
                }

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition =   layoutManager.findLastCompletelyVisibleItemPosition();
                if (dy > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }

            }
        });

        if (newsAdapterWeakReference.get() != null){

            newsAdapterWeakReference.get().setOnItemClickListener(new OnListItemClickListener<NewsItem.NewsItemBean>() {
                @Override
                public void onItemClick(View view, int position, NewsItem.NewsItemBean data) {

                    if (newsType == Constant.NewsType.NEWS ||  newsType == Constant.NewsType.BANNER ){
                        NewsDetailActivity.start(NewslistFragment.this.getContext(), data.title, data.index,data.imgurl);
                    }else {
                        BaseWebActivity.start(NewslistFragment.this.getContext(), data.url, data.title, true, true);
                    }
                }
            });

        }

    }

    private void requestIndex(final boolean isRefresh) {
        TencentService.getNewsIndex(newsType, isRefresh, new RequestCallback<NewsIndex>() {
            @Override
            public void onSuccess(NewsIndex newsIndex) {
                indexs.clear();
                start = 0;
                for (NewsIndex.IndexBean bean : newsIndex.data) {
                    indexs.add(bean.id);
                }
                String arcIds = parseIds();
                requestNews(arcIds, isRefresh, false);
            }
            @Override
            public void onFailure(String message) {
                Log.i("NewsDemo", "query index error ");
                mProgress.hide();
                // complete();
                //  LogUtils.i(message);
            }
        });
    }


    private void requestNews(String arcIds, final boolean isRefresh, final boolean isLoadMore) {
        //newsType 是类型
        TencentService.getNewsItem(newsType,arcIds, isRefresh, new RequestCallback<NewsItem>() {
            @Override
            public void onSuccess(NewsItem newsItem) {
                if (newsItem  != null && newsItem.data.size() > 0){
                    if (isRefresh){
                        list.clear();
                    }
                    list.addAll(newsItem.data);
                    //这里是刷新界面的
                    complete();
                }
            }

            @Override
            public void onFailure(String message) {
                // complete();
                swipeRefreshLayout.setRefreshing(false);
                mProgress.hide();
            }
        });

    }

    private String parseIds() {
        int size = indexs.size();
        String articleIds = "";
        for (int i = start, j = 0; i < size && j < num; i++, j++, start++) {
            articleIds += indexs.get(i) + ",";
        }
        if (!TextUtils.isEmpty(articleIds)) {
            articleIds = articleIds.substring(0, articleIds.length() - 1);
        }
        return articleIds;
    }

    private void complete(){
        swipeRefreshLayout.setRefreshing(false);
        if (newsAdapterWeakReference.get() != null){
            newsAdapterWeakReference.get().notifyDataSetChanged();
        }
        mProgress.hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
     //   getChildFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        // Caused by: java.lang.IllegalStateException: FragmentManager is already executing transactions
    }
}
