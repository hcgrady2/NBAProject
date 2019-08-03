package com.hcworld.nbalive.UI.widget;

import android.view.View;

/**
 * Created by hcw on 2019/1/6.
 * Copyright©hcw.All rights reserved.
 */

public interface OnListItemClickListener<T> {
    void onItemClick(View view, int position, T data);
}