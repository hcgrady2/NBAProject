package com.hcworld.nbalive.UI.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hcworld.nbalive.R;
import com.hcworld.nbalive.utils.CountDownTimerUtils;
import com.hcworld.nbalive.utils.RegexUtils;
import com.hcworld.nbalive.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by hcw on 2019/2/3.
 * Copyright©hcw.All rights reserved.
 */

public class ForgetPwdActivity extends AppCompatActivity  implements View.OnClickListener{

    @BindView(R.id.forget_account_text_input)
    TextInputLayout forget_account_text_input;

    @BindView(R.id.forget_account)
    EditText forget_account;

    @BindView(R.id.forget_verification_input)
    TextInputLayout forget_verification_input;

    @BindView(R.id.forget_verification)
    EditText forget_verification;

    @BindView(R.id.forget_tv_get_code)
    TextView forget_tv_get_code;


    @BindView(R.id.forget_pwd_text_input)
    TextInputLayout forget_pwd_text_input;

    @BindView(R.id.forget_edpwd)
    EditText forget_edpwd;


    @BindView(R.id.forget_repwd_text_input)
    TextInputLayout forget_repwd_text_input;

    @BindView(R.id.forget_edpwd_again)
    EditText forget_edpwd_again;


    @BindView(R.id.forget_confirm_btn_btn)
    AppCompatButton  forget_confirm_btn_btn;

    @BindView(R.id.forget_fab)
    FloatingActionButton forget_fab;

    @BindView(R.id.forget_cardview)
    CardView cv;


    private String accountEditStr;
    private String pwdEditStr;
    private String veriCodeEditStr;


    WeakReference<CardView> cardViewWeakReference;;
    CountDownTimerUtils mCountDownTimerUtils;

    //验证码验证通过在注册
    //本地验证->验证码验证->注册

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // int result = msg.arg2;
            //int event = msg.arg1;
            //Object data = msg.obj;
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Log.i("VerificationCode", "send requeset success" );
                } else {
                    // TODO 处理错误的结果
                    Log.i("VerificationCode", "send request error" );
                    ((Throwable) data).printStackTrace();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证码验证通过的结果
                    Log.i("VerificationCode", "validate success" );
                    accountEditStr  = forget_account.getText().toString();
                    pwdEditStr  = forget_edpwd.getText().toString();
                    hasRecord(accountEditStr,pwdEditStr);

                } else {
                    // TODO 处理错误的结果
                    Log.i("VerificationCode", "validate error" );
                    ToastUtils.showShort(ForgetPwdActivity.this,"您输入的验证码不正确,请重试!");
                    ((Throwable) data).printStackTrace();
                }
            }

        }
    };



    //Mob 回掉
    EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);

        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            startEnterAnimation();
        }
        forget_fab.setOnClickListener(this);
        forget_confirm_btn_btn.setOnClickListener(this);
        forget_tv_get_code.setOnClickListener(this);
        cardViewWeakReference = new WeakReference<CardView>(cv);
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);

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

    @TargetApi(21)
    private void startEnterAnimation() {
        Transition transition = TransitionInflater.from(this.getApplicationContext()).inflateTransition(R.transition.registertransition);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                if (cardViewWeakReference.get() != null)
                    cardViewWeakReference.get().setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animationCompleteShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animationCompleteShow(){
        Animator animator = ViewAnimationUtils.createCircularReveal(cv, cv.getWidth() / 2, 0, forget_fab.getWidth() / 2, cv.getHeight());
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (cardViewWeakReference.get() != null)
                    cardViewWeakReference.get().setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animationCloseForget(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(cardViewWeakReference.get(), cardViewWeakReference.get().getWidth() / 2, 0, cv.getHeight(),forget_fab.getWidth() / 2);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    cardViewWeakReference.get().setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    forget_fab.setImageResource(R.drawable.ic_add_24dp);
                    ForgetPwdActivity.super.onBackPressed();
                }
            });
            animator.start();

        }else {

            finish();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_fab:
                animationCloseForget();
                break;
            case R.id.forget_confirm_btn_btn:
                // TODO: 2017/1/23 下一步
                changePwd();
                break;
            case R.id.forget_tv_get_code:
                //获取验证码
                getVerifyCode();
                break;
        }
    }



    private void getVerifyCode(){
        accountEditStr = forget_account.getText().toString();
        if (accountEditStr.isEmpty() || !RegexUtils.checkMobile(accountEditStr) || accountEditStr.length() != 11) {
            //_emailText.setError("enter a valid email address");
            forget_account_text_input.setError("手机号输入错误！");
        } else {
            forget_account_text_input.setError(null);
            mCountDownTimerUtils = new CountDownTimerUtils(forget_tv_get_code, 60000, 1000); //倒计时1分钟
            mCountDownTimerUtils.start();
            // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
            SMSSDK.getVerificationCode("86", accountEditStr);
        }

    }


    private void changePwd(){

        veriCodeEditStr = forget_verification.getText().toString();
         accountEditStr = forget_account.getText().toString();
        pwdEditStr = forget_edpwd.getText().toString();
        String reEnterPassword = forget_edpwd_again.getText().toString();

        if (accountEditStr.isEmpty() || !Patterns.PHONE.matcher(accountEditStr).matches()) {
            //_emailText.setError("enter a valid email address");
            forget_account_text_input.setError("手机号输入错误！");
            return;
        } else {
            forget_account_text_input.setError(null);
        }

        if (pwdEditStr.isEmpty() || pwdEditStr.length() < 4 || pwdEditStr.length() > 10) {
            // _passwordText.setError("between 4 and 10 alphanumeric characters");
            forget_pwd_text_input.setError("密码格式错误");
            return;
        } else {
            forget_pwd_text_input.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(pwdEditStr))) {
            //    _reEnterPasswordText.setError("Password Do not match");
            forget_repwd_text_input.setError("两次密码输入不一致");
            return;
        } else {
            forget_repwd_text_input.setError(null);
        }
        if (veriCodeEditStr == null || "".equals(veriCodeEditStr)){
            ToastUtils.showShort(getApplicationContext(),"请输入验证码!");
        }else {
            //先验证短信接口
            // 提交验证码，其中的code表示验证码，如“1357”
            SMSSDK.submitVerificationCode("86", accountEditStr, veriCodeEditStr);
        }
    }


    private void hasRecord(final String phoneNumber, final String pwd ){

        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("mobilePhoneNumber", phoneNumber);
        query.findObjects(getApplicationContext(), new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> object) {
                // TODO Auto-generated method stub
               // toast("查询用户成功："+object.size());
                BmobUser newUser = new BmobUser();
                newUser.setPassword(pwd);
                newUser.update(getApplicationContext(),object.get(0).getObjectId(),new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                     //   toast("更新用户信息成功:");
                        onChangeSuccess();
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(), "修改密码失败!", Toast.LENGTH_LONG).show();
                        onChangeFailed();
                    }
                });

              //  changeBmobPwd();
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "没有注册或没有找到用户!", Toast.LENGTH_LONG).show();
                onChangeFailed();
            }
        });



    }



    public void onChangeSuccess() {
        setResult(RESULT_OK);
        ForgetPwdActivity.super.onBackPressed();
        //   finish();
    }

    public void onChangeFailed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);

    }
}
