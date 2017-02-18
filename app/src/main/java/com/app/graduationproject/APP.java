package com.app.graduationproject;

import android.app.Application;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lenovo on 2016/10/31.
 */
public class APP extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build());
        BGASwipeBackManager.getInstance().init(this);
    }
}
