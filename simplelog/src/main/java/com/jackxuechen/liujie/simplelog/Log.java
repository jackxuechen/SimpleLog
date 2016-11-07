package com.jackxuechen.liujie.simplelog;

import android.app.Application;

import com.jackxuechen.liujie.simplelog.abs.AbsLog;
import com.jackxuechen.liujie.simplelog.abs.ILog;
import com.jackxuechen.liujie.simplelog.impl.LogPhone;


/**
 * Created by liujie on 2016/9/26.
 */

public class Log {

    private final String tag = "Log";
    private static Log log;
    public ILog iLog;
    private Application mContext;
    boolean mInit = false;
    boolean mPos = false;
    boolean mPrintLog = false;

    private Log() {

    }

    public void init(boolean isPos, Application context) {
        if (context == null) {
            android.util.Log.e(tag, "context 为 null LOG初始化失败");
            mInit = false;
            return;
        }

        mContext = context;
        mPos = isPos;
        mPrintLog = BuildConfig.DEBUG;
        if (isPos) {//pos机//
            if (iLog == null) {
//                iLog = new LogPos(mContext);
            }
        } else {//手机
            if (iLog == null) {
                iLog = new LogPhone(mContext, mPrintLog);
            }
        }
        mInit = true;
        android.util.Log.v(tag, "LOG初始化成功");

    }


    public boolean checkInit() {
        if (!mInit) {
            init(mPos, mContext);
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


    public synchronized void uploadNow() {
        ((AbsLog) iLog).setUploadNow(true);
//        iLog.record(Log.class,"正在上传日志");
    }

    public void flush() {
        if (checkInit()) {
            iLog.flush();
        }
    }



}
