package com.bubbles.common.result.feedback.definition.customize;


import cn.hutool.v7.http.meta.HttpStatus;
import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.FeedbackMapperBuilderFactory;
import com.bubbles.common.result.feedback.builder.CustomizeFeedbackMapperBuilder;

/**
 * <p>description: 自定义错误反馈实体 </p>
 * <p/>
 * 占用Http状态码未使用的6xx~9XX段
 * 在框架内部和外部构建，主要用于扩展自定义的错误反馈实体
 * 若在外部构建，可通过 {@link FeedbackMapperBuilderFactory} 或 {@link CustomizeFeedbackMapperBuilder} 集成到 {@link FeedbackMapper#sequenceMap} 中
 *
 * @author CryptoNeedle
 * @date 2024-12-11
 */
public class CustomizeFeedback extends Feedback {
    
    public CustomizeFeedback(String value, int custom) {
        super(HttpStatus.HTTP_INTERNAL_ERROR, value, custom);
    }
}