package com.bubbles.common.result.feedback.builder;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.Feedbacks;
import com.bubbles.common.result.feedback.definition.http.success.NoContentFeedback;
import com.bubbles.common.result.feedback.definition.http.success.OkFeedback;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>description: 2xx成功反馈映射构建器 </p>
 * <p/>
 * 基于双重检查锁的单例模式
 * 状态码说明见 {@link Feedbacks}
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class SuccessFeedbackMapperBuilder extends AbstractFeedbackMapperBuilder {
    
    private static volatile SuccessFeedbackMapperBuilder builder;
    /** 200 */
    private final Map<Feedback, Integer> okMap = new LinkedHashMap<>() {{
        put(Feedbacks.OK, Feedbacks.OK.getSequence());
    }};
    /** 204 */
    private final Map<Feedback, Integer> noContentMap = new LinkedHashMap<>() {{
        put(Feedbacks.NO_CONTENT, Feedbacks.NO_CONTENT.getSequence());
    }};
    
    private SuccessFeedbackMapperBuilder() {
    }
    
    public static SuccessFeedbackMapperBuilder getInstance() {
        if (ObjectUtils.isEmpty(builder)) {
            synchronized (SuccessFeedbackMapperBuilder.class) {
                if (ObjectUtils.isEmpty(builder)) {
                    builder = new SuccessFeedbackMapperBuilder();
                }
            }
        }
        return builder;
    }
    
    /** 200 */
    public SuccessFeedbackMapperBuilder ok(OkFeedback... feedbacks) {
        append(okMap, feedbacks);
        return this;
    }
    
    /** 204 */
    public SuccessFeedbackMapperBuilder noContent(NoContentFeedback... feedbacks) {
        append(noContentMap, feedbacks);
        return this;
    }
    
    @Override
    public void build() {
        FeedbackMapper feedbackMapper = FeedbackMapper.getInstance();
        feedbackMapper.append(okMap);
        feedbackMapper.append(noContentMap);
    }
}