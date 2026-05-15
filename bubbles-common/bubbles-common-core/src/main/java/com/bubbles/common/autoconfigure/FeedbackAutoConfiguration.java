package com.bubbles.common.autoconfigure;


import com.bubbles.common.autoconfigure.customizer.DefaultFeedbackMapperBuilderCustomizer;
import com.bubbles.common.exception.handler.BaseExceptionHandler;
import com.bubbles.common.result.feedback.FeedBackMapperBuilderFactoryCustomizer;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.FeedbackMapperBuilderFactory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * <p>description: 错误反馈自动配置 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-13
 */
@AutoConfiguration
@Import({BaseExceptionHandler.class})
public class FeedbackAutoConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(FeedbackAutoConfiguration.class);
    
    @PostConstruct
    public void postConstruct() {
        log.info("[Engine Common Starter] ||- Auto Configure Module [Feedback]");
    }
    
    @Bean
    public FeedBackMapperBuilderFactoryCustomizer commonFeedbackMapperBuilderCustomizer() {
        FeedBackMapperBuilderFactoryCustomizer customizer = new DefaultFeedbackMapperBuilderCustomizer();
        log.info("[Engine Common Starter] ||- Auto Configure Strategy [Common Feedback Mapper Builder Customizer]");
        return customizer;
    }
    
    @Bean
    public FeedbackMapperBuilderFactory feedbackMapperBuilderFactory(List<FeedBackMapperBuilderFactoryCustomizer> customizers) {
        FeedbackMapperBuilderFactory factory = FeedbackMapperBuilderFactory.getInstance();
        for (FeedBackMapperBuilderFactoryCustomizer customizer : customizers) {
            customizer.customize(factory);
        }
        log.info("[Engine Common Starter] ||- Auto Configure Bean [Feedback Mapper Builder Factory]");
        return factory;
    }
    
    @Bean
    public FeedbackMapper feedbackMapper(FeedbackMapperBuilderFactory factory) {
        FeedbackMapper mapper = factory.build();
        log.info("[Engine Common Starter] ||- Auto Configure Bean [Feedback Mapper]");
        return mapper;
    }
}