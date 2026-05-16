package com.bubbles.common.result.feedback;

/**
 * <p>description: 错误反馈常量 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-10
 */
public interface FeedbackConstants {
    
    /**
     * 非自定义错误反馈标志
     */
    int NOT_CUSTOMIZED = 0;
    
    /**
     * 数据操作相关错误反馈编号
     */
    int CUSTOMIZED_DATA = 6;
    
    /**
     * 基础设施交互错误反馈编号
     */
    int CUSTOMIZED_FRAMEWORK = 7;
}