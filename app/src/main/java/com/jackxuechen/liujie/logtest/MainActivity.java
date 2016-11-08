package com.jackxuechen.liujie.logtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jackxuechen.liujie.simplelog.Log;

public class MainActivity extends AppCompatActivity {

    TextView tv_show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_show = (TextView) findViewById(R.id.tv_show);
    }

    int i = 0;

    public void click(View v) {
        Log.as().v("MainActivity", "i:" + i);
    }


    public void upload_immediacy(View v) {
        Log.as().uploadNow();
    }


}
