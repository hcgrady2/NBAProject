package com.hcworld.nbalive.UI.view;

import com.hcworld.nbalive.http.bean.match.LiveDetail;

import java.util.List;

/**
 * Created by hcw on 2019/1/20.
 * Copyright©hcw.All rights reserved.
 */

public interface MatchGraphicView {

    void addList(List<LiveDetail.LiveContent> detail, boolean front);

    void showError(String message);
}

