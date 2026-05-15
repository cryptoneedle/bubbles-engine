package com.bubbles.common.result.feedback.builder;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.Feedbacks;
import com.bubbles.common.result.feedback.definition.http.client.*;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>description: 4xx客户端错误反馈映射构建器 </p>
 * <p/>
 * 基于双重检查锁的单例模式
 * 状态码说明见 {@link Feedbacks}
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class ClientFeedbackMapperBuilder extends AbstractFeedbackMapperBuilder {
    
    private static volatile ClientFeedbackMapperBuilder builder;
    /** 401 */
    private final Map<Feedback, Integer> unauthorizedMap = new LinkedHashMap<>() {{
        put(Feedbacks.UNAUTHORIZED, Feedbacks.UNAUTHORIZED.getSequence());
    }};
    /** 403 */
    private final Map<Feedback, Integer> forbiddenMap = new LinkedHashMap<>() {{
        put(Feedbacks.FORBIDDEN, Feedbacks.FORBIDDEN.getSequence());
    }};
    /** 404 */
    private final Map<Feedback, Integer> notFoundMap = new LinkedHashMap<>() {{
        put(Feedbacks.NO_RESOURCE_FOUND_EXCEPTION, Feedbacks.NO_RESOURCE_FOUND_EXCEPTION.getSequence());
    }};
    /** 405 */
    private final Map<Feedback, Integer> methodNotAllowedMap = new LinkedHashMap<>() {{
        put(Feedbacks.METHOD_NOT_ALLOWED, Feedbacks.METHOD_NOT_ALLOWED.getSequence());
    }};
    /** 406 */
    private final Map<Feedback, Integer> notAcceptableMap = new LinkedHashMap<>() {{
        put(Feedbacks.NOT_ACCEPTABLE, Feedbacks.NOT_ACCEPTABLE.getSequence());
    }};
    /** 412 */
    private final Map<Feedback, Integer> preconditionFailedMap = new LinkedHashMap<>() {{
        put(Feedbacks.PRECONDITION_FAILED, Feedbacks.PRECONDITION_FAILED.getSequence());
    }};
    /** 415 */
    private final Map<Feedback, Integer> unsupportedMediaTypeMap = new LinkedHashMap<>() {{
        put(Feedbacks.UNSUPPORTED_MEDIA_TYPE, Feedbacks.UNSUPPORTED_MEDIA_TYPE.getSequence());
    }};
    
    private ClientFeedbackMapperBuilder() {
    }
    
    public static ClientFeedbackMapperBuilder getInstance() {
        if (ObjectUtils.isEmpty(builder)) {
            synchronized (ClientFeedbackMapperBuilder.class) {
                if (ObjectUtils.isEmpty(builder)) {
                    builder = new ClientFeedbackMapperBuilder();
                }
            }
        }
        return builder;
    }
    
    /** 401 */
    public ClientFeedbackMapperBuilder unauthorized(UnauthorizedFeedback... feedbacks) {
        append(unauthorizedMap, feedbacks);
        return this;
    }
    
    /** 403 */
    public ClientFeedbackMapperBuilder forbidden(ForbiddenFeedback... feedbacks) {
        append(forbiddenMap, feedbacks);
        return this;
    }
    
    /** 404 */
    public ClientFeedbackMapperBuilder notFound(NotFoundFeedback... feedbacks) {
        append(notFoundMap, feedbacks);
        return this;
    }
    
    /** 405 */
    public ClientFeedbackMapperBuilder methodNotAllowed(MethodNotAllowedFeedback... feedbacks) {
        append(methodNotAllowedMap, feedbacks);
        return this;
    }
    
    /** 406 */
    public ClientFeedbackMapperBuilder notAcceptable(NotAcceptableFeedback... feedbacks) {
        append(notAcceptableMap, feedbacks);
        return this;
    }
    
    /** 412 */
    public ClientFeedbackMapperBuilder preconditionFailed(PreconditionFailedFeedback... feedbacks) {
        append(preconditionFailedMap, feedbacks);
        return this;
    }
    
    /** 415 */
    public ClientFeedbackMapperBuilder unsupportedMediaType(UnsupportedMediaTypeFeedback... feedbacks) {
        append(unsupportedMediaTypeMap, feedbacks);
        return this;
    }
    
    @Override
    public void build() {
        FeedbackMapper feedbackMapper = FeedbackMapper.getInstance();
        feedbackMapper.append(unauthorizedMap);
        feedbackMapper.append(unauthorizedMap);
        feedbackMapper.append(forbiddenMap);
        feedbackMapper.append(notFoundMap);
        feedbackMapper.append(methodNotAllowedMap);
        feedbackMapper.append(notAcceptableMap);
        feedbackMapper.append(preconditionFailedMap);
        feedbackMapper.append(unsupportedMediaTypeMap);
    }
}