package com.jackxuechen.liujie.simplelog.abs;


/**
 * Created by liujie on 2016/9/26.
 */

public interface ILog {


    String DEBUG = "DEBUG";
    String ERROR = "ERROR";
    String INFO = "INFO";
    String VERBOSE = "VERBOSE";
    String WARN = "WARN";

    void v(String tag, String msg);

    void d(String tag, String msg);

    void i(String tag, String msg);

    void w(String tag, String msg);

    void e(String tag, String msg);

    void record(String level, String tag, String msg);

    void save(byte[] bytes);

    void upload();

    void flush();

}
