package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 405 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class MethodNotAllowedFeedback extends ClientFeedback {
    
    public MethodNotAllowedFeedback(String value) {
        super(HttpStatus.SC_METHOD_NOT_ALLOWED, value);
    }
}