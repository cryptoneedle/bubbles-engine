package com.bubbles.common.result.feedback.definition.http;


import com.bubbles.common.result.feedback.Feedback;

/**
 * <p>description: Http状态码错误反馈实体 </p>
 * <p/>
 * 在框架内部构建
 * 通过 HttpStatus的第一位状态码 进行包分类管理
 * 1xx  informational  信息
 * 2xx  success        成功
 * 3xx  redirection    重定向
 * 4xx  client         客户端错误
 * 5xx  server         服务器错误
 * 具体的类是根据 HttpStatus状态码命名 的方式命名
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public class HttpStatusFeedback extends Feedback {
    
    public HttpStatusFeedback(int status, String message) {
        super(status, message);
    }
}