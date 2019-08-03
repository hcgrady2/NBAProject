package com.hcworld.nbalive.UI.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.register_edname)
    EditText account;
    @BindView(R.id.register_edpwd)
    EditText password;
    @BindView(R.id.register_edpwd_again)
    EditText passwordAgain;
    @BindView(R.id.register_fab)
    FloatingActionButton fab;
    @BindView(R.id.register_btn)
    AppCompatButton register_bt;
    @BindView(R.id.cardview)
    CardView cv;

    @BindView(R.id.account_text_input)
    TextInputLayout account_text_input;

    @BindView(R.id.pwd_text_input)
    TextInputLayout pwd_text_input;

    @BindView(R.id.repwd_text_input)
    TextInputLayout repwd_text_input;

    @BindView(R.id.pwd_verification_input)
    TextInputLayout pwd_verification_input;

    @BindView(R.id.register_verification)
    EditText register_verification;

    @BindView(R.id.tv_get_code)
    TextView tv_get_code;

    @BindView(R.id.register_username)
    EditText register_username;

    WeakReference<CardView> cardViewWeakReference;;

    private String accountEditStr;
    private String pwdEditStr;
    private String veriCodeEditStr;
    private String userNameStr;
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
                    accountEditStr  = account.getText().toString();
                    pwdEditStr  = password.getText().toString();
                    userNameStr = register_username.getText().toString();
                    InserUserToBmob(accountEditStr,pwdEditStr,userNameStr);
                } else {
                    // TODO 处理错误的结果
                    Log.i("VerificationCode", "validate error" );
                    ToastUtils.showShort(RegisterActivity.this,"您输入的验证码不正确,请重试!");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_register);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            startEnterAnimation();
        }
        fab.setOnClickListener(this);
        register_bt.setOnClickListener(this);
        tv_get_code.setOnClickListener(this);
        cardViewWeakReference = new WeakReference<CardView>(cv);
        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
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
        Animator animator = ViewAnimationUtils.createCircularReveal(cv, cv.getWidth() / 2, 0, fab.getWidth() / 2, cv.getHeight());
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
    private void animationCloseRegister(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(cardViewWeakReference.get(), cardViewWeakReference.get().getWidth() / 2, 0, cv.getHeight(),fab.getWidth() / 2);
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
                    fab.setImageResource(R.drawable.ic_add_24dp);
                    RegisterActivity.super.onBackPressed();
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
            case R.id.register_fab:
                animationCloseRegister();
            break;
            case R.id.register_btn:
                // TODO: 2017/1/23 下一步
                signup();
                break;
            case R.id.tv_get_code:
                //获取验证码
                getVerifyCode();
                break;
        }
    }


    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }
        //验证码验证
        veriCodeEditStr = register_verification.getText().toString();

        if (veriCodeEditStr == null || "".equals(veriCodeEditStr)){
            ToastUtils.showShort(getApplicationContext(),"请输入验证码!");
        }else {

            accountEditStr  = account.getText().toString();
            pwdEditStr  = password.getText().toString();
            //先验证短信接口
            // 提交验证码，其中的code表示验证码，如“1357”
            SMSSDK.submitVerificationCode("86", accountEditStr, veriCodeEditStr);

        }


        /*    final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);*/
    }


    public void onSignupSuccess() {
        setResult(RESULT_OK);
        RegisterActivity.super.onBackPressed();
     //   finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
    }


    public boolean validate() {
        boolean valid = true;
        accountEditStr = account.getText().toString();
         pwdEditStr = password.getText().toString();
        String reEnterPassword = passwordAgain.getText().toString();
        if (accountEditStr.isEmpty() || !Patterns.PHONE.matcher(accountEditStr).matches()) {
            //_emailText.setError("enter a valid email address");
            account_text_input.setError("手机号输入错误！");
            valid = false;
        } else {
            account_text_input.setError(null);
        }
/*

        if (mobile.isEmpty() || mobile.length()!=10) {
            //  _mobileText.setError("Enter Valid Mobile Number");
            signup_textinput_4.setError("4");
            valid = false;
        } else {
            _mobileText.setError(null);
        }
*/

        if (pwdEditStr.isEmpty() || pwdEditStr.length() < 4 || pwdEditStr.length() > 10) {
            // _passwordText.setError("between 4 and 10 alphanumeric characters");
            pwd_text_input.setError("密码格式错误");
            valid = false;
        } else {
            pwd_text_input.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(pwdEditStr))) {
            //    _reEnterPasswordText.setError("Password Do not match");
            repwd_text_input.setError("两次密码输入不一致");

            valid = false;
        } else {
            repwd_text_input.setError(null);
        }

        return valid;
    }


    private void getVerifyCode(){

        accountEditStr = account.getText().toString();
        if (accountEditStr.isEmpty() || !RegexUtils.checkMobile(accountEditStr) || accountEditStr.length() != 11) {
            //_emailText.setError("enter a valid email address");
            account_text_input.setError("手机号输入错误！");
        } else {
            account_text_input.setError(null);
            mCountDownTimerUtils = new CountDownTimerUtils(tv_get_code, 60000, 1000); //倒计时1分钟
            mCountDownTimerUtils.start();
            // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
            SMSSDK.getVerificationCode("86", accountEditStr);

        }


    }



    private void InserUserToBmob(String phoneNumber,String pwd,String NickName){

        // 使用BmobSDK提供的注册功能
        BmobUser user = new BmobUser();
        user.setUsername(NickName);
        user.setPassword(pwd);
        user.setMobilePhoneNumber(phoneNumber);

        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                onSignupSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                onSignupFailed();
            }
        });


    }




    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        //   moveTaskToBack(true);
        RegisterActivity.super.onBackPressed();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);

        // 写为全局变量是为了这里用
        if (mCountDownTimerUtils != null) {
            mCountDownTimerUtils.cancel();
            mCountDownTimerUtils = null;
        }
    }
}
