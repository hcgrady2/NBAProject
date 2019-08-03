package com.hcworld.nbalive.UI.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.app.AppApplication;
import com.hcworld.nbalive.event.LoginEvent;
import com.hcworld.nbalive.http.bean.user.UserBean;
import com.hcworld.nbalive.utils.Base64Utils;
import com.hcworld.nbalive.utils.ProgressBarUtils;
import com.hcworld.nbalive.utils.SharedPreferencesUtils;
import com.hcworld.nbalive.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hcw on 2019/1/25.
 * Copyright©hcw.All rights reserved.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.login_edname)
    EditText account;
    @BindView(R.id.login_edpwd)
    EditText password;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.login_btn)
    AppCompatButton comfirm;

    @BindView(R.id.login_account_textinput)
    TextInputLayout login_account;

    @BindView(R.id.login_pwd_textinput)
    TextInputLayout login_pwd;

    @BindView(R.id.login_tvfogot)
    TextView login_tvfogot;


    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGET = 1;

    private ProgressBarUtils mProgress;

    private Context mContext;
    private String preAcount = "123";
    private String prePassword = "123";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_login_main);
        ButterKnife.bind(this);
        fab.setOnClickListener(this);
        comfirm.setOnClickListener(this);
        login_tvfogot.setOnClickListener(this);
        initNetModel();
        mProgress = new ProgressBarUtils(LoginActivity.this);
        mContext = this;
        //bugly 测试
        //CrashReport.testJavaCrash();

    }

    private void initNetModel() {

   /*     OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Config.SERVER_URL)
                .build();
        mNetModel = retrofit.create(LoginNet.class);*/

    }

    @SuppressLint("RestrictedApi")
    @TargetApi(21)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setEnterTransition(null);
                    getWindow().setExitTransition(null);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    //startActivity(new Intent(mdLoginActivity.this, RegisterActivity.class), options.toBundle());
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP,options.toBundle());

                } else {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    // startActivity(new Intent(mdLoginActivity.this, RegisterActivity.class));
                }
                break;
            case R.id.login_btn:
                /** update at 2017-07-06 */
                login();
                break;
            case R.id.login_tvfogot:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setEnterTransition(null);
                    getWindow().setExitTransition(null);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    //startActivity(new Intent(mdLoginActivity.this, RegisterActivity.class), options.toBundle());
                    Intent intent = new Intent(getApplicationContext(), ForgetPwdActivity.class);
                    startActivityForResult(intent, REQUEST_FORGET,options.toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), ForgetPwdActivity.class);
                    startActivityForResult(intent, REQUEST_FORGET);

                    // startActivity(new Intent(mdLoginActivity.this, RegisterActivity.class));
                }
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void login() {
        mProgress.show();
        if (!validate()) {
            onLoginFailed();
            return;
        }
        //loginTest();
        bmobLogin();
    }


    /**
     * bmob 提供登陆和注册，可以使用
     */
    private void bmobLogin(){

        final String username = account.getText().toString();
        final String passwordStr = password.getText().toString();

        // 使用BmobSDK提供的登录功能
        BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(passwordStr);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {

                SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
                helper.putValues(

                        new SharedPreferencesUtils.ContentValue("IsLogin", true),
                        new SharedPreferencesUtils.ContentValue("UserName", username),
                        new SharedPreferencesUtils.ContentValue("Password", Base64Utils.encryptBASE64(passwordStr)));

                         /*   new SharedPreferencesUtils.ContentValue("Avatar", userBean.getAvatar()),
                            new SharedPreferencesUtils.ContentValue("PhoneNum", userBean.getPhoneNum()),
                            new SharedPreferencesUtils.ContentValue("Bio", userBean.getBio()),
                            new SharedPreferencesUtils.ContentValue("Local", userBean.getLocal()),
                            new SharedPreferencesUtils.ContentValue("UserName", userBean.getName()),
                            new SharedPreferencesUtils.ContentValue("Scores", userBean.getScores()),
                            new SharedPreferencesUtils.ContentValue("Flag", userBean.getFlag()),
                            new SharedPreferencesUtils.ContentValue("CreatedTime", userBean.getCreatedAt()),
                            new SharedPreferencesUtils.ContentValue("ObjectId", userBean.getObjectId()),
                            new SharedPreferencesUtils.ContentValue("Email", userBean.getEmail()));*/


                //    finish();
                onLoginSuccess();

            }

            @Override
            public void onFailure(int i, String s) {
                onLoginFailed();
            }
        });


    }

    private void loginTest(){

        final String accountStr = account.getText().toString();
        final String passwordStr = password.getText().toString();

        BmobQuery<UserBean> query_phone = new BmobQuery<UserBean>();
        query_phone.addWhereEqualTo("PhoneNum", accountStr);
      //  BmobQuery<UserBean> query_pwd = new BmobQuery<UserBean>();
        query_phone.addWhereEqualTo("Pwd", passwordStr);
        //最后组装完整的and条件
        List<BmobQuery<UserBean>> queries = new ArrayList<BmobQuery<UserBean>>();
        queries.add(query_phone);
        queries.add(query_phone);
        //查询符合整个and条件的人
        BmobQuery<UserBean> query = new BmobQuery<UserBean>();
        query.and(queries);
        query.findObjects(this, new FindListener<UserBean>() {
            @Override
            public void onSuccess(List<UserBean> object) {
                // TODO Auto-generated method stub
                if (object != null && object.size() > 0){
                    //查询到记录，更新数据库，最好加密
                    //  ToastUtils.showShort(LoginActivity.this,R.string.LoginSuccess);
                    //EventBus 发布消息
                    //然后跳转到 AccountFragment，更新 fragment
                    //存到 sb
                    //存到 sb
                    SharedPreferencesUtils helper = new SharedPreferencesUtils(AppApplication.getContext(), "UserBean");
                    helper.putValues(

                            new SharedPreferencesUtils.ContentValue("IsLogin", true),
                            new SharedPreferencesUtils.ContentValue("UserName", accountStr),
                            new SharedPreferencesUtils.ContentValue("Password", Base64Utils.encryptBASE64(passwordStr)));

                         /*   new SharedPreferencesUtils.ContentValue("Avatar", userBean.getAvatar()),
                            new SharedPreferencesUtils.ContentValue("PhoneNum", userBean.getPhoneNum()),
                            new SharedPreferencesUtils.ContentValue("Bio", userBean.getBio()),
                            new SharedPreferencesUtils.ContentValue("Local", userBean.getLocal()),
                            new SharedPreferencesUtils.ContentValue("UserName", userBean.getName()),
                            new SharedPreferencesUtils.ContentValue("Scores", userBean.getScores()),
                            new SharedPreferencesUtils.ContentValue("Flag", userBean.getFlag()),
                            new SharedPreferencesUtils.ContentValue("CreatedTime", userBean.getCreatedAt()),
                            new SharedPreferencesUtils.ContentValue("ObjectId", userBean.getObjectId()),
                            new SharedPreferencesUtils.ContentValue("Email", userBean.getEmail()));*/

                    //    finish();
                    onLoginSuccess();
                }else {
                    ToastUtils.showShort(LoginActivity.this.getApplicationContext(),R.string.LoginFailed);
                }
            }
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(LoginActivity.this.getApplicationContext(),R.string.LoginFailed);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                //  this.finish();
            }

        }else if (requestCode == REQUEST_FORGET){
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"修改密码成功！",Toast.LENGTH_SHORT).show();
                //  this.finish();
            }
        }
    }


    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_LONG).show();
        EventBus.getDefault().post(new LoginEvent(0));
        mProgress.hide();
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        mProgress.hide();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public boolean validate() {
        boolean valid = true;

        String accountStr = account.getText().toString();
        String passwordStr = password.getText().toString();

        //     text_input_layout_1.setHint("Title");

        if (accountStr.isEmpty() || !Patterns.PHONE.matcher(accountStr).matches() || accountStr.length() != 11 ) {
            //     _emailText.setError
            //_emailText.setError("enter a valid email address");
            login_account.setError("手机号格式错误！");
            valid = false;
        } else {
            login_account.setError(null);
        }

        if (passwordStr.isEmpty() || passwordStr.length() < 4 || passwordStr.length() > 10) {

            login_pwd.setError("密码格式错误");
            valid = false;
        } else {
            login_pwd.setError(null);
        }

        return valid;
    }

        @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        //   moveTaskToBack(true);
        LoginActivity.super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
