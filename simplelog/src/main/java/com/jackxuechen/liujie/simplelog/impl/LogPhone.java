package com.jackxuechen.liujie.simplelog.impl;

import android.app.Application;


import com.jackxuechen.liujie.simplelog.LogConfigBuild;
import com.jackxuechen.liujie.simplelog.RecordType;
import com.jackxuechen.liujie.simplelog.abs.AbsLog;

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
    Application mContext;
    LogConfigBuild.LogConfig mLogConfig;
    StringBuilder sb;


    public LogPhone(Application context, boolean isPrintLog) {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        mContext = context;
        mCalendar = Calendar.getInstance();
        mSeparator = "\t";
        mLogConfig = LogConfigBuild.as(mContext).build();
        this.isPrintLog = isPrintLog;
        sb = new StringBuilder();

    }



    public void record(Class c, RecordType type, String description, String request, String result) {
        StringBuffer sb = new StringBuffer();
        sb.append(mFormat.format(mCalendar.getTimeInMillis()))
                .append(mSeparator)
                .append(c.getSimpleName())
                .append(mSeparator)
                .append(type)
                .append(mSeparator)
                .append(description)
                .append(mSeparator)
                .append(request)
                .append(mSeparator)
                .append(result)
                .append("\r\n");
        if (isPrintLog) {
            android.util.Log.v(c.getSimpleName(), sb.toString());
        }
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
            } else if (file.length() >= mLogConfig.getLogSize() || isUploadNow) {//文件大于100k
                setUploadNow(false);
                moveAndGzip(file);
                upload();
                file = new File(mLogConfig.getLogDirAndName());
            }
            write(file, true, true, bytes, offset, byteCount);

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
//        File fDir;
//        fDir = new File(mLogConfig.getUploadDir());
//        if (fDir.exists()) {
//            File[] files = fDir.listFiles();
//
//            Observable.from(files)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(Schedulers.io())
//                    .map(new Func1<File, CommResponse>() {
//                        @Override
//                        public CommResponse call(final File file) {
//                            ParamMap params = new ParamMap();
//                            CommResponse commResponse = null;
//                            params.put("filename", file.getName() + ".txt");
//                            try {
//                                Source source = Okio.source(file);
//                                BufferedSource buffer = Okio.buffer(source);
//                                byte[] bytes = buffer.readByteArray();
//                                params.put("bytes", bytes);
//                                commResponse = AccRequestUtils.sendData(Urls.PROCESSOR_LOGCAT_REPORT, params);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            AccCommonResponse.dealCommResponse(commResponse, new AccCommonResponse.OnResultListener() {
//                                @Override
//                                public void onSuccess(ParamMap data) {
//                                    file.delete();
//                                    Log.as().record(LogPhone.class, "删除日志文件成功");
//                                }
//
//                                @Override
//                                public void onError(AccCommonResponse.ErrorNet e) {
//                                    Log.as().record(LogPhone.class, e.toString());
//                                }
//                            });
//
//                            return commResponse;
//                        }
//                    })
//                    .subscribe(new Subscriber<CommResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.as().record(LogPhone.class, e.toString());
//                            e.printStackTrace();
//
//                        }
//
//                        @Override
//                        public void onNext(CommResponse o) {
//
//                        }
//                    });


//        }

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
