package com.hcworld.nbalive.http.bean.app;

import cn.bmob.v3.BmobObject;

/**
 * Created by hcw on 2019/2/14.
 * Copyright©hcw.All rights reserved.
 */

public class AppUpdate extends BmobObject {
    String IsUpdate;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    String versionName;
    public String getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.IsUpdate = isUpdate;
    }
}
