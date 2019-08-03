package com.hcworld.nbalive.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.BaseSwipeBackCompatActivity;
import com.hcworld.nbalive.UI.widget.BrowserLayout;
import com.hcworld.nbalive.utils.ToastUtils;

/**
 * Created by hcw on 2019/1/12.
 * Copyright©hcw.All rights reserved.
 */

public class BaseWebActivity extends BaseSwipeBackCompatActivity {


    public static final String BUNDLE_KEY_URL = "BUNDLE_KEY_URL";
    public static final String BUNDLE_KEY_TITLE = "BUNDLE_KEY_TITLE";
    public static final String BUNDLE_KEY_SHOW_BOTTOM_BAR = "BUNDLE_KEY_SHOW_BOTTOM_BAR";
    public static final String BUNDLE_OVERRIDE = "BUNDLE_OVERRIDE";

    private String mWebUrl = null;
    private String mWebTitle = null;
    private boolean isShowBottomBar = true;
    private boolean isOverrideUrlLoading = true;

    private BrowserLayout mBrowserLayout = null;
    private LinearLayout web_root_linearlayout;

    Toolbar  toolbar;

    public static void start(Context context, String url, String title,
                             boolean isShowBottomBar, boolean isOverrideUrlLoading) {
        Intent intent = new Intent(context, BaseWebActivity.class)
                .putExtra(BUNDLE_KEY_URL, url)
                .putExtra(BUNDLE_KEY_TITLE, title)
                .putExtra(BUNDLE_KEY_SHOW_BOTTOM_BAR, isShowBottomBar)
                .putExtra(BUNDLE_OVERRIDE, isOverrideUrlLoading);
      //  FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent);
    }

/*

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_base_web);

        toolbar = findViewById(R.id.common_toolbar);

        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //CollapsingToolbarLayout
        toolbar.setTitle("详细新闻");

        //事件抽离，todo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        Intent intent = getIntent();
        mWebTitle = intent.getStringExtra(BUNDLE_KEY_TITLE);
        mWebUrl = intent.getStringExtra(BUNDLE_KEY_URL);
        isShowBottomBar = intent.getBooleanExtra(BUNDLE_KEY_SHOW_BOTTOM_BAR, true);
        isOverrideUrlLoading = intent.getBooleanExtra(BUNDLE_OVERRIDE, true);

        if (!TextUtils.isEmpty(mWebTitle)) {
            toolbar.setTitle(mWebTitle);
        } else {
            toolbar.setTitle("详细新闻");
        }

        web_root_linearlayout  = findViewById(R.id.web_root_linearlayout);

        // mBrowserLayout = ButterKnife.findById(this, R.id.common_web_browser_layout);
        mBrowserLayout = new BrowserLayout(AppApplication.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBrowserLayout.setLayoutParams(layoutParams);

        web_root_linearlayout.addView(mBrowserLayout);

        if (!isShowBottomBar) {
            mBrowserLayout.hideBrowserController();
        } else {
            mBrowserLayout.showBrowserController();
        }

        mBrowserLayout.setOverrideUrlLoading(isOverrideUrlLoading);

        mBrowserLayout.setOnReceiveTitleListener(new BrowserLayout.OnReceiveTitleListener() {
            @Override
            public void onReceive(String title) {
                if (TextUtils.isEmpty(mWebTitle)) {
                    setTitle("testtest");
                }
            }

            @Override
            public void onPageFinished() {
                //hideLoadingDialog();
            }
        });
        if (!TextUtils.isEmpty(mWebUrl)) {
            mBrowserLayout.loadUrl(mWebUrl);
        } else {
            ToastUtils.makeShortText("获取地址失败",getApplicationContext());
        }

    }
*/

    @Override
    protected void onPause() {
        if (mBrowserLayout.getWebView() != null) {
            mBrowserLayout.getWebView().onPause();
            mBrowserLayout.getWebView().reload();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
  /*      if (mBrowserLayout.getWebView() != null) {
            mBrowserLayout.getWebView().removeAllViews();
            mBrowserLayout.getWebView().destroy();
        }
*/

        if( mBrowserLayout.getWebView()!=null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mBrowserLayout.getWebView().getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mBrowserLayout.getWebView());
            }

            mBrowserLayout.getWebView().stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mBrowserLayout.getWebView().getSettings().setJavaScriptEnabled(false);
            mBrowserLayout.getWebView().clearHistory();
            mBrowserLayout.getWebView().clearView();
            mBrowserLayout.getWebView().removeAllViews();
            mBrowserLayout.getWebView().destroy();

        }


    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void initViewsAndEvents() {
        toolbar = findViewById(R.id.common_toolbar);

        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //CollapsingToolbarLayout
        toolbar.setTitle("详细新闻");

        //事件抽离，todo
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        Intent intent = getIntent();
        mWebTitle = intent.getStringExtra(BUNDLE_KEY_TITLE);
        mWebUrl = intent.getStringExtra(BUNDLE_KEY_URL);
        isShowBottomBar = intent.getBooleanExtra(BUNDLE_KEY_SHOW_BOTTOM_BAR, true);
        isOverrideUrlLoading = intent.getBooleanExtra(BUNDLE_OVERRIDE, true);

        if (!TextUtils.isEmpty(mWebTitle)) {
            toolbar.setTitle(mWebTitle);
        } else {
            toolbar.setTitle("详细新闻");
        }

        web_root_linearlayout  = findViewById(R.id.web_root_linearlayout);

        // mBrowserLayout = ButterKnife.findById(this, R.id.common_web_browser_layout);
        //避免内存泄漏，以及崩溃
        mBrowserLayout = new BrowserLayout(BaseWebActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBrowserLayout.setLayoutParams(layoutParams);

        web_root_linearlayout.addView(mBrowserLayout);

        if (!isShowBottomBar) {
            mBrowserLayout.hideBrowserController();
        } else {
            mBrowserLayout.showBrowserController();
        }

        mBrowserLayout.setOverrideUrlLoading(isOverrideUrlLoading);

        mBrowserLayout.setOnReceiveTitleListener(new BrowserLayout.OnReceiveTitleListener() {
            @Override
            public void onReceive(String title) {
                if (TextUtils.isEmpty(mWebTitle)) {
                    setTitle("testtest");
                }
            }

            @Override
            public void onPageFinished() {
                //hideLoadingDialog();
            }
        });
        if (!TextUtils.isEmpty(mWebUrl)) {
            mBrowserLayout.loadUrl(mWebUrl);
        } else {
            ToastUtils.makeShortText("获取地址失败",getApplicationContext());
        }
    }

  /*  @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_base_web;
    }
*/

}
