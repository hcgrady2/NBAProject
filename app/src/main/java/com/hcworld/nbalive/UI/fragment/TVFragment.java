package com.hcworld.nbalive.UI.fragment;

/**
 * Created by hcw on 2019/5/17.
 * Copyright©hcw.All rights reserved.
 */

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.adapter.TVRecyclerViewAdapter;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.http.api.tv.TVApi;
import com.hcworld.nbalive.http.bean.tv.TvBean;
import com.hcworld.nbalive.utils.DateUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by hcw on 2019/5/15.
 * Copyright©hcw.All rights reserved.
 */
public class TVFragment extends StateFragment {

    private static final String TAG = "TVFragment";

    @BindView(R.id.tv_recyclerview)
    RecyclerView recyclerView;

    List<TvBean> tvList;


    String tvInfoString = "";

    SharedPreferencesUtils helper;
    TVRecyclerViewAdapter adapter;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_tv;
    }

    @Override
    public void initValue() {

        super.initValue();
        tvList = new ArrayList<>();
        adapter = new TVRecyclerViewAdapter(TVFragment.this.getActivity(),tvList);


        recyclerView.setLayoutManager(new LinearLayoutManager(TVFragment.this.getContext()));
        recyclerView.setLayoutManager(new GridLayoutManager(TVFragment.this.getContext(), 2));
        recyclerView.setAdapter(adapter);


        helper = new SharedPreferencesUtils(AppApplication.getContext(), "tvInfo");
        checkUpdateTvInfo();



    }


    private  void checkUpdateTvInfo(){
        //判断是否使用启动页
        Log.i(TAG, "checkUpdateTvInfo: 查询 sp");
        String date = helper.getString("checkDate");

        String currentDate =   DateUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");

        if (date!= null && !date.equals(currentDate)){
            //这个时候，就要去检查更新配置文件了
            Log.i(TAG, "checkUpdateTvInfo: ");
            helper.putValues(new SharedPreferencesUtils.ContentValue("checkDate", currentDate));
            updateTV();
        }else {
            String tvInfoString = helper.getString("tvInfoString");
            Log.i(TAG, "checkUpdateTvInfo:找到 ss");
            if(tvInfoString != null && !tvInfoString.equals("")){

                try {

                    if(tvInfoString != null && tvInfoString != ""){
                        JSONObject demoJson = new JSONObject(tvInfoString);
                        JSONArray jsonArray = demoJson.getJSONArray("channels");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject matchItem = jsonArray.getJSONObject(i);
                            String name = matchItem.getString("name");

                            String url = matchItem.getString("url");

                            Log.i(TAG, "sp 找到: 名字:" + name);

                            TvBean tvBean = new TvBean();
                            tvBean.setTvName(name);
                            tvBean.setChanel(url);
                            tvList.add(tvBean);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }else {
                updateTV();
            }

        }

    }


    private void updateTV(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .build();

        TVApi service = retrofit.create(TVApi.class);

        Call<ResponseBody> call = service.getTv();
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    String ss = response.body().string();

                    if(ss != null && !ss.equals("")){
                        helper.putValues(new SharedPreferencesUtils.ContentValue("tvInfoString", ss));
                    }

                    JSONObject demoJson = new JSONObject(ss);
                    JSONArray jsonArray = demoJson.getJSONArray("channels");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject matchItem = jsonArray.getJSONObject(i);
                        String name = matchItem.getString("name");

                        String url = matchItem.getString("url");

                        Log.i(TAG, "onResponse: 名字:" + name);
                        Log.i(TAG, "onResponse: url:" + url);

                        TvBean tvBean = new TvBean();
                        tvBean.setTvName(name);
                        tvBean.setChanel(url);
                        tvList.add(tvBean);

                    }

                    adapter.notifyDataSetChanged();


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("TVFragment", "fail: error"  +t.toString());

            }
        });

    }


}
