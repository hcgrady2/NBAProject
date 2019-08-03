package com.hcworld.nbalive.UI.fragment;

import android.os.Bundle;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.BaseLazyFragment;

/**
 * Created by hcw on 2019/1/23.
 * Copyright©hcw.All rights reserved.
 */

public class LoginFragment extends BaseLazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_login);
    }
}
