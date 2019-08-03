package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.BaseWebActivity;
import com.hcworld.nbalive.http.bean.match.LiveDetail;
import com.hcworld.nbalive.support.NoDoubleClickListener;
import com.yuyh.easyadapter.abslistview.EasyLVAdapter;
import com.yuyh.easyadapter.abslistview.EasyLVHolder;

import java.util.List;

import static com.hcworld.nbalive.app.AppApplication.getContext;

/**
 * Created by hcw on 2019/1/21.
 * Copyright©hcw.All rights reserved.
 */

public class MatchGraphicAdapter extends EasyLVAdapter<LiveDetail.LiveContent> {

    public MatchGraphicAdapter(List<LiveDetail.LiveContent> mList, Context context, int... layoutIds) {
        super(context, mList, layoutIds);
    }

    @Override
    public void convert(EasyLVHolder viewHolder, int position, LiveDetail.LiveContent item) {
        viewHolder.setText(R.id.tvLiveTime, item.time)
                .setText(R.id.tvLiveTeam, item.teamName)
                .setText(R.id.tvLiveContent, item.content);
        if (!(TextUtils.isEmpty(item.leftGoal) || TextUtils.isEmpty(item.rightGoal))) {
            viewHolder.setVisible(R.id.tvLiveScore, true);
            viewHolder.setText(R.id.tvLiveScore, item.leftGoal + ":" + item.rightGoal);
        } else {
            viewHolder.setVisible(R.id.tvLiveScore, View.INVISIBLE);
        }

        ImageView image = viewHolder.getView(R.id.ivLiveImage);


        if ("图片".equals(item.time) && item.image != null
                && item.image.urls != null && item.image.urls.size() > 0) {
            final List<LiveDetail.UrlsBean> urls = item.image.urls;
            image.setVisibility(View.VISIBLE);
           // image.setController(FrescoUtils.getController(urls.get(0).small, image));
            Glide.with(getContext()).load(urls.get(0).small).into(image);


            if (!TextUtils.isEmpty(urls.get(0).large)) {
                image.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {

                       //这里是查看图片的吧 todo
                     /*   ImagePreViewActivity.start(mContext, new ArrayList<String>() {
                            {add(urls.get(0).large);
                        }}, urls.get(0).large);
                        */


                    }
                });
            }
        } else {
            image.setVisibility(View.GONE);
        }

        ImageView video = viewHolder.getView(R.id.ivLiveVideo);

        if ("视频".equals(item.time) && item.video != null) {
            final LiveDetail.VideoBean videoBean = item.video;
            if (!TextUtils.isEmpty(videoBean.pic_160x90)) {
                video.setVisibility(View.VISIBLE);

            //    video.setController(FrescoUtils.getController(videoBean.pic_160x90, video));
                Glide.with(getContext()).load(videoBean.pic_160x90).into(video);

                video.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        BaseWebActivity.start(mContext, videoBean.playurl, "", true, true);
                    }
                });
            } else {
                video.setVisibility(View.GONE);
            }
        } else {
            video.setVisibility(View.GONE);
        }

        if ("1".equals(item.ctype) && TextUtils.isEmpty(item.time)) {
            viewHolder.setText(R.id.tvLiveTime, "结束");
        }
    }
}
