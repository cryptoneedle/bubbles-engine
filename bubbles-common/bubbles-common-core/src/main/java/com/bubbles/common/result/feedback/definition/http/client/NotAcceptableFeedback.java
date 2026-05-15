package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 406 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class NotAcceptableFeedback extends ClientFeedback {
    
    public NotAcceptableFeedback(String value) {
        super(HttpStatus.SC_NOT_ACCEPTABLE, value);
    }
}