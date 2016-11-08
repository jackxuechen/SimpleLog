package com.jackxuechen.liujie.simplelog.abs;

/**
 * Created by liujie on 2016/10/20.
 */

public interface IUpload {
    /**
     * 上传接口
     * 当日志文件大小大于等于100kb时，会调用该方法
     * 该路径下可能有多个带上传的日志文件，需要自己做按需求上传
     *
     * @param LogCacheDirPath 日志上传文件夹全路径
     */
    void upload(String LogCacheDirPath);
}
