package com.hcworld.nbalive.UI.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.fragment.AccountFragment;
import com.hcworld.nbalive.UI.fragment.DataFragment;
import com.hcworld.nbalive.UI.fragment.LiveFragment;
import com.hcworld.nbalive.UI.fragment.MenuListFragment;
import com.hcworld.nbalive.UI.fragment.NewsFragment;
import com.hcworld.nbalive.UI.fragment.SettingFragment;
import com.hcworld.nbalive.UI.fragment.TVFragment;
import com.hcworld.nbalive.UI.fragment.TeamPerformanceFragment;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.http.bean.app.AppUpdate;
import com.hcworld.nbalive.http.utils.UpdateAppHttpUtil;
import com.hcworld.nbalive.utils.AppUtils;
import com.hcworld.nbalive.utils.DateUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;
import com.hcworld.nbalive.utils.ToastUtils;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.listener.ExceptionHandler;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivityDemo";

    private String mUpdateUrl = "https://raw.githubusercontent.com/hcgrady2/Apk/master/json.txt";
    private long mPressedTime = 0;

    @BindView(R.id.drawerlayout)
    FlowingDrawer mDrawer;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private NewsFragment newsFragment;
    private LiveFragment liveFragment;
    private TeamPerformanceFragment teamPerformanceFragment;
    private DataFragment dataFragment;
    private AccountFragment accountFragment;
    private SettingFragment settingFragment;
    private TVFragment tvFragment;



    private Fragment currentFragment;
    private String currentDate;
    private boolean isFirstOfDay = false;

    private WeakReference<Context> contextWeakReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        contextWeakReference = new WeakReference<Context>(this);
        getPermission();
        checkUpdate();

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        if(savedInstanceState == null)
        {
            newsFragment = new NewsFragment();
            //设置第一个为 news fragment
            getSupportFragmentManager().beginTransaction().add(R.id.id_container_content, newsFragment).commit();
        }

        toolbar.setTitle(R.string.menu_news);
        currentFragment = newsFragment;
        setupToolbar();
        setupMenu();

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {

            }
        });

    }

    protected void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }
    }

    private void changeMenu(){
        Log.i("WelfareDemo", "changeMenu: ");
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = new MenuListFragment();
        fm.beginTransaction().replace(R.id.id_container_menu,mMenuFragment).commit();

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            long mNowTime = System.currentTimeMillis();
            if((mNowTime - mPressedTime) > 2000){
             //   Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                ToastUtils.makeShortText("再按一次退出程序",getApplicationContext());

                mPressedTime = mNowTime;
            } else{
                this.finish();
            }
        }
    }

    /**
     * 这里不应该是每次检查，应该是每天检查，开启个新的线程
     */
    private void checkUpdate(){

        //判断是否使用启动页
        SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "Setting");
        boolean checkUpdate = helper.getBoolean("IsCheckUpdate",true);

        String date = helper.getString("checkDate");

        currentDate =   DateUtils.format(System.currentTimeMillis(), "yyyy-MM-dd");

        if (date!= null && !date.equals(currentDate)){
            //这个时候，就要去检查更新配置文件了
            isFirstOfDay = true;
            helper.putValues(new SharedPreferencesUtils.ContentValue("checkDate", currentDate));
            updateSettingInfo();
        }

        final String versionName =  AppUtils.getVersionName(getApplicationContext());
        //检查更新是这个，其他配置文件的检查更新，用另一个数据表
        if (checkUpdate && isFirstOfDay){
            BmobQuery<AppUpdate> query = new BmobQuery<AppUpdate>();
            query.getObject(HomeActivity.this, "SQji1113", new GetListener<AppUpdate>() {
                @Override
                public void onSuccess(AppUpdate appUpdate) {
                    if (appUpdate != null && appUpdate.getIsUpdate().equals("1") && !appUpdate.getVersionName().equals(versionName)){
                            updateApp();
                    }
                }
                @Override
                public void onFailure(int i, String s) {
                }
            });
        }
    }


    /**
     * 检查配置文件
     */
    private void updateSettingInfo(){

    }

    public void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(HomeActivity.this);
        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }
                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (!aBoolean) {
                                    ToastUtils.makeShortText("您没有存储权限",getApplicationContext());
                                }

                            }
                            @Override
                            public void onError(Throwable e) {

                            }
                            @Override
                            public void onComplete() {


                            }
                        }

                );

    }

    /**
     * 最简方式
     */
    public void updateApp() {
        new UpdateAppManager
                .Builder()
                .setActivity((Activity) contextWeakReference.get())
                .setTopPic(R.mipmap.top_3)
                //更新地址
                .setUpdateUrl(mUpdateUrl)
                .setTargetPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "NBABox")
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();
    }

    public void setContentLayout(int id ){
        switch (id){
            case R.id.menu_news:
                if (null == newsFragment){
                    newsFragment = new NewsFragment();
                }
                switchFragment(newsFragment);
                toolbar.setTitle(R.string.menu_news);

                break;
            case R.id.menu_live:
                if (null == liveFragment){
                    liveFragment = new LiveFragment();
                }
                switchFragment(liveFragment);
                //getSupportFragmentManager().beginTransaction().replace(R.id.id_container_content,liveFragment).addToBackStack(null).commit();
                toolbar.setTitle(R.string.menu_live);
                break;
            case R.id.menu_performance:
                if (null == teamPerformanceFragment){
                    teamPerformanceFragment = new TeamPerformanceFragment();
                }
                switchFragment(teamPerformanceFragment);
               // getSupportFragmentManager().beginTransaction().replace(R.id.id_container_content,teamPerformanceFragment).addToBackStack(null).commit();
                toolbar.setTitle(R.string.menu_performance);
                break;
            case R.id.menu_data:
                if (null == dataFragment){
                    dataFragment = new DataFragment();
                }
                switchFragment(dataFragment);
               // getSupportFragmentManager().beginTransaction().replace(R.id.id_container_content,dataFragment).addToBackStack(null).commit();
                toolbar.setTitle(R.string.menu_data);
                break;

            case R.id.menu_account:
              //  Toast.makeText(getApplicationContext(),"acc" ,Toast.LENGTH_SHORT).show();
                if (null == accountFragment){
                    accountFragment = new AccountFragment();
                }
                switchFragment(accountFragment);
                // getSupportFragmentManager().beginTransaction().replace(R.id.id_container_content,dataFragment).addToBackStack(null).commit();
                toolbar.setTitle("个人中心");
                break;

            case R.id.menu_settings:
              //  Toast.makeText(getApplicationContext(),"setting" ,Toast.LENGTH_SHORT).show();
                if (null == settingFragment){
                    settingFragment = new SettingFragment();
                }
                switchFragment(settingFragment);
                toolbar.setTitle("设置");
                break;



            case  R.id.menu_game:
               BaseWebActivity.start(HomeActivity.this.getApplicationContext(), "https://chvin.github.io/react-tetris/?lan=en", "俄罗斯方块", false, true);
                break;

            case  R.id.menu_tv:
                if (null == tvFragment){
                    tvFragment = new TVFragment();
                }
                switchFragment(tvFragment);
                toolbar.setTitle("电视");
                break;


            default:
                    break;
        }
        mDrawer.closeMenu();
    }


    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!targetFragment.isAdded()) {

            transaction.addToBackStack(null);

            transaction
                    .hide(currentFragment)
                    .add(R.id.id_container_content, targetFragment)
                    .commit();

        } else {

            transaction.addToBackStack(null);


            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
