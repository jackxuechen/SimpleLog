package com.jackxuechen.liujie.logtest;

import android.app.Application;

import com.jackxuechen.liujie.simplelog.Log;
import com.jackxuechen.liujie.simplelog.LogConfigBuild;

/**
 * Created by liujie on 16-11-7.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final LogConfigBuild.LogConfig logConfig = LogConfigBuild
                .as(getApplicationContext())
                .setPrintLog(true)
                .setUpload(new MyUpload())
                .build();

        Log.as().init(getApplicationContext(), logConfig);

    }
}
