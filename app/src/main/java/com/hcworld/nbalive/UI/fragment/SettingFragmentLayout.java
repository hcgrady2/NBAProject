package com.hcworld.nbalive.UI.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.bumptech.glide.Glide;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.utils.Constants;
import com.hcworld.nbalive.utils.DataCleanManager;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;

/**
 * Created by hcw on 2019/1/26.
 * Copyright©hcw.All rights reserved.
 */

public class SettingFragmentLayout extends PreferenceFragment {
    public static final String CLEAR_CACHE = "clear_cache";//清除缓存
    public static final String ORIGINAL_NIGHT_MODE = "original_night_mode";//夜间模式
    public static final String CHECK_VERSION = "check_version";//检查版本
    public static final String CHECK_SPLASH = "check_splash";//是否使用启动页
    private Preference clear_cache;
    //    private Preference original_night_mode;
    private Preference check_version,check_splash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initPreference();
        initPreferenceTitle();
        initPreferenceEvent();
    }

    private void initPreferenceEvent() {
        clear_cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                clearCache();
                return true;
            }
        });

  /*      original_splash.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSelect = (Boolean) newValue;
                return true;
            }
        });*/

     /*   original_night_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSelect = (Boolean) newValue;

//                Animator animator = createCheckoutRevealAnimator(getView(), 200, 200, 10, 1000);
//                if (null != animator) {
//                    animator.start();
//                } else {
                activity.onResumeCall();
//                }
                return true;
            }
        });*/

        check_version.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSelect = (Boolean) newValue;
                SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "Setting");
                helper.putValues(new SharedPreferencesUtils.ContentValue("IsCheckUpdate", isSelect));
                return true;
            }
        });

   /*     feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Ts.showShort(getActivity(), R.string.goqq2chat);
                goQQ2Chat();
                return true;
            }
        });*/

        check_splash.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isSelect = (Boolean) newValue;
                SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "Setting");
                helper.putValues(new SharedPreferencesUtils.ContentValue("IsUseSplash", isSelect));
                return true;
            }
        });





    }

    private void initPreference() {
        clear_cache = findPreference(CLEAR_CACHE);
        //   original_night_mode = findPreference(ORIGINAL_NIGHT_MODE);
        check_version = findPreference(CHECK_VERSION);
        check_splash = findPreference(CHECK_SPLASH);
    }

    private void initPreferenceTitle() {
//        String title = FileUtil.getFileSize(MyApp.getAppContext().getCacheDir());
        String title = null;
        try {
            title = DataCleanManager.getCacheSize(AppApplication.getContext().getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
        clear_cache.setSummary(getString(R.string.set_current_cache) + title);
    }



    public void clearCache() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(Constants.DELAYTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataCleanManager.cleanInternalCache(AppApplication.getContext());
                Glide.get(getActivity()).clearDiskCache();//用glide的清除缓存能清掉缓存.但是不想用了
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgress();
                clear_cache.setSummary(getString(R.string.set_current_cache) + "0.00M");
            }
        }.execute();

    }

    private ProgressDialog progressDialog;

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("清除缓存中..");
        }
        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

}
