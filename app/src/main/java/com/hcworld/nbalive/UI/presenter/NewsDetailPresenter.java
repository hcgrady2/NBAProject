package com.hcworld.nbalive.UI.presenter;

import com.hcworld.nbalive.http.api.RequestCallback;
import com.hcworld.nbalive.http.api.tencent.TencentService;
import com.hcworld.nbalive.http.bean.news.NewsDetail;
import com.hcworld.nbalive.utils.Constant;

/**
 * Created by hcw on 2019/1/11.
 * Copyright©hcw.All rights reserved.
 */

public class NewsDetailPresenter {
    public void getNewsDetail(String arcId, RequestCallback<NewsDetail> callback) {
        TencentService.getNewsDetail(Constant.NewsType.BANNER, arcId, false, callback);
    }
}
