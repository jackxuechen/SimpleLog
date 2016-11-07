package com.jackxuechen.liujie.simplelog.abs;


/**
 * Created by liujie on 2016/9/26.
 */

public interface ILog {



    void save(byte[] bytes);

    void upload();

    void flush();

}
