package com.hcworld.nbalive.UI.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.TVActivity;
import com.hcworld.nbalive.http.bean.tv.TvBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcw on 2019/5/17.
 * Copyright©hcw.All rights reserved.
 */
public class TVRecyclerViewAdapter extends RecyclerView.Adapter<TVRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<TvBean> tvBeanList;
    Intent intent;
    public TVRecyclerViewAdapter(Context context,List<TvBean> list) {
        Log.i("'TVActiviy'", "TVRecyclerViewAdapter: ");
        tvBeanList = list;
        mContext = context;
        intent = new Intent(mContext, TVActivity.class);
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.fragment_tv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(tvBeanList.get(position).getTvName());
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return tvBeanList == null ? 0 :tvBeanList.size();
    }


    public  class NormalTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view)
        TextView mTextView;

        NormalTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url =  tvBeanList.get((Integer) v.getTag()).getChanel();
                    String name =  tvBeanList.get((Integer) v.getTag()).getTvName();

                    intent.putExtra("name",name);
                    intent.putExtra("url",url);
                    mContext.startActivity(intent);

                }
            });
        }
    }
}



