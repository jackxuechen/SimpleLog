package com.jackxuechen.liujie.simplelog.abs;


import android.util.Log;

/**
 * Created by liujie on 2016/9/26.
 */

public abstract class AbsLog implements ILog {
    private boolean mUploadNow;
    private boolean mPrintLog;


    @Override
    public void v(String tag, String msg) {
        if (isPrintLog()) {
            Log.v(tag, msg);
        }
        record(VERBOSE, tag, msg);
    }

    @Override
    public void d(String tag, String msg) {
        if (isPrintLog()) {
            Log.d(tag, msg);
        }
        record(DEBUG, tag, msg);
    }

    @Override
    public void i(String tag, String msg) {
        if (isPrintLog()) {
            Log.i(tag, msg);
        }
        record(INFO, tag, msg);
    }

    @Override
    public void w(String tag, String msg) {
        if (isPrintLog()) {
            Log.w(tag, msg);
        }
        record(WARN, tag, msg);
    }

    @Override
    public void e(String tag, String msg) {
        if (isPrintLog()) {
            Log.e(tag, msg);
        }
        record(ERROR, tag, msg);
    }

    public boolean isUploadNow() {
        return mUploadNow;
    }

    public synchronized void uploadNow() {
        mUploadNow = true;
    }

    public synchronized void resetUploadFlag() {
        mUploadNow = false;
    }


    public boolean isPrintLog() {
        return mPrintLog;
    }

    public void setPrintLog(boolean printLog) {
        mPrintLog = printLog;
    }
}
