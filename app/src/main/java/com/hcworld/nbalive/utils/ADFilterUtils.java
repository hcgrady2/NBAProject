package com.hcworld.nbalive.utils;

import android.content.Context;
import android.content.res.Resources;

import com.hcworld.nbalive.R;

/**
 * Created by hcw on 2019/1/12.
 * Copyright©hcw.All rights reserved.
 */

public class ADFilterUtils {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.ad);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}