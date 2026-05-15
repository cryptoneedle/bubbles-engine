package com.bubbles.common.result.feedback;

/**
 * <p>description: 自定义错误反馈回调接口 </p>
 * <p>
 * 实现了该接口的Bean，可以在自动配置阶段通过 {@link FeedbackMapperBuilderFactory} 进一步扩展错误码
 *
 * @author CryptoNeedle
 * @date 2024-12-12
 */
@FunctionalInterface
public interface FeedBackMapperBuilderFactoryCustomizer {
    
    void customize(FeedbackMapperBuilderFactory factory);
}