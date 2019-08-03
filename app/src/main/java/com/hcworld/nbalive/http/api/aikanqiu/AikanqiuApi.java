package com.hcworld.nbalive.http.api.aikanqiu;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hcw on 2019/1/29.
 * Copyright©hcw.All rights reserved.
 */

public interface AikanqiuApi {

    @GET("app/v110/lives.json")
    Call<ResponseBody> getJson();


    //    //lehu https://www.lehuzhibo.cc/lua-cgi/line-info-v3?id=12354
    //    //       https://live.lehuzhibo.cc/lua-cgi/line-info-v3?id=12354

    //line-info-v3?id=12354
    @GET("lua-cgi/line-info-v3")
    Call<ResponseBody> getLeHuJson(@Query("id") String id );

    @GET("{room}.json")
    Call<ResponseBody> getSreamType(@Path("room") String room);


    //https://app.ssmat.com/app/schedule/all.json

    // https://www.lehuzhibo.cc/app/schedule/basketball.json
    @GET("app/schedule/basketball.json")
    Call<ResponseBody> getSchedule();

}
