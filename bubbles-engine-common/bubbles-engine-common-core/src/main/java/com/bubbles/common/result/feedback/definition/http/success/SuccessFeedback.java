package com.bubbles.common.result.feedback.definition.http.success;


import com.bubbles.common.result.feedback.definition.http.HttpStatusFeedback;

/**
 * <p>description: 2xx成功反馈实体 </p>
 * <p/>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public class SuccessFeedback extends HttpStatusFeedback {
    
    public SuccessFeedback(int status, String message) {
        super(status, message);
    }
}