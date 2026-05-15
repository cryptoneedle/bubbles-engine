package com.bubbles.common.result.feedback;

/**
 * <p>description: 错误反馈构建顺序 </p>
 * <p>
 * 注解@Order或者接口Ordered的作用是定义Spring IOC容器中Bean的执行顺序的优先级，而不是定义Bean的加载顺序，Bean的加载顺序不受@Order或Ordered接口的影响
 *
 * @author CryptoNeedle
 * @date 2024-12-12
 */
public interface FeedbackMapperBuilderOrdered {
    
    int STEP = 10;
    
    int COMMON = 0;
    
    // 10
    int CACHE = COMMON + STEP;
    
    // 20
    int CAPTCHA = CACHE + STEP;
    
    // 30
    int OAUTH2 = CAPTCHA + STEP;
    
    // 40
    int REST = OAUTH2 + STEP;
    
    // 50
    int MESSAGE = REST + STEP;
    
    // 60
    int ACCESS = MESSAGE + STEP;
}