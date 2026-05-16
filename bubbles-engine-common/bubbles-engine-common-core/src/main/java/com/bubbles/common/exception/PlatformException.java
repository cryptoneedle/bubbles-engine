package com.bubbles.common.exception;

/**
 * <p>description: 平台异常基类 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
public class PlatformException extends AbstractException {
    
    public PlatformException() {
        super();
    }
    
    public PlatformException(String message) {
        super(message);
    }
    
    public PlatformException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public PlatformException(Throwable cause) {
        super(cause);
    }
    
    public PlatformException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}