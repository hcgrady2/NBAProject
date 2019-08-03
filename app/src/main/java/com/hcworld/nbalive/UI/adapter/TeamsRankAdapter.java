package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.widget.OnListItemClickListener;
import com.hcworld.nbalive.http.bean.team.TeamsRank;
import com.hcworld.nbalive.support.NoDoubleClickListener;
import com.hcworld.nbalive.utils.ItemAnimHelper;
import com.yuyh.easyadapter.recyclerview.EasyRVAdapter;
import com.yuyh.easyadapter.recyclerview.EasyRVHolder;

import java.util.List;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */
public class TeamsRankAdapter extends EasyRVAdapter<TeamsRank.TeamBean> {

    private OnListItemClickListener mOnItemClickListener = null;
    private ItemAnimHelper helper = new ItemAnimHelper();

    public TeamsRankAdapter(List<TeamsRank.TeamBean> data, Context context, int... layoutId) {
        super(context, data, layoutId);
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    protected void onBindData(final EasyRVHolder viewHolder, final int position, final TeamsRank.TeamBean item) {
        if (item.type == 0) {
          //  SimpleDraweeView iv = viewHolder.getView(R.id.team_icon);
            //iv.setController(FrescoUtils.getController(item.badge, iv));
            ImageView iv = viewHolder.getView(R.id.team_icon);
            Glide.with(mContext.getApplicationContext()).load(item.badge).into(iv);
            if (position < 16){
                viewHolder.setText(R.id.team_no,""+position + ".");
            }else if (position >16){
                viewHolder.setText(R.id.team_no,""+(position-16) + ".");
            }

            viewHolder.setText(R.id.team_name, item.name)
                    .setText(R.id.win, item.win + "")
                    .setText(R.id.lose, item.lose + "")
                    .setText(R.id.win_percent, item.rate)
                    .setText(R.id.difference, item.difference);
        } else {
            viewHolder.setText(R.id.team_name, item.name);
        }

        viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null && item.type == 0)
                    mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });

        helper.showItemAnim(viewHolder.getItemView(), position);
    }

    @Override
    public int getLayoutIndex(int position, TeamsRank.TeamBean item) {
        if (item.type == 0)
            return 0;
        else
            return 1;
    }
}
