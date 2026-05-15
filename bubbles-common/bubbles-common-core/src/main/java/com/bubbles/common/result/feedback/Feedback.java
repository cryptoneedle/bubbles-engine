package com.bubbles.common.result.feedback;


import cn.hutool.v7.core.lang.Assert;
import com.bubbles.common.result.feedback.definition.customize.CustomizeFeedback;
import com.bubbles.common.result.feedback.definition.http.HttpStatusFeedback;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * <p>description: 错误反馈实体 </p>
 * <p>
 * 错误反馈实体的状态码与Http的状态码关系绑定
 * 错误反馈实体分为以下两种：
 * <p>
 * 1.{@link HttpStatusFeedback} Http状态码错误反馈实体
 * 算法为 (Http状态码 * 100) + 索引编号
 * eg.40103 该账户已经被禁用
 * <p>
 * 2.{@link CustomizeFeedback} 自定义错误反馈实体
 * 算法为 (custom * 10000) + 索引编号
 * eg.60001 数据事务处理失败，数据回滚
 * 可以通过 {@link Feedbacks} 查看框架内定义的错误反馈实体
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
public class Feedback implements Serializable {
    
    /**
     * HTTP状态码
     */
    private final int status;
    
    /**
     * 错误信息
     */
    private final String message;
    
    /**
     * 第一位状态码
     * 出于可区分原始状态码和可拓展性的考虑，没有设置为简单的布尔值
     * 0-HTTP状态码
     * 6~9-自定义状态码
     */
    private final int custom;
    
    public Feedback(int status, String message, int custom) {
        Assert.checkBetween(custom, FeedbackConstants.NOT_CUSTOMIZED, 9);
        this.status = status;
        this.message = message;
        this.custom = custom;
    }
    
    public Feedback(int status, String message) {
        this(status, message, FeedbackConstants.NOT_CUSTOMIZED);
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public int getCustom() {
        return custom;
    }
    
    private boolean isCustom() {
        return custom != FeedbackConstants.NOT_CUSTOMIZED;
    }
    
    /**
     * @return eg.40100、60000
     */
    public int getSequence() {
        if (isCustom()) {
            return custom * 10000;
        } else {
            return status * 100;
        }
    }
    
    /**
     * @param index 目前是由构造顺序决定的index顺序
     * @return eg.40103、60001
     */
    public int getSequence(int index) {
        return getSequence() + index;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feedback feedback = (Feedback) o;
        return Objects.equal(message, feedback.message);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}