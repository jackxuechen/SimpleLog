package com.jackxuechen.liujie.simplelog;

import android.content.Context;

import com.jackxuechen.liujie.simplelog.abs.AbsLog;
import com.jackxuechen.liujie.simplelog.impl.LogPhone;


/**
 * Created by liujie on 2016/9/26.
 */

public class Log {

    private final String tag = "Log";
    private static Log log;

    public AbsLog mAbsLog;

    private Context mContext;

    boolean mInit = false;


    private Log() {

    }

    public void init(Context context) {
        init(context, LogConfigBuild.as(context).build());
    }


    public void init(Context context, LogConfigBuild.LogConfig logConfig) {
        if (mInit) {
            return;
        }
        if (context == null) {
            android.util.Log.e(tag, "context is null LOG init failed");
            mInit = false;
            return;
        }
        mContext = context;
        mAbsLog = new LogPhone(mContext, logConfig);
        mInit = true;
        android.util.Log.v(tag, "LOG init success");
    }

    public void init(Context context, AbsLog absLog) {
        if (mInit) {
            return;
        }
        if (context == null) {
            android.util.Log.e(tag, "context is null LOG init failed");
            mInit = false;
            return;
        }
        mContext = context;
        mAbsLog = absLog;
        mInit = true;
        android.util.Log.v(tag, "LOG init success");
    }


    public boolean checkInit() {
        if (!mInit) {
            init(mContext);
            return mInit;
        } else {
            return true;
        }
    }

    public static Log as() {
        if (log == null) {
            log = new Log();
        }
        return log;
    }

    public void flush() {
        if (checkInit()) {
            mAbsLog.flush();
        }
    }


    public void v(String tag, String msg) {
        if (checkInit()) {
            mAbsLog.v(tag, msg);
        }
    }

    public void d(String tag, String msg) {
        if (checkInit()) {
            mAbsLog.d(tag, msg);
        }
    }

    public void i(String tag, String msg) {
        if (checkInit()) {
            mAbsLog.i(tag, msg);
        }
    }

    public void w(String tag, String msg) {
        if (checkInit()) {
            mAbsLog.w(tag, msg);
        }
    }

    public void e(String tag, String msg) {
        if (checkInit()) {
            mAbsLog.e(tag, msg);
        }
    }


    public synchronized void uploadNow() {
        mAbsLog.uploadNow();
        mAbsLog.v("Log","正在上传日志。");
    }

}
