package com.jackxuechen.liujie.logtest;

import com.jackxuechen.liujie.simplelog.abs.IUpload;

import java.io.Closeable;
import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

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

            Observable.fromArray(files)
                    .observeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<File>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(File file) {
                            Source source = null;
                            BufferedSource bufferedSource = null;
                            GzipSource gzipSource = null;
                            try {
                                source = Okio.source(file);
                                gzipSource = new GzipSource(source);
                                bufferedSource = Okio.buffer(gzipSource);
                                byte[] bytes = bufferedSource.readByteArray();
                                //// TODO: 16-11-8 上传到网络或其他操作
                                file.delete();

                            } catch (Exception e) {
                                e.printStackTrace();

                            } finally {

                                closeQuietly(bufferedSource);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

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
