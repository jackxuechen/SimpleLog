package com.jackxuechen.liujie.logtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jackxuechen.liujie.simplelog.Log;
import com.jackxuechen.liujie.simplelog.LogConfigBuild;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView tv_show;
    TextView tv_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_show = (TextView) findViewById(R.id.tv_show);
        tv_content = (TextView) findViewById(R.id.tv_content);
        final LogConfigBuild.LogConfig logConfig = LogConfigBuild
                .as(getApplicationContext())
                .setPrintLog(true)
                .setUpload(new MyUpload(tv_content))
                .build();

        Log.as().init(getApplicationContext(), logConfig);
        final File file = new File(logConfig.getLogDirAndName());

        Observable.interval(500, 500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.as().uploadNow();
                    }
                });


    }

    int i = 0;

    public void click(View v) {
        Log.as().v("MainActivity", "i:"+i);
        tv_show.setText("记录i:" + i++);
    }


}
