package com.bubbles.common.result.feedback.definition.http.success;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 204 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class NoContentFeedback extends SuccessFeedback {
    
    public NoContentFeedback(String value) {
        super(HttpStatus.SC_NO_CONTENT, value);
    }
}