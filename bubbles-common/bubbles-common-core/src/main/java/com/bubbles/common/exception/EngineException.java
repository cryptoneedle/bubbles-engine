package com.bubbles.common.exception;


import com.bubbles.common.result.Result;
import com.bubbles.common.result.feedback.Feedback;

/**
 * <p>description: 引擎异常 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
public interface EngineException {
    
    /**
     * @return 错误反馈实体
     */
    Feedback getFeedback();
    
    /**
     * @return 响应结果实体
     */
    Result<String> getResult();
}