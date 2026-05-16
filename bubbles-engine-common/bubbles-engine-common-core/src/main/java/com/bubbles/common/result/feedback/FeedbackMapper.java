package com.bubbles.common.result.feedback;


import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>description: 错误反馈映射器 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class FeedbackMapper {
    
    private static volatile FeedbackMapper feedbackMapper;
    
    private final Map<Feedback, Integer> sequenceMap = new LinkedHashMap<>();
    
    private FeedbackMapper() {
    }
    
    public static FeedbackMapper getInstance() {
        if (ObjectUtils.isEmpty(feedbackMapper)) {
            synchronized (FeedbackMapper.class) {
                if (ObjectUtils.isEmpty(feedbackMapper)) {
                    feedbackMapper = new FeedbackMapper();
                }
            }
        }
        return feedbackMapper;
    }
    
    public static Integer get(Feedback feedback) {
        return getInstance().getSequence(feedback);
    }
    
    public void append(Map<Feedback, Integer> feedbackSequenceMap) {
        if (MapUtils.isNotEmpty(feedbackSequenceMap)) {
            this.sequenceMap.putAll(feedbackSequenceMap);
        }
    }
    
    private Integer getSequence(Feedback feedback) {
        return this.sequenceMap.get(feedback);
    }
}