package com.bubbles.common.result.feedback.definition.http.server;


import com.bubbles.common.result.feedback.definition.http.HttpStatusFeedback;

/**
 * <p>description: 5xx服务端错误反馈实体 </p>
 * <p/>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public class ServerFeedback extends HttpStatusFeedback {
    
    public ServerFeedback(int status, String message) {
        super(status, message);
    }
}