package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.http.bean.news.NewsItem;
import com.hcworld.nbalive.utils.DimenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List data;
    private List<NewsItem.NewsItemBean> list = new ArrayList<>();

    RequestOptions requestOptions;
    private int sourceWidth;

    private OnListItemClickListener<NewsItem.NewsItemBean> mOnItemClickListener = null;

    public void setOnItemClickListener(OnListItemClickListener<NewsItem.NewsItemBean> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public NewsAdapter(Context context, List<NewsItem.NewsItemBean> itemList) {
        this.context = context;
        this.list = itemList;

        //int height = DimenUtils.getScreenWidth() / 2;
        sourceWidth = DimenUtils.getScreenWidth();

           requestOptions = new RequestOptions().fitCenter()
              //  .override(width,height)
                   .skipMemoryCache(true)
                   .placeholder(R.drawable.nba_default)
                    .error(R.drawable.nba_default)
                    .fallback(R.drawable.nba_default);
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_fragment_item, parent, false);
        return new NewsAdapter.ItemViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NewsAdapter.ItemViewHolder) {
            if (list != null && list.size() > 0){

                Glide.with(context)
                        .asBitmap()
                        .load(list.get(position).imgurl)
                        .apply(requestOptions)
                      //  .into(((ItemViewHolder)holder).new_image);

                     .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                         @Override
                         public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                 //原始图片宽高
                                 int imageWidth = resource.getWidth();
                                 int imageHeight = resource.getHeight();
                                 //按比例收缩图片
                                 float ratio=(float) ((imageWidth*1.0)/(sourceWidth*1.0));
                                 int height=(int) (imageHeight*1.0/ratio);
                                 ViewGroup.LayoutParams params = ((ItemViewHolder)holder).new_image.getLayoutParams();
                                 params.width=sourceWidth;
                                 params.height=height;
                                ((ItemViewHolder)holder).new_image.setImageBitmap(resource);

                         }
                     });

            ((NewsAdapter.ItemViewHolder) holder).news_title.setText(list.get(position).title.toString());
            }

            ((NewsAdapter.ItemViewHolder) holder).new_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick( ((ItemViewHolder) holder).new_image, position,list.get(position));
                }
            });

        }
    }



    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView news_title ;
        ImageView new_image;

        public ItemViewHolder(View view) {
            super(view);
            news_title = view.findViewById(R.id.tvBannerTitle);
            new_image = view.findViewById(R.id.ivBannerImg);
        }
    }

}