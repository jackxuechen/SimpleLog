package com.jackxuechen.liujie.simplelog;

/**
 * 记录类型
 */

public enum RecordType {
    /**
     * 一般性记录类型
     */
    RECORD,
    /**
     * 用户手动操作
     */
    OPERATION,
    /**
     * 请求类型
     */
    REQUEST,
    /**
     * 响应类型
     */
    RESULT;
}
