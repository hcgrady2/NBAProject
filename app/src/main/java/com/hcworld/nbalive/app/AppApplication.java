package com.hcworld.nbalive.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.hcworld.nbalive.utils.Constants;
import com.mob.MobSDK;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import cn.bmob.v3.Bmob;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class AppApplication  extends Application{
    private static Context sContext;
    private boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

          /*获取当前系统的android版本号*/
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.LOLLIPOP)//适配android5.0以下
        {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }


        if (isDebug()){
             setupLeakcanary();
            initRealm();
        }else {

            //umeng
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
            // 调试时，将第三个参数改为true,开发阶段先注释掉
            Bugly.init(this, "", false);

            //开发使用内存泄漏，不使用 bmob
            //bmob
            initBombSDK();
            initRealm();
            //免费短信 mob 平台
            MobSDK.init(this);
        }


    }

    private boolean isDebug(){
        ApplicationInfo info = this.getApplicationInfo();
        isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        return isDebug;
    }
    private void setupLeakcanary(){
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
      //  MultiDex.install(this);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    private void initBombSDK(){
        Bmob.initialize(this, "");
    }

    private void initRealm(){
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name(Constants.REALMNAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }


}
