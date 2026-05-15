package com.bubbles.common.result.feedback.definition.http.server;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 503 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class ServiceUnavailableFeedback extends ServerFeedback {
    
    public ServiceUnavailableFeedback(String value) {
        super(HttpStatus.SC_SERVICE_UNAVAILABLE, value);
    }
}