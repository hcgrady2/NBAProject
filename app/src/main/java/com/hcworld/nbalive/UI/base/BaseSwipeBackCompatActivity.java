package com.hcworld.nbalive.UI.base;

import android.os.Bundle;

import com.hcworld.nbalive.UI.activity.HomeActivity;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public abstract class BaseSwipeBackCompatActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwipeBackHelper.onCreate(this);
        if (this.getClass().equals( HomeActivity.class)){
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        }else {
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true);
        }

        SwipeBackHelper.getCurrentPage(this)
                .setSwipeEdgePercent(0.2f)//0.2 mean left 20% of screen can touch to begin swipe.
                .setSwipeSensitivity(1)//sensitiveness of the gesture。0:slow  1:sensitive
/*
                .setScrimColor(Color.BLUE)//color of Scrim below the activity
*/
                .setClosePercent(0.8f)//close activity when swipe over this
                .setSwipeRelateEnable(true)//if should move together with the following Activity
                .setSwipeRelateOffset(500)//the Offset of following Activity when setSwipeRelateEnable(true)
                .setDisallowInterceptTouchEvent(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

}
