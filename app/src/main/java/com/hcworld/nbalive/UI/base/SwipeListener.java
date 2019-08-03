package com.hcworld.nbalive.UI.base;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */


public interface SwipeListener {
    void onScroll(float percent, int px);
    void onEdgeTouch();
    /**
     * Invoke when scroll percent over the threshold for the first time
     */
    void onScrollToClose();
}