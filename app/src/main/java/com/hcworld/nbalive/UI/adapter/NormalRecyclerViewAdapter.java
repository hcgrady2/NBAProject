package com.hcworld.nbalive.UI.adapter;

/**
 * Created by hcw on 2019/2/23.
 * Copyright©hcw.All rights reserved.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.MatchLiveActivity;
import com.hcworld.nbalive.http.api.aikanqiu.AikanqiuApi;
import com.hcworld.nbalive.http.bean.live.LiveItemBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by WenTong on 2019/2/22.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> implements View.OnClickListener{
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private int streamType = 1;
    private boolean isUseLehu = false;

    List<String> lehuhostList = new ArrayList<>();


   // private OnItemClickListener mOnItemClickListener = null;

    ButtonItemClick buttonItemClick;
    public void setListenner(ButtonItemClick buttonItemClick){
        this.buttonItemClick = buttonItemClick;
    }


    @Override
    public void onClick(View v) {

        if (buttonItemClick instanceof MatchLiveActivity){
            //不可能有 100 个
            int tag = (int) v.getTag();
            if (tag < 100){
                buttonItemClick.onClick(mList.get((Integer) v.getTag()).getUrl());
            }else {
                initHost(mList.get((Integer) v.getTag() - 100).getRoom_num());
            }
        }
    }

    private List<LiveItemBean> mList;
    public NormalRecyclerViewAdapter(Context context,   List<LiveItemBean> list) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.live_button_item, parent, false);
        NormalTextViewHolder vh = new NormalTextViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        int sourceId = position + 1;
        if (mList.get(position).getName().contains("高清直播")){
            holder.mTextView.setText("高清直播" + sourceId );
            holder.itemView.setTag(position);
        }else if (mList.get(position).getName().contains("CCTV")){
            holder.mTextView.setText("CCTV5");
            holder.itemView.setTag(position);
        }
        //不使用 乐虎直播了，因为高清直播里面就是乐虎直播 ，里面包含了,为了以防万一，可以加一个参数，控制是否使用乐虎
        else {
            holder.mTextView.setText("高清直播" + sourceId);
            holder.itemView.setTag(position+100);
        }
    //    holder.mTextView.setText(mList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        NormalTextViewHolder(View view) {
            super(view);
            //  ButterKnife.inject(this, view);
            mTextView = view.findViewById(R.id.text_view);

        }
    }



    private void initHost(final String room){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.ssmat.com/app/room/")
                .build();
        AikanqiuApi service = retrofit.create(AikanqiuApi.class);

        Call<ResponseBody> call = service.getSreamType(room);
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //System.out.println(response.body().string());



                    String ss = response.body().string().toString();
                    JSONObject demoJson = new JSONObject(ss);
                    String type = demoJson.getJSONObject("data").getJSONObject("anchor").getString("stream_type");
                    streamType = Integer.parseInt(type);
                    initLehu(room,streamType);

                 /*
                    if(response != null && response.body() != null){
                        String ss = response.body().string().toString();
                        JSONObject demoJson = new JSONObject(ss);
                        String type = demoJson.getJSONObject("data").getJSONObject("anchor").getString("stream_type");
                        streamType = Integer.parseInt(type);
                        initLehu(room,streamType);
                    }else {
                        Log.i("whc", "onResponse:  is null in normalRecyclerview adapter");
                    }*/





                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: error"  +e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: json error"  +e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("JsonError", "fail: error"  +t.toString());

            }
        });


    }
    /**
     * 应该保存起来
     */

    private void initLehu(String room, final int type){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://live.lehuzhibo.cc/")
                .build();

        AikanqiuApi service = retrofit.create(AikanqiuApi.class);

        Call<ResponseBody> call = service.getLeHuJson(room);
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    lehuhostList.clear();
                    String ss = response.body().string();
                    JSONObject demoJson = new JSONObject(ss);
                    JSONObject hosts = demoJson.getJSONObject("hosts");

                    for (int i = 1; i <= hosts.length(); i++){
                        JSONObject hostOne = hosts.getJSONObject(i + "");
                        String hostOneStr  = hostOne.getString("hdl");
                        lehuhostList.add(hostOneStr);
                    }

                    JSONObject lines = demoJson.getJSONObject("lines");

                    for (int i = 1; i <=hosts.length(); i++){
                        JSONObject linesOne = lines.getJSONObject(i + "");
                        String linesOneStr = linesOne.getString("hdl");
                        lehuhostList.set(i-1,lehuhostList.get(i-1) + linesOneStr);
                        Log.i("StreamDemo", "onResponse,直播源," + "i" + lehuhostList.get(i-1));

                    }
                    buttonItemClick.onClick(lehuhostList.get(type-1));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: error"  +e.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JsonError", "onResponse: json error"  +e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("JsonError", "fail: error"  +t.toString());
            }

        }
        );


    }

    public interface ButtonItemClick{
        void onClick(String url);
    }


}

