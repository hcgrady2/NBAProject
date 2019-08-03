package com.hcworld.nbalive.utils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by hcw on 2019/2/1.
 * Copyright©hcw.All rights reserved.
 */

public class DBUtils {
    //保存或者更新数据
    public static void copyOrUpdateRealm(Realm realm, RealmObject realmObject) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObject);
        realm.commitTransaction();
    }

    public static <T extends RealmObject> void saveList(Realm realm, List<T> realmObjects) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmObjects);
        realm.commitTransaction();
    }
}
