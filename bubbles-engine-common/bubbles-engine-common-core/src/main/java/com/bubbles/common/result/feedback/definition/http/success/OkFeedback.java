package com.bubbles.common.result.feedback.definition.http.success;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 200 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class OkFeedback extends SuccessFeedback {
    
    public OkFeedback(String value) {
        super(HttpStatus.SC_OK, value);
    }
}