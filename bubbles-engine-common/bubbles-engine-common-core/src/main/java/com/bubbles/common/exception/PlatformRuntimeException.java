package com.bubbles.common.exception;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.Feedbacks;

/**
 * <p>description: 平台运行时异常基类 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
public class PlatformRuntimeException extends AbstractRuntimeException {
    
    public PlatformRuntimeException() {
        super();
    }
    
    public PlatformRuntimeException(String message) {
        super(message);
    }
    
    public PlatformRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PlatformRuntimeException(Throwable cause) {
        super(cause);
    }
    
    public PlatformRuntimeException(String message,
                                    Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * @return 错误反馈实体
     */
    @Override
    public Feedback getFeedback() {
        return Feedbacks.INTERNAL_SERVER_ERROR;
    }
}