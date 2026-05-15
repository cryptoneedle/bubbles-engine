package com.bubbles.common.result.feedback.definition.http.client;


import com.bubbles.common.result.feedback.definition.http.HttpStatusFeedback;

/**
 * <p>description: 4xx客户端错误反馈实体 </p>
 * <p/>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public class ClientFeedback extends HttpStatusFeedback {
    
    public ClientFeedback(int status, String message) {
        super(status, message);
    }
}