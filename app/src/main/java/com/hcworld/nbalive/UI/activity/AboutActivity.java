package com.hcworld.nbalive.UI.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.base.BaseSwipeBackCompatActivity;
import com.hcworld.nbalive.utils.AppUtils;

import butterknife.BindView;

/**
 * Created by hcw on 2019/1/26.
 * Copyright©hcw.All rights reserved.
 */

public class AboutActivity extends BaseSwipeBackCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.llayout_about)
    LinearLayout llayout_about;
    @BindView(R.id.txt_about_app)
    TextView txt_about_app;

    @BindView(R.id.versionName)
    TextView versionName;

    private Toast toast;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }



    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about;
    }



    @Override
    protected void initViewsAndEvents() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        String versionTitle = getString(R.string.version) + AppUtils.getVersionName(getApplicationContext());
        versionName.setText(versionTitle);



        //用码云，或者其他的比如博客之类的，试一下
/*        String url =  "http://gitee.com/hcgrady/Apk/raw/master/NBABox.apk";
        VersionUpdateConfig.getInstance()//获取配置实例
                .setContext(AboutActivity.this)//设置上下文
                .setDownLoadURL(url)//设置文件下载链接
                .setNewVersion("1.0.1")//设置即将下载的APK的版本号,避免重复下载
              //  .setFileSavePath(savePath)//设置文件保存路径（可不设置）
                .setNotificationIconRes(R.drawable.ic_launcher)//设置通知图标
                .setNotificationSmallIconRes(R.drawable.ic_launcher)//设置通知小图标
                .setNotificationTitle("版本更新")//设置通知标题
                .startDownLoad();//开始下载*/


      /*  UpdateAppUtils.from(this)
                .serverVersionCode(2)  //服务器versionCode
                .serverVersionName("2.0") //服务器versionName
                .apkPath(url) //最新apk下载地址
                .update();*/



    }
}
