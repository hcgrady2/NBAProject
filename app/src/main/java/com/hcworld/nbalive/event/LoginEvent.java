package com.hcworld.nbalive.event;

/**
 * Created by hcw on 2018/10/14.
 * Copyright©hcw.All rights reserved.
 */

public class LoginEvent {
  // public UserBean userBean;
    public int type;
    // 0 登陆，1 退出
    public LoginEvent(int type ){
       this.type = type;
    }
}
