package com.bubbles.common.result.feedback.builder;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.Feedbacks;
import com.bubbles.common.result.feedback.definition.http.server.InternalServerErrorFeedback;
import com.bubbles.common.result.feedback.definition.http.server.NotImplementedFeedback;
import com.bubbles.common.result.feedback.definition.http.server.ServiceUnavailableFeedback;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>description: 5xx服务端错误反馈映射构建器 </p>
 * <p/>
 * 基于双重检查锁的单例模式
 * 状态码说明见 {@link Feedbacks}
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class ServerFeedbackMapperBuilder extends AbstractFeedbackMapperBuilder {
    
    private static volatile ServerFeedbackMapperBuilder builder;
    /** 500 */
    private final Map<Feedback, Integer> internalServerErrorMap = new LinkedHashMap<>() {{
        put(Feedbacks.INTERNAL_SERVER_ERROR, Feedbacks.INTERNAL_SERVER_ERROR.getSequence());
    }};
    /** 501 */
    private final Map<Feedback, Integer> notImplementedMap = new LinkedHashMap<>() {{
        put(Feedbacks.NOT_IMPLEMENTED, Feedbacks.NOT_IMPLEMENTED.getSequence());
    }};
    /** 503 */
    private final Map<Feedback, Integer> serviceUnavailableMap = new LinkedHashMap<>() {{
        put(Feedbacks.SERVICE_UNAVAILABLE, Feedbacks.SERVICE_UNAVAILABLE.getSequence());
    }};
    
    private ServerFeedbackMapperBuilder() {
    }
    
    public static ServerFeedbackMapperBuilder getInstance() {
        if (ObjectUtils.isEmpty(builder)) {
            synchronized (ServerFeedbackMapperBuilder.class) {
                if (ObjectUtils.isEmpty(builder)) {
                    builder = new ServerFeedbackMapperBuilder();
                }
            }
        }
        return builder;
    }
    
    /** 500 */
    public ServerFeedbackMapperBuilder internalServerError(InternalServerErrorFeedback... feedbacks) {
        append(internalServerErrorMap, feedbacks);
        return this;
    }
    
    /** 501 */
    public ServerFeedbackMapperBuilder notImplemented(NotImplementedFeedback... feedbacks) {
        append(notImplementedMap, feedbacks);
        return this;
    }
    
    /** 503 */
    public ServerFeedbackMapperBuilder serviceUnavailable(ServiceUnavailableFeedback... feedbacks) {
        append(serviceUnavailableMap, feedbacks);
        return this;
    }
    
    @Override
    public void build() {
        FeedbackMapper feedbackMapper = FeedbackMapper.getInstance();
        feedbackMapper.append(internalServerErrorMap);
        feedbackMapper.append(notImplementedMap);
        feedbackMapper.append(serviceUnavailableMap);
    }
}