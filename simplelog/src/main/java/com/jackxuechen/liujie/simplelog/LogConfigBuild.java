package com.jackxuechen.liujie.simplelog;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.jackxuechen.liujie.simplelog.abs.IUpload;

import java.io.File;

/**
 * Created by liujie on 2016/9/27.
 */

public class LogConfigBuild {
    private LogConfig mLogConfig;
    private String mCacheRootDir;
    private long mLogSize;
    private Context mContext;
    private IUpload mUpload;
    private boolean mPrintLog;


    private static LogConfigBuild mLogConfigBuild;

    private LogConfigBuild(Context context) {
        mContext = context;
        mLogConfig = new LogConfig();
    }

    public static LogConfigBuild as(Context context) {
        if (mLogConfigBuild == null) {
            mLogConfigBuild = new LogConfigBuild(context);
        }
        return mLogConfigBuild;
    }


    public LogConfigBuild setCacheRootDir(String cacheRootDir) {
        mCacheRootDir = cacheRootDir;
        return mLogConfigBuild;
    }

    public LogConfigBuild setLogSize(long logSize) {
        mLogSize = logSize;
        return mLogConfigBuild;
    }

    public LogConfigBuild setPrintLog(boolean printLog) {
        mPrintLog = printLog;
        return mLogConfigBuild;
    }

    public LogConfigBuild setUpload(IUpload upload) {
        mUpload = upload;
        return mLogConfigBuild;
    }

    public LogConfig build() {
        if (TextUtils.isEmpty(mCacheRootDir)) {
            mCacheRootDir = Environment.getExternalStorageDirectory().getPath() + File.separator + mContext.getPackageName();
        }
        if (mLogSize <= 100 * 1024) {//100k
            mLogSize = 100 * 1024;
        }
        mLogConfig.init(mCacheRootDir, mLogSize, mUpload, mPrintLog);
        return mLogConfig;
    }


    public class LogConfig {
        private String mCacheDir;
        private String mLogName;
        private String mLogDirAndName;
        private boolean mPrintLog;
        private String mUploadDir;
        private long mLogSize;
        private IUpload mUpload;


        private StringBuilder mTempSb;

        private LogConfig() {
            mTempSb = new StringBuilder();
        }


        public void init(String rootDir, long logSize, IUpload upload, boolean isPrintLog) {
            mPrintLog = isPrintLog;
            mUpload = upload;

            mTempSb.delete(0, mTempSb.length());
            mTempSb.append(rootDir)
                    .append(File.separator)
                    .append("cache");
            mCacheDir = mTempSb.toString();
            mLogName = "record";

            mLogDirAndName = mTempSb.append(File.separator)
                    .append(mLogName)
                    .toString();
            mTempSb.delete(0, mTempSb.length());
            mUploadDir = mTempSb.append(mCacheDir)
                    .append(File.separator)
                    .append("upload").toString();
            mLogSize = logSize;//100k
        }

        public String getCacheDir() {
            return mCacheDir;
        }

        public String getLogName() {
            return mLogName;
        }

        public String getLogDirAndName() {
            return mLogDirAndName;
        }

        public long getLogSize() {
            return mLogSize;
        }

        public String getUploadDir() {
            return mUploadDir;
        }

        public boolean isPrintLog() {
            return mPrintLog;
        }

        public IUpload getUpload() {
            return mUpload;
        }
    }
}
