package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 401 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class UnauthorizedFeedback extends ClientFeedback {
    
    public UnauthorizedFeedback(String value) {
        super(HttpStatus.SC_UNAUTHORIZED, value);
    }
}