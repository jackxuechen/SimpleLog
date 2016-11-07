package com.jackxuechen.liujie.simplelog.impl;

import android.app.IntentService;
import android.content.Intent;


import com.jackxuechen.liujie.simplelog.abs.IUpload;



/**
 * Created by liujie on 2016/10/20.
 */

public class UplaodLog implements IUpload {

    public static class UploadLogService extends IntentService {

        public UploadLogService() {
            super("UploadLogService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
//            File fDir;
//            fDir = new File(intent.getStringExtra("path"));
//            if (fDir.exists()) {
//                File[] files = fDir.listFiles();
//                Observable.from(files)
//                        .map(new Func1<File, CommResponse>() {
//                            @Override
//                            public CommResponse call(File file) {
//                                ParamMap params = new ParamMap();
//                                CommResponse commResponse = null;
//                                params.put("filename", file.getName() + ".txt");
//                                try {
//                                    Source source = Okio.source(file);
//                                    BufferedSource buffer = Okio.buffer(source);
//                                    byte[] bytes = buffer.readByteArray();
//                                    params.put("bytes", bytes);
//                                    commResponse = AccRequestUtils.sendData(Urls.PROCESSOR_LOGCAT_REPORT, params);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                return commResponse;
//                            }
//                        })
//                        .subscribe(new Subscriber<CommResponse>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(CommResponse o) {
//
//                            }
//                        });
//
//
//            }
        }
    }

    @Override
    public void upload(String LogCacheDirPath) {

    }
}
