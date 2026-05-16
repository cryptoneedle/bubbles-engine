package com.bubbles.common.result.feedback.definition.http.client;

import org.apache.hc.core5.http.HttpStatus;

/**
 * <p>description: 415 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class UnsupportedMediaTypeFeedback extends ClientFeedback {
    
    public UnsupportedMediaTypeFeedback(String value) {
        super(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE, value);
    }
}