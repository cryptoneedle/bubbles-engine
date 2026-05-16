package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 404 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class NotFoundFeedback extends ClientFeedback {
    
    public NotFoundFeedback(String value) {
        super(HttpStatus.SC_NOT_FOUND, value);
    }
}