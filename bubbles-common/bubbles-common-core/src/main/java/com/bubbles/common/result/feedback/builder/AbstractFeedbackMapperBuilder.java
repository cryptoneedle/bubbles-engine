package com.bubbles.common.result.feedback.builder;


import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapperBuilderFactory;

import java.util.Map;

/**
 * <p>description: 抽象错误反馈映射构建器 </p>
 * <p/>
 * 实现类应全部采用基于双重检查锁的单例模式
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public abstract class AbstractFeedbackMapperBuilder {
    
    /**
     * 添加错误反馈实体到对应类型的Map中
     */
    protected void append(Map<Feedback, Integer> feedbackMap, Feedback... feedbacks) {
        for (Feedback feedback : feedbacks) {
            feedbackMap.put(feedback, feedback.getSequence(feedbackMap.size()));
        }
    }
    
    /**
     * @return 返回错误反馈映射构建工厂(用于链式操作)
     */
    public FeedbackMapperBuilderFactory end() {
        return FeedbackMapperBuilderFactory.getInstance();
    }
    
    /**
     * 构建错误反馈映射器中的错误反馈实体
     */
    public abstract void build();
}