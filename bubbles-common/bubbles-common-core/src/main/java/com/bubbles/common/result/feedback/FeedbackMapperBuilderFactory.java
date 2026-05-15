package com.bubbles.common.result.feedback;


import com.bubbles.common.result.feedback.builder.ClientFeedbackMapperBuilder;
import com.bubbles.common.result.feedback.builder.CustomizeFeedbackMapperBuilder;
import com.bubbles.common.result.feedback.builder.ServerFeedbackMapperBuilder;
import com.bubbles.common.result.feedback.builder.SuccessFeedbackMapperBuilder;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <p>description: 错误反馈映射构建工厂 </p>
 * <p/>
 * 错误反馈构建工厂、错误反馈构建器、错误反馈映射器全部采用单例模式
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class FeedbackMapperBuilderFactory {
    
    private static volatile FeedbackMapperBuilderFactory factory;
    
    private FeedbackMapperBuilderFactory() {
    }
    
    public static FeedbackMapperBuilderFactory getInstance() {
        if (ObjectUtils.isEmpty(factory)) {
            synchronized (FeedbackMapperBuilderFactory.class) {
                if (ObjectUtils.isEmpty(factory)) {
                    factory = new FeedbackMapperBuilderFactory();
                }
            }
        }
        return factory;
    }
    
    /** 2xx */
    public SuccessFeedbackMapperBuilder successBuilder() {
        return SuccessFeedbackMapperBuilder.getInstance();
    }
    
    /** 4xx */
    public ClientFeedbackMapperBuilder clientBuilder() {
        return ClientFeedbackMapperBuilder.getInstance();
    }
    
    /** 5xx */
    public ServerFeedbackMapperBuilder serverBuilder() {
        return ServerFeedbackMapperBuilder.getInstance();
    }
    
    /** customize */
    public CustomizeFeedbackMapperBuilder customizeBuilder() {
        return CustomizeFeedbackMapperBuilder.getInstance();
    }
    
    public FeedbackMapper build() {
        successBuilder().build();
        clientBuilder().build();
        serverBuilder().build();
        customizeBuilder().build();
        return FeedbackMapper.getInstance();
    }
}