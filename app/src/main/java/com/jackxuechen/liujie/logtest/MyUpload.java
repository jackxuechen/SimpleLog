package com.jackxuechen.liujie.logtest;

import com.jackxuechen.liujie.simplelog.abs.IUpload;

import java.io.Closeable;
import java.io.File;

import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liujie on 16-11-7.
 * 实现IUpload 接口 当日志文件大小超过100k时，会触发 IUpload接口的upload（String LogCacheDirPath） 方法
 */

public class MyUpload implements IUpload {

    @Override
    public void upload(String LogCacheDirPath) {
        File fDir;
        fDir = new File(LogCacheDirPath);
        if (fDir.exists()) {
            File[] files = fDir.listFiles();
            Observable.from(files)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Func1<File, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(final File file) {
                            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                                @Override
                                public void call(Subscriber<? super Boolean> subscriber) {
                                    Source source = null;
                                    BufferedSource bufferedSource = null;
                                    GzipSource gzipSource = null;
                                    try {
                                        source = Okio.source(file);
                                        gzipSource = new GzipSource(source);
                                        bufferedSource = Okio.buffer(gzipSource);
                                        byte[] bytes = bufferedSource.readByteArray();
                                        //// TODO: 16-11-8 上传到网络或其他操作
                                        subscriber.onNext(file.delete());
                                        subscriber.onCompleted();

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    } finally {

                                        closeQuietly(bufferedSource);

                                    }
                                }
                            });
                        }
                    })
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean b) {

                        }
                    });

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
