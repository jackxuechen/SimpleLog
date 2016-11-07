package com.jackxuechen.liujie.logtest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.jackxuechen.liujie.simplelog.Log;
import com.jackxuechen.liujie.simplelog.LogConfigBuild;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.jackxuechen.liujie.logtest", appContext.getPackageName());
    }


    @Test
    public void test() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        final LogConfigBuild.LogConfig logConfig = LogConfigBuild
                .as(appContext)
                .setPrintLog(true)
                .build();

        Log.as().init(appContext, logConfig);
        for (int i = 0; i < 4000; i++) {
            Log.as().v("test", "i:" + i);
        }
    }

    @Test
    public void test2() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        final LogConfigBuild.LogConfig logConfig = LogConfigBuild
                .as(appContext)
                .setPrintLog(true)
                .build();

        Source source = null;

        BufferedSource bufferedSource = null;

        GzipSource gzipSource = null;

        File file;

        try {
            file = new File(logConfig.getUploadDir());
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                source = Okio.source(files[0]);
                gzipSource = new GzipSource(source);
                bufferedSource = Okio.buffer(gzipSource);
                String content = bufferedSource.readUtf8();
                System.out.print(content);
            }


        } catch (Exception e) {
            e.printStackTrace();

        } finally {


        }
    }

}
