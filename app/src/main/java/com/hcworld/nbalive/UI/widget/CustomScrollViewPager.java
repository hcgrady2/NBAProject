package com.hcworld.nbalive.UI.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hcw on 2019/1/21.
 * Copyright©hcw.All rights reserved.
 */

/**
 * @author        hcw
 * @time          2019/1/21 20:07
 * @description  自定义 ViewPager 禁止滑动
*/


public class CustomScrollViewPager extends ViewPager {
    public CustomScrollViewPager(@NonNull Context context) {
        super(context);
    }

    //是否可以左右滑动？true 可以，像Android原生ViewPager一样。
    // false 禁止ViewPager左右滑动。
    private boolean scrollable = false;

    public CustomScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return scrollable;
    }


}
