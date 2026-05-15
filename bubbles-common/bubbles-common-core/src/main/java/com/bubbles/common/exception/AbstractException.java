package com.bubbles.common.exception;

/**
 * <p>description: 异常抽象类 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
public abstract class AbstractException extends Exception {
    
    protected AbstractException() {
        super();
    }
    
    protected AbstractException(String message) {
        super(message);
    }
    
    protected AbstractException(String message, Throwable cause) {
        super(message, cause);
    }
    
    protected AbstractException(Throwable cause) {
        super(cause);
    }
    
    protected AbstractException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}