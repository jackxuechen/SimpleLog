package com.jackxuechen.liujie.logtest;

import android.widget.TextView;

import com.jackxuechen.liujie.simplelog.abs.IUpload;

import java.io.Closeable;
import java.io.File;

import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by liujie on 16-11-7.
 */

public class MyUpload implements IUpload {

    TextView mTextView;

    public MyUpload(TextView textView) {
        mTextView = textView;
    }

    @Override
    public void upload(String LogCacheDirPath) {
        File fDir;
        fDir = new File(LogCacheDirPath);
        if (fDir.exists()) {
            File[] files = fDir.listFiles();
            Observable.from(files)
                    .flatMap(new Func1<File, Observable<String>>() {
                        @Override
                        public Observable<String> call(final File file) {
                            return Observable.create(new Observable.OnSubscribe<String>() {
                                @Override
                                public void call(Subscriber<? super String> subscriber) {
                                    Source source = null;

                                    BufferedSource bufferedSource = null;

                                    GzipSource gzipSource = null;

                                    try {

                                        source = Okio.source(file);
                                        gzipSource = new GzipSource(source);
                                        bufferedSource = Okio.buffer(gzipSource);

                                        String content = bufferedSource.readUtf8();
                                        file.delete();
                                        subscriber.onNext(content);
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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(String s) {
                            mTextView.setText(s);
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
