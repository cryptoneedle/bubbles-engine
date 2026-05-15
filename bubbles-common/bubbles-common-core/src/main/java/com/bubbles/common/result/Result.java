package com.bubbles.common.result;

import cn.hutool.v7.http.meta.HttpStatus;
import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.FeedbackMapper;
import com.bubbles.common.result.feedback.Feedbacks;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>description: 响应结果实体 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
@Schema(name = "响应结果实体")
@Getter
public class Result<T> implements Serializable {
    
    @Schema(name = "响应时间戳", pattern = ResultConstants.DATE_TIME_MS_PATTERN)
    @JsonFormat(pattern = ResultConstants.DATE_TIME_MS_PATTERN)
    private final LocalDateTime timestamp = LocalDateTime.now();
    
    @Schema(name = "响应错误实体")
    private Error error;
    
    @Schema(name = "请求路径")
    private String path;
    
    @Schema(name = "链路追踪TraceId")
    private String traceId;
    
    @Schema(name = "HTTP状态码")
    private int status;
    
    @Schema(name = "响应编码")
    private int code;
    
    @Schema(name = "响应信息")
    private String message;
    
    @Schema(name = "响应数据")
    private T data;
    
    /**
     * Constructor
     */
    public Result() {
    }
    
    private static <T> Result<T> create(String message,
                                        int code,
                                        int status,
                                        T data,
                                        String detail,
                                        StackTraceElement[] stackTrace) {
        return create(message, code, status, data)
                .error(new Error())
                .detail(StringUtils.isNotBlank(detail) ? detail : null)
                .stackTrace(ArrayUtils.isNotEmpty(stackTrace) ? stackTrace : null);
    }
    
    private static <T> Result<T> create(String message, int code, int status, T data) {
        return new Result<T>()
                .message(StringUtils.isNotBlank(message) ? message : null)
                .code(code)
                .status(status)
                .data(ObjectUtils.isNotEmpty(data) ? data : null);
    }
    
    /**
     * Success
     */
    
    public static <T> Result<T> success(String message, int code, int status, T data) {
        return create(message, code, status, data);
    }
    
    public static <T> Result<T> success(String message, int code, T data) {
        return success(message, code, HttpStatus.HTTP_OK, data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return success(message, Feedbacks.OK.getSequence(), data);
    }
    
    public static <T> Result<T> success(String message) {
        return success(message, null);
    }
    
    public static <T> Result<T> success(T data) {
        return success(ResultConstants.SUCCESS_MASSAGE, data);
    }
    
    public static <T> Result<T> success() {
        return success(ResultConstants.SUCCESS_MASSAGE);
    }
    
    /**
     * Failure
     */
    
    public static <T> Result<T> failure(String message,
                                        int code,
                                        int status,
                                        T data,
                                        String detail,
                                        StackTraceElement[] stackTrace) {
        return create(message, code, status, data, detail, stackTrace);
    }
    
    public static <T> Result<T> failure(String message, int code, int status, T data, String detail) {
        return failure(message, code, status, data, detail, null);
    }
    
    public static <T> Result<T> failure(String message, int code, int status, T data) {
        return failure(message, code, status, data, message);
    }
    
    public static <T> Result<T> failure(String message, int code, T data, String detail) {
        return failure(message, code, HttpStatus.HTTP_INTERNAL_ERROR, data, detail);
    }
    
    public static <T> Result<T> failure(String message, int code, T data) {
        return failure(message, code, data, message);
    }
    
    public static <T> Result<T> failure(String message, T data) {
        return failure(message, Feedbacks.INTERNAL_SERVER_ERROR.getSequence(), data);
    }
    
    public static <T> Result<T> failure(String message) {
        return failure(message, null);
    }
    
    public static <T> Result<T> failure() {
        return failure(ResultConstants.FAILURE_MASSAGE);
    }
    
    public static <T> Result<T> failure(Feedback feedback, T data) {
        Feedback result = ObjectUtils.isNotEmpty(feedback) ? feedback : Feedbacks.DISCOVERED_UNRECORDED_ERROR_EXCEPTION;
        Integer code = FeedbackMapper.get(result);
        
        // 方案1：如果遇到未识别的错误反馈，只有code变为501，其余信息保留
        //return failure(feedback.getMessage(), code, feedback.getStatus(), data);
        
        // 方案2：如果遇到未识别的错误反馈，吞掉原有信息，优先要求处理未识别反馈
        return failure(result.getMessage(), code, result.getStatus(), data);
    }
    
    public static <T> Result<T> failure(Feedback feedback) {
        return failure(feedback, null);
    }
    
    /**
     * Empty
     */
    
    public static <T> Result<T> empty(String message, int code, int status) {
        return create(message, code, status, null, null, null);
    }
    
    public static <T> Result<T> empty(String message, int code) {
        return empty(message, code, Feedbacks.OK.getStatus());
    }
    
    public static <T> Result<T> empty(Feedback feedback) {
        Integer code = FeedbackMapper.get(feedback);
        return empty(feedback.getMessage(), code, feedback.getStatus());
    }
    
    public static <T> Result<T> empty(String message) {
        return empty(message, Feedbacks.OK.getSequence());
    }
    
    public static <T> Result<T> empty() {
        return empty(ResultConstants.EMPTY_MESSAGE);
    }
    
    /**
     * Chain
     */
    public Result<T> path(String path) {
        this.path = path;
        return this;
    }
    
    public Result<T> error(Error error) {
        this.error = error;
        return this;
    }
    
    public Result<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    public Result<T> status(int status) {
        this.status = status;
        return this;
    }
    
    public Result<T> code(int code) {
        this.code = code;
        return this;
    }
    
    public Result<T> message(String message) {
        this.message = message;
        return this;
    }
    
    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
    
    public Result<T> type(Feedback feedback) {
        this.code(FeedbackMapper.get(feedback))
            .message(feedback.getMessage())
            .status(feedback.getStatus());
        return this;
    }
    
    public Result<T> stackTrace(StackTraceElement[] stackTrace) {
        if (this.error == null) {
            error = new Error();
        }
        this.error.stackTrace(stackTrace);
        return this;
    }
    
    public Result<T> detail(String detail) {
        if (this.error == null) {
            error = new Error();
        }
        this.error.detail(detail);
        return this;
    }
    
    public Result<T> validation(String message, String code, String field) {
        if (this.error == null) {
            error = new Error();
        }
        this.error.message(message)
                  .code(code)
                  .field(field);
        return this;
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("code", code)
                          .add("message", message)
                          .add("path", path)
                          .add("data", data)
                          .add("status", status)
                          .add("timestamp", timestamp)
                          .add("error", error)
                          .toString();
    }
}