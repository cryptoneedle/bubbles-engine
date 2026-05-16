package com.bubbles.common.result.feedback.definition.http.server;


import cn.hutool.v7.http.meta.HttpStatus;

/**
 * <p>description: 501 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class NotImplementedFeedback extends ServerFeedback {
    
    public NotImplementedFeedback(String value) {
        super(HttpStatus.HTTP_NOT_IMPLEMENTED, value);
    }
}