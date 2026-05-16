package com.bubbles.common.result.feedback.builder;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.Feedbacks;
import com.bubbles.common.result.feedback.definition.customize.CustomizeFeedback;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>description: 自定义错误反馈映射构建器 </p>
 * <p/>
 * 基于双重检查锁的单例模式
 * 状态码说明见 {@link Feedbacks}
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class CustomizeFeedbackMapperBuilder extends AbstractFeedbackMapperBuilder {
    
    private static volatile CustomizeFeedbackMapperBuilder builder;
    /** 自定义 */
    private final Map<Integer, Map<Feedback, Integer>> customCustomizeMapMap = new LinkedHashMap<>();
    
    private CustomizeFeedbackMapperBuilder() {
    }
    
    public static CustomizeFeedbackMapperBuilder getInstance() {
        if (ObjectUtils.isEmpty(builder)) {
            synchronized (CustomizeFeedbackMapperBuilder.class) {
                if (ObjectUtils.isEmpty(builder)) {
                    builder = new CustomizeFeedbackMapperBuilder();
                }
            }
        }
        return builder;
    }
    
    public CustomizeFeedbackMapperBuilder customize(CustomizeFeedback... feedbacks) {
        for (CustomizeFeedback feedback : feedbacks) {
            customCustomizeMapMap.computeIfAbsent(feedback.getCustom(), LinkedHashMap::new)
                                 .put(feedback, feedback.getSequence(customCustomizeMapMap.get(feedback.getCustom())
                                                                                          .size()));
        }
        return this;
    }
    
    @Override
    public void build() {
        FeedbackMapper feedbackMapper = FeedbackMapper.getInstance();
        customCustomizeMapMap.forEach((custom, feedbacks) -> feedbackMapper.append(feedbacks));
    }
}