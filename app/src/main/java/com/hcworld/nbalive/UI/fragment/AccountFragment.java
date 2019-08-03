package com.hcworld.nbalive.UI.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hcworld.nbalive.R;
import com.hcworld.nbalive.UI.activity.AboutActivity;
import com.hcworld.nbalive.UI.activity.BaseWebActivity;
import com.hcworld.nbalive.UI.activity.LoginActivity;
import com.hcworld.nbalive.UI.base.StateFragment;
import com.hcworld.nbalive.UI.widget.AccountItemView;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.event.LoginEvent;
import com.hcworld.nbalive.utils.FileUtil;
import com.hcworld.nbalive.utils.ShareUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.hcworld.nbalive.utils.FileUtil.getFileDir;

/**
 * Created by hcw on 2018/12/15.
 * Copyright©hcw.All rights reserved.
 */

public class AccountFragment extends StateFragment {
    @BindView(R.id.main_img)
    ImageView main_img;

    @BindView(R.id.detail_bg)
    ImageView detail_bg;

    @BindView(R.id.account_nba_calender)
    AccountItemView calender;

    @BindView(R.id.account_share)
    AccountItemView share;

    @BindView(R.id.account_about)
    AccountItemView account_about;

    @BindView(R.id.account_feed_back)
    AccountItemView feed_back;

    @BindView(R.id.account_name)
    TextView account_name;

    @BindView(R.id.btn_logout)
    Button btn_logout;



    private boolean isLogin;
    SharedPreferencesUtils helper;

    String UserName = "";

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_account;
    }

    @Override
    public void initValue() {
        super.initValue();
        //判断是否登陆，决定哪个 fragment,然后不用的 fragment，应该想着移除
        helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
        isLogin = helper.getBoolean("IsLogin",false);

        //注册 EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void initView() {
        super.initView();

        //状态栏透明和间距处理

        RequestOptions mRequestOptions = RequestOptions.bitmapTransform(new BlurTransformation(10,3) )
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                //  .skipMemoryCache(true)
                .placeholder(R.drawable.staples)
                .error(R.drawable.staples)
                .fallback(R.drawable.staples);

        Glide.with(getContext())
                .load(R.drawable.staples)
                .apply(mRequestOptions).into(detail_bg);


        RequestOptions mRequestOptions2 = RequestOptions.circleCropTransform()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                //  .skipMemoryCache(true)
                .placeholder(R.drawable.nba_default)
                .error(R.drawable.nba_default)
                .fallback(R.drawable.nba_default);


        Glide.with(getContext())
                .load(R.drawable.ic_launcher)
                .apply(mRequestOptions2)
                .into(main_img);

        if (isLogin){
            if (UserName != "" || !UserName.equals("")){
                account_name.setText(UserName.toString());
            }else {
                UserName = helper.getString("UserName");
                account_name.setText(UserName.toString());
            }

        }else {
            account_name.setText(R.string.login_hint);
        }


    }

    @Override
    public void initEvent() {
        super.initEvent();

        main_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"image",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(this, mdLoginActivity.class);
                //startActivity(intent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AccountFragment.this.getActivity(), main_img, main_img.getTransitionName());
                    startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class), options.toBundle());
                }else {
                    startActivity(new Intent(getContext().getApplicationContext(), LoginActivity.class));
                }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ToastUtils.makeShortText("share",getContext());
                ShareUtils.shareText(getActivity(),  getString(R.string.downloadUrl));
            }
        });

        account_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseWebActivity.start(AccountFragment.this.getContext(), "http://m.china.nba.com/importantdatetoapp/wap.htm", "NBA日历", false, true);

            }
        });

        feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedBack();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.putValues(new SharedPreferencesUtils.ContentValue("IsLogin", false));
                EventBus.getDefault().post(new LoginEvent(1));
                BmobUser.logOut(getContext().getApplicationContext());   //清除缓存用户对象
                BmobUser currentUser = BmobUser.getCurrentUser(getContext().getApplicationContext()); // 现在的currentUser是null了
            }
        });





    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "hcwang1024@163.com", null));
        intent.putExtra(Intent.EXTRA_EMAIL, "hcwang1024@163.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        intent.putExtra(Intent.EXTRA_TEXT, FileUtil.readFile(getFileDir("Log/crash.log")));
        startActivity(Intent.createChooser(intent, "反馈"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    // 处理登录信息的 Event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent loginEvent){
        //   SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
        //   String phoneNum = helper.getString("PhoneNum");
       if (loginEvent.type == 0){

           if (UserName != "" || !UserName.equals("")){
               account_name.setText(UserName.toString());
           }else {
               UserName = helper.getString("UserName");
               account_name.setText(UserName.toString());
           }

       }else if (loginEvent.type == 1){
           account_name.setText(R.string.login_hint);
           isLogin = false;
       }
    }

}

