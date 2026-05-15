package com.bubbles.common.result;


import cn.hutool.v7.core.date.DateFormatPool;

/**
 * <p>description: 响应结果常量 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public interface ResultConstants {
    
    /**
     * 默认时间格式 yyyy-MM-dd HH:mm:ss.SSS
     */
    String DATE_TIME_MS_PATTERN = DateFormatPool.NORM_DATETIME_MS_PATTERN;
    
    /**
     * 默认响应信息
     */
    String SUCCESS_MASSAGE = "操作成功";
    String FAILURE_MASSAGE = "操作失败";
    String EMPTY_MESSAGE = "未查询到相关内容";
}