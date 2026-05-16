package com.bubbles.common.exception;


import com.bubbles.common.result.Result;

/**
 * <p>description: 运行时异常抽象类 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
public abstract class AbstractRuntimeException extends RuntimeException implements EngineException {
    
    protected AbstractRuntimeException() {
        super();
    }
    
    protected AbstractRuntimeException(String message) {
        super(message);
    }
    
    protected AbstractRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected AbstractRuntimeException(Throwable cause) {
        super(cause);
    }
    
    protected AbstractRuntimeException(String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    /**
     * @return 响应结果实体
     */
    @Override
    public Result<String> getResult() {
        Result<String> result = Result.failure(getFeedback());
        result.stackTrace(super.getStackTrace());
        result.detail(super.getMessage());
        return result;
    }
}