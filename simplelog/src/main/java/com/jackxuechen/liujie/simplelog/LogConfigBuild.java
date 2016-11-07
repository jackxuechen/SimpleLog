package com.jackxuechen.liujie.simplelog;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by liujie on 2016/9/27.
 */

public class LogConfigBuild {
    private LogConfig mLogConfig;


    private String mCacheRootDir;
    private long mLogSize;

    private Context mContext;

    private static LogConfigBuild logConfigBuild;

    private LogConfigBuild(Application context) {
        mContext = context;
        mLogConfig = new LogConfig();
    }

    public static LogConfigBuild as(Application context) {
        if (logConfigBuild == null) {
            logConfigBuild = new LogConfigBuild(context);
        }
        return logConfigBuild;
    }


    public LogConfigBuild setCacheRootDir(String cacheRootDir) {
        mCacheRootDir = cacheRootDir;
        return logConfigBuild;
    }

    public LogConfigBuild setLogSize(long logSize) {
        mLogSize = logSize;
        return logConfigBuild;
    }

    public LogConfig build() {
        if (TextUtils.isEmpty(mCacheRootDir)) {
            mCacheRootDir = Environment.getExternalStorageDirectory().getPath() + File.separator + mContext.getPackageName();
        }
        if (mLogSize <= 100 * 1024) {//100k
            mLogSize = 100 * 1024;
        }
        mLogConfig.init(mCacheRootDir, mLogSize);
        return mLogConfig;
    }

    public class LogConfig {
        private String mCacheDir;
        private String mLogName;
        private String mLogDirAndName;

        private String mUploadDir;
        private long mLogSize;


        private StringBuilder mTempSb;

        private LogConfig() {
            mTempSb = new StringBuilder();
        }


        public void init(String rootDir, long logSize) {
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
    }
}
