package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.http.match.Matchs;
import com.hcworld.nbalive.support.NoDoubleClickListener;
import com.hcworld.nbalive.utils.ItemAnimHelper;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public class ScheduleAdapter extends EasyRVAdapter<Matchs.MatchsDataBean.MatchesBean> {

    private OnListItemClickListener mOnItemClickListener = null;
    private ItemAnimHelper helper = new ItemAnimHelper();

    public ScheduleAdapter(List<Matchs.MatchsDataBean.MatchesBean> data, Context context, int... layoutId) {
        super(context, data, layoutId);
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final Matchs.MatchsDataBean.MatchesBean item) {
        Matchs.MatchsDataBean.MatchesBean.MatchInfoBean matchInfo = item.matchInfo;

        ImageView ivLeft = viewHolder.getView(R.id.ivLeftTeam);

        Glide.with(mContext).load(matchInfo.leftBadge)
                .into(ivLeft);

        ImageView ivRight = viewHolder.getView(R.id.ivRightTeam);
        Glide.with(mContext).load(matchInfo.rightBadge)
                .into(ivRight);

        String status;
        String aa = matchInfo.quarter;

        if (((matchInfo.quarter.contains("第4节") || matchInfo.quarter.contains("加时")) && !matchInfo.leftGoal.equals(matchInfo.rightGoal))
                && matchInfo.quarterTime.contains("00:00")) {
            status = "已结束";
        } else if ((matchInfo.quarter.equals("") && matchInfo.quarterTime.equals("12:00")) || (matchInfo.quarter.equals("第0节")&& matchInfo.quarterTime.equals("")) ) {
            String[] temp = matchInfo.startTime.split(":");
            //status = matchInfo.startTime;
            if (temp != null && temp.length > 2){
                status = temp[0] + ":"+temp[1];
            }else {
                status = matchInfo.startTime;
            }
        } else {
            status = matchInfo.quarter + " " + matchInfo.quarterTime;
        }
        String broadcasters = "";
        if (matchInfo.broadcasters != null) {
            for (String str : matchInfo.broadcasters) {
                broadcasters += str + "/";
            }
        }
        if (broadcasters.length() > 1) {
            broadcasters = broadcasters.substring(0, broadcasters.length() - 1);
        }

        viewHolder.setText(R.id.tvLeftTeam, matchInfo.leftName)
                .setText(R.id.tvRightTeam, matchInfo.rightName)
                .setText(R.id.tvMatchStatus, status)
                .setText(R.id.tvLeftTeamPoint, matchInfo.leftGoal)
                .setText(R.id.tvRightTeamPoint, matchInfo.rightGoal)
                .setText(R.id.tvMatchDesc, matchInfo.matchDesc)
                .setText(R.id.tvBroadcasters, broadcasters);

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
