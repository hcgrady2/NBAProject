package com.hcworld.nbalive.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.hcworld.nbalive.R;

import java.lang.ref.WeakReference;


/**
 * Created by hcw on 2018/11/17.
 * Copyright©hcw.All rights reserved.
 */

public class CountDownTimerUtils extends CountDownTimer {

  //  private TextView mTextView; //显示倒计时的文字
  WeakReference<TextView> mTextView; //显示倒计时的文字  用弱引用 防止内存泄漏


    /**
     * @param textView          The TextView
     * @param millisInFuture     millisInFuture  从开始调用start()到倒计时完成
     *                           并onFinish()方法被调用的毫秒数。（译者注：倒计时时间，单位毫秒）
     * @param countDownInterval 接收onTick(long)回调的间隔时间。（译者注：单位毫秒）
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
     //   this.mTextView = textView;
        this.mTextView = new WeakReference(textView);

    }

    @Override
    public void onTick(long millisUntilFinished) {

        //用弱引用 先判空 避免崩溃
        if (mTextView.get() == null) {
            cancle();
            return;
        }
        mTextView.get().setClickable(false); //设置不可点击

        mTextView.get().setClickable(false); //设置不可点击
        mTextView.get().setText( "发送验证码(" + millisUntilFinished / 1000+")秒" );  //设置倒计时时间
        mTextView.get().setBackgroundResource(R.drawable.validate_code_press_bg); //设置按钮为灰色，这时是不能点击的

        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mTextView.get().getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.get().setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.get().setText("重新获取验证码");
        mTextView.get().setClickable(true);//重新获得点击
        mTextView.get().setBackgroundResource(R.drawable.validate_code_normal_bg);  //还原背景色
    }

    public void cancle() {
        if (this != null) {
            this.cancel();
        }
    }

}
