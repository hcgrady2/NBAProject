package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.http.match.MatchStat;
import com.yuyh.easyadapter.abslistview.EasyLVAdapter;
import com.yuyh.easyadapter.abslistview.EasyLVHolder;

import java.util.List;

import static com.hcworld.nbalive.app.AppApplication.getContext;

/**
 * Created by hcw on 2019/1/21.
 * Copyright©hcw.All rights reserved.
 */

public class MatchLMaxPlayerdapter extends EasyLVAdapter<MatchStat.MaxPlayers> {

    public MatchLMaxPlayerdapter(List<MatchStat.MaxPlayers> mList, Context context, int... layoutIds) {
        super(context, mList, layoutIds);
    }

    @Override
    public void convert(EasyLVHolder viewHolder, int position, MatchStat.MaxPlayers item) {
        viewHolder.setText(R.id.tvLeftPlayerName, item.leftPlayer.name)
                .setText(R.id.tvLeftPlayerType, item.leftPlayer.position + "  #" + item.rightPlayer.jerseyNum)
                .setText(R.id.tvRightPlayerName, item.rightPlayer.name)
                .setText(R.id.tvRightPlayerType, item.rightPlayer.position + "  #" + item.rightPlayer.jerseyNum)
                .setText(R.id.tvType, item.text);

        ImageView ivLeft = viewHolder.getView(R.id.ivLeftPlayerIcon);

      //  ivLeft.setController(FrescoUtils.getController(item.leftPlayer.icon, ivLeft));

        Glide.with(getContext()).load(item.leftPlayer.icon).into(ivLeft);

        ImageView ivRight = viewHolder.getView(R.id.ivRightPlayerIcon);
        Glide.with(getContext()).load(item.rightPlayer.icon).into(ivRight);

       // ivRight.setController(FrescoUtils.getController(item.rightPlayer.icon, ivRight));
    }
}
