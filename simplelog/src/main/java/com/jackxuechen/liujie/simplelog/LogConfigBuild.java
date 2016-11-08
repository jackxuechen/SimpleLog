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

    /**
     * @param cacheRootDir 日志目录根目录
     * @return
     */
    public LogConfigBuild setCacheRootDir(String cacheRootDir) {
        mCacheRootDir = cacheRootDir;
        return mLogConfigBuild;
    }

    /**
     * 设置缓存日志大小，默认为100kb，
     * 以100kb为例，该大小是缓存大小，经过压缩后实际待上传的日志大小在6kb左右
     * 日志每次达到100k会触发IUpload接口的upload（String path）方法
     *
     * @param logSize 日志大小 bit
     * @return
     */
    public LogConfigBuild setLogSize(long logSize) {
        mLogSize = logSize;
        return mLogConfigBuild;
    }

    /**
     * 设置是否在控制台打印日志
     * 默认关闭
     *
     * @param printLog
     * @return
     */
    public LogConfigBuild setPrintLog(boolean printLog) {
        mPrintLog = printLog;
        return mLogConfigBuild;
    }

    /**
     * 设置上传类
     *
     * @param upload
     * @return
     */
    public LogConfigBuild setUpload(IUpload upload) {
        mUpload = upload;
        return mLogConfigBuild;
    }

    /**
     * 返回配置实例
     *
     * @return
     */
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
                    .append("cache")
                    .append(File.separator);
            mCacheDir = mTempSb.toString();
            mLogName = "record";

            mLogDirAndName = mTempSb.append(mLogName).toString();
            mTempSb.delete(0, mTempSb.length());
            mUploadDir = mTempSb.append(mCacheDir)
                    .append(File.separator)
                    .append("upload").toString();
            mLogSize = logSize;//100k
        }

        /**
         * 获取日志缓存根路径
         *
         * @return
         */
        public String getCacheDir() {
            return mCacheDir;
        }

        /**
         * 获取日志缓存文件名字
         * 默认为record
         *
         * @return
         */
        public String getLogName() {
            return mLogName;
        }

        /**
         * 获取日志缓存文件全路径+文件名
         *
         * @return
         */
        public String getLogDirAndName() {
            return mLogDirAndName;
        }

        /**
         * 获取日志缓存大小
         *
         * @return
         */
        public long getLogSize() {
            return mLogSize;
        }

        /**
         * 获取待上传路径
         *
         * @return
         */
        public String getUploadDir() {
            return mUploadDir;
        }

        /**
         * 是否打印日志
         *
         * @return
         */
        public boolean isPrintLog() {
            return mPrintLog;
        }

        /**
         * 获取上传类
         *
         * @return
         */
        public IUpload getUpload() {
            return mUpload;
        }
    }
}
