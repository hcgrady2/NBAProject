package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.http.bean.data.StatsRank;
import com.hcworld.nbalive.support.NoDoubleClickListener;
import com.hcworld.nbalive.utils.ItemAnimHelper;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * Created by hcw on 2019/1/9.
 * Copyright©hcw.All rights reserved.
 */

public class StatsRankAdapter extends EasyRVAdapter<StatsRank.RankItem> {

    private OnListItemClickListener mOnItemClickListener = null;
    private ItemAnimHelper helper = new ItemAnimHelper();

    public StatsRankAdapter(List<StatsRank.RankItem> data, Context context, int... layoutId) {
        super(context, data, layoutId);
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final StatsRank.RankItem item) {
        viewHolder.setText(R.id.tvRank, "NO." + item.serial + "")
                .setText(R.id.tvData, item.value)
                .setText(R.id.tvName, item.playerName);
    //    SimpleDraweeView ivHead = viewHolder.getView(R.id.ivHead);
      //  ivHead.setController(FrescoUtils.getController(item.pln, ivHead));
        ImageView ivHead = viewHolder.getView(R.id.ivHead);
        Glide.with(mContext).load(item.playerIcon)
                .into(ivHead);

        ImageView ivTeam = viewHolder.getView(R.id.ivTeam);
        //ivTeam.setController(FrescoUtils.getController(item.teamIcon, ivTeam));
        Glide.with(mContext).load(item.teamIcon)
                .into(ivTeam);

        viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });
        helper.showItemAnim(viewHolder.getItemView(), position);
    }
}
