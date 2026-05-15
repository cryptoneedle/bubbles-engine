package com.bubbles.common.result;

import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;

/**
 * <p>description: 响应错误实体 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
@Schema(name = "响应错误实体")
@Getter
public class Error implements Serializable {
    
    @Schema(name = "错误字段")
    private String field;
    
    @Schema(name = "错误编码")
    private String code;
    
    @Schema(name = "错误信息")
    private String message;
    
    @Schema(name = "错误详情", type = "string")
    private String detail;
    
    @Schema(name = "错误指引")
    private String recommend;
    
    @Schema(name = "错误堆栈信息")
    private StackTraceElement[] stackTrace;
    
    /**
     * Chain
     */
    public Error field(String field) {
        this.field = field;
        return this;
    }
    
    public Error code(String code) {
        this.code = code;
        return this;
    }
    
    public Error message(String message) {
        this.message = message;
        return this;
    }
    
    public Error detail(String detail) {
        this.detail = detail;
        return this;
    }
    
    public Error recommend(String recommend) {
        this.recommend = recommend;
        return this;
    }
    
    public Error stackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("field", field)
                          .add("code", code)
                          .add("message", message)
                          .add("detail", detail)
                          .add("recommend", recommend)
                          //.add("stackTrace", stackTrace)
                          .toString();
    }
}