package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 412 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class PreconditionFailedFeedback extends ClientFeedback {
    
    public PreconditionFailedFeedback(String value) {
        super(HttpStatus.SC_PRECONDITION_FAILED, value);
    }
}