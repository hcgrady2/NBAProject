package com.hcworld.nbalive.http.bean.user;

import cn.bmob.v3.BmobObject;

/**
 * Created by hcw on 2018/7/22.
 * Copyright©hcw.All rights reserved.
 */

public class UserBean extends BmobObject {
    private String UserId;
    private String Name;
    private String Pwd;
    private String PhoneNum;
    private String Email;
    private String Mac;
    // 头像 url 地址
    private String Avatar;
    private String Local;
    // 简介
    private String Bio;
    // 关注列表，Json 格式存储
    private String FollowerUsers;
    // 粉丝 Json 格式存储
    private String Followers;
    // 用户积分
    private Integer Scores;
    // 用户 评论与 赞过的 Id
    private String HistoryItemId;
    //标识是评论与赞
    private Boolean Flag;
    // 备用字段一， 先不用
    private String SpareOne;
    // 备用字段二，先不用
    private String SpareTwo;

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }



    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getFollowerUsers() {
        return FollowerUsers;
    }

    public void setFollowerUsers(String followerUsers) {
        FollowerUsers = followerUsers;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public Integer getScores() {
        return Scores;
    }

    public void setScores(Integer scores) {
        Scores = scores;
    }

    public String getHistoryItemId() {
        return HistoryItemId;
    }

    public void setHistoryItemId(String historyItemId) {
        HistoryItemId = historyItemId;
    }

    public Boolean getFlag() {
        return Flag;
    }

    public void setFlag(Boolean flag) {
        Flag = flag;
    }

    public String getSpareOne() {
        return SpareOne;
    }

    public void setSpareOne(String spareOne) {
        SpareOne = spareOne;
    }

    public String getSpareTwo() {
        return SpareTwo;
    }

    public void setSpareTwo(String spareTwo) {
        SpareTwo = spareTwo;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }
}
