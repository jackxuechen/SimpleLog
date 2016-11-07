package com.jackxuechen.liujie.simplelog.abs;



/**
 * Created by liujie on 2016/9/26.
 */

public abstract class AbsLog implements ILog {
    protected boolean isUploadNow;
    protected boolean isPrintLog;



    public boolean isUploadNow() {
        return isUploadNow;
    }

    public synchronized void setUploadNow(boolean uploadNow) {
        isUploadNow = uploadNow;
    }

    public boolean isPrintLog() {
        return isPrintLog;
    }

    public void setPrintLog(boolean printLog) {
        isPrintLog = printLog;
    }
}
