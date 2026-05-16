package com.bubbles.common.result.feedback.definition.http.server;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 500 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class InternalServerErrorFeedback extends ServerFeedback {
    
    public InternalServerErrorFeedback(String value) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, value);
    }
}