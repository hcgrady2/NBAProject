package com.hcworld.nbalive.UI.viewInterface;


import com.hcworld.nbalive.http.bean.match.LiveDetail;

import java.util.List;


public interface MatchLiveView {

    void addList(List<LiveDetail.LiveContent> detail, boolean front);

    void showError(String message);
}
