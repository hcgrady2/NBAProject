package com.hcworld.nbalive.http.api.tv;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hcw on 2019/5/15.
 * Copyright©hcw.All rights reserved.
 */
public interface TVApi {

    @GET("hcgrady2/Apk/master/tvTest.json")
    Call<ResponseBody> getTv();

}
