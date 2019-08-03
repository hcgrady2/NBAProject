package com.hcworld.nbalive.UI.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.BaseSwipeBackCompatActivity;
import com.hcworld.nbalive.UI.presenter.NewsDetailPresenter;
import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.bean.news.NewsDetail;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/11.
 * Copyright©hcw.All rights reserved.
 */

public class NewsDetailActivity extends BaseSwipeBackCompatActivity {

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.news_detail_toolbar)
    Toolbar toolbar;

    @BindView(R.id.news_image)
    ImageView news_image;

   // @BindView(R.id.news_detail)
  //  TextView news_detail;

    @BindView(R.id.news_linearlayout)
    LinearLayout news_linearlayout;

    private LayoutInflater inflate;


    public static final String ARTICLE_ID = "arcId";
    public static final String TITLE = "title";
    public static final String IMAGEURL = "url";
    public static void start(Context context, String title, String arcId,String imagUrl) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NewsDetailActivity.TITLE, title);
        intent.putExtra(NewsDetailActivity.ARTICLE_ID, arcId);
        intent.putExtra(NewsDetailActivity.IMAGEURL,imagUrl);
        context.startActivity(intent);
    }

    NewsDetailPresenter newsDetailPresenter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initViewsAndEvents() {
        inflate = LayoutInflater.from(this);

        //toolbar
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //CollapsingToolbarLayout
        collapsingToolbar.setTitle("详细新闻");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });


        Intent intent = getIntent();
        String title = intent.getStringExtra(TITLE);
        if (!TextUtils.isEmpty(title)) {
            collapsingToolbar.setTitle(title);
        }
        String arcId = intent.getStringExtra(ARTICLE_ID);

        String imagurl = intent.getStringExtra(IMAGEURL);


        //   int height = DimenUtils.getScreenWidth() / 2;
        //  int width = DimenUtils.getScreenWidth();

        RequestOptions requestOptions = new RequestOptions()
                //  .override(width,height)
                .placeholder(R.drawable.nba_default)
                .error(R.drawable.nba_default)
                .fallback(R.drawable.nba_default);


        Glide.with(NewsDetailActivity.this).load(imagurl)
                .apply(requestOptions)
                .into(news_image);


        newsDetailPresenter = new NewsDetailPresenter();

        newsDetailPresenter.getNewsDetail(arcId, new RequestCallback<NewsDetail>() {
            @Override
            public void onSuccess(NewsDetail newsDetail) {
                StringBuilder stringBuilder = new StringBuilder();
                List<Map<String, String>> content = newsDetail.content;
             //   for (Map<String, String> map : content) {

                Map<String, String> map = null;
                for (int i = 0;i < content.size(); i++) {
                    map = content.get(i);
                    if (map == null)
                        return;

                    Set<String> set = map.keySet();
                    if (set == null)
                        return;

                    if (!TextUtils.isEmpty(map.get("text"))) {
                        stringBuilder.append(map.get("text"));
                        TextView tv = (TextView) inflate.inflate(R.layout.textview_news_detail, null);
                        tv.append(map.get("text"));
                        news_linearlayout.addView(tv);
                    }/*else       if (set.contains("img")) {
                        final String url = map.get("img");
                        if (!TextUtils.isEmpty(url)) {
                            ImageView imageView = new ImageView(NewsDetailActivity.this);
                            news_linearlayout.addView(imageView);
                            int width = DimenUtils.getScreenWidth();
                            int height = width * 2 / 3;
                            RequestOptions requestOptions = new RequestOptions()
                                    .override(width,height)
                                    .placeholder(R.drawable.nba_default)
                                    .error(R.drawable.nba_default)
                                    .fallback(R.drawable.nba_default);

                            Glide.with(NewsDetailActivity.this).load(url)
                                    .apply(requestOptions)
                                    .into(imageView);
                            //保存图片
                                  iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Glide.with(NewsDetailActivity.this).load(url).into(mPhotoView);
                                    mInfo = ((PhotoView) v).getInfo();
                                    mBg.startAnimation(in);
                                    mBg.setVisibility(View.VISIBLE);
                                    mParent.setVisibility(View.VISIBLE);
                                    mPhotoView.animaFrom(mInfo);


                                }
                            });
                        }
                    }*/

                }

                //news_detail.setText(stringBuilder);
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

}
