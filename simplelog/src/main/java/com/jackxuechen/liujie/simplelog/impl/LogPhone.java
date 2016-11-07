package com.jackxuechen.liujie.simplelog.impl;

import android.content.Context;

import com.jackxuechen.liujie.simplelog.LogConfigBuild;
import com.jackxuechen.liujie.simplelog.abs.AbsLog;
import com.jackxuechen.liujie.simplelog.abs.IUpload;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import okio.Sink;
import okio.Source;


/**
 * Created by liujie on 2016/9/26.
 */

public class LogPhone extends AbsLog {
    SimpleDateFormat mFormat;
    String mSeparator;
    Calendar mCalendar;
    Context mContext;
    LogConfigBuild.LogConfig mLogConfig;
    StringBuilder sb;
    IUpload mIUpload;

    public LogPhone(Context context) {
        this(context, LogConfigBuild.as(context).build());
    }

    public LogPhone(Context context, LogConfigBuild.LogConfig logConfig) {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        mContext = context;
        mCalendar = Calendar.getInstance();
        mSeparator = "\t";
        if (logConfig == null) {
            mLogConfig = LogConfigBuild.as(context).build();
        } else {
            mLogConfig = logConfig;
        }
        resetUploadFlag();
        setPrintLog(mLogConfig.isPrintLog());
        mIUpload = mLogConfig.getUpload();
        sb = new StringBuilder();

    }


    @Override
    public void record(String level, String tag, String msg) {
        sb.delete(0, sb.length());
        sb.append(mFormat.format(mCalendar.getTimeInMillis()))
                .append(mSeparator)
                .append(level)
                .append(mSeparator)
                .append(tag)
                .append(mSeparator)
                .append(msg)
                .append("\r\n");
        save(sb.toString().getBytes());
    }

    @Override
    public synchronized void save(byte[] bytes) {
        writeToLocal(bytes, 0, bytes.length);
    }

    private synchronized void writeToLocal(byte[] bytes, int offset, int byteCount) {
        File fCacheDir;
        File fUploadDir;
        File file;
        try {

            fCacheDir = new File(mLogConfig.getCacheDir());
            fUploadDir = new File(mLogConfig.getUploadDir());
            if (!fCacheDir.exists()) {
                fCacheDir.mkdirs();
                fUploadDir.mkdirs();
            }
            file = new File(mLogConfig.getLogDirAndName());
            if (!file.exists()) {
                file.createNewFile();
            } else if (file.length() >= mLogConfig.getLogSize() || isUploadNow()) {//文件大于100k
                resetUploadFlag();
                moveAndGzip(file);
                upload();
                file = new File(mLogConfig.getLogDirAndName());
            }
            write(file, true, false, bytes, offset, byteCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void moveAndGzip(File file) throws FileNotFoundException {
        File uploadFile = new File(mLogConfig.getUploadDir() + File.separator + System.currentTimeMillis());
        write(uploadFile, true, true, Okio.source(file));
        file.delete();
    }

    @Override
    public synchronized void upload() {
        if (mIUpload != null) {
            mIUpload.upload(mLogConfig.getUploadDir());
        }
    }

    @Override
    public synchronized void flush() {

    }


    private void write(File file, boolean isAppend, boolean isGzip, byte[] bytes, int offset, int byteCount) {
        Sink sink;
        BufferedSink bufferedSink = null;
        GzipSink gzipSink;

        try {
            sink = isAppend ? Okio.appendingSink(file) : Okio.sink(file);

            if (isGzip) {
                gzipSink = new GzipSink(sink);
                bufferedSink = Okio.buffer(gzipSink);
            } else {
                bufferedSink = Okio.buffer(sink);
            }
            bufferedSink.write(bytes, offset, byteCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bufferedSink);
        }
    }

    private void write(File file, boolean isAppend, boolean isGzip, Source source) {
        Sink sink;
        BufferedSink bufferedSink = null;
        GzipSink gzipSink;

        try {
            sink = isAppend ? Okio.appendingSink(file) : Okio.sink(file);

            if (isGzip) {
                gzipSink = new GzipSink(sink);
                bufferedSink = Okio.buffer(gzipSink);
            } else {
                bufferedSink = Okio.buffer(sink);
            }
            bufferedSink.writeAll(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bufferedSink);
        }
    }

    private void closeQuietly(Closeable closeable) {

        if (closeable != null) {

            try {

                closeable.close();

            } catch (RuntimeException rethrown) {

                throw rethrown;

            } catch (Exception ignored) {

            }

        }

    }


}
