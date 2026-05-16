package com.bubbles.common.exception.handler;


import com.bubbles.common.exception.EngineException;
import com.bubbles.common.exception.PlatformException;
import com.bubbles.common.exception.PlatformRuntimeException;
import com.bubbles.common.result.Result;
import com.bubbles.common.result.feedback.Feedback;
import com.bubbles.common.result.feedback.Feedbacks;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>description: 通用异常处理器 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-17
 */
@RestControllerAdvice
public class BaseExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(BaseExceptionHandler.class);
    
    private static final Map<String, Feedback> EXCEPTION_DICTIONARY = new HashMap<>() {{
        put("AccessDeniedException", Feedbacks.ACCESS_DENIED);
        put("BadSqlGrammarException", Feedbacks.BAD_SQL_GRAMMAR);
        put("BindException", Feedbacks.METHOD_ARGUMENT_NOT_VALID);
        put("CookieTheftException", Feedbacks.COOKIE_THEFT);
        put("DataIntegrityViolationException", Feedbacks.DATA_INTEGRITY_VIOLATION);
        put("HttpMediaTypeNotAcceptableException", Feedbacks.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE);
        put("HttpMessageNotReadableException", Feedbacks.HTTP_MESSAGE_NOT_READABLE_EXCEPTION);
        put("HttpRequestMethodNotSupportedException", Feedbacks.HTTP_REQUEST_METHOD_NOT_SUPPORTED);
        put("IllegalArgumentException", Feedbacks.ILLEGAL_ARGUMENT_EXCEPTION);
        put("InsufficientAuthenticationException", Feedbacks.ACCESS_DENIED);
        put("InvalidCookieException", Feedbacks.INVALID_COOKIE);
        put("IOException", Feedbacks.IO_EXCEPTION);
        put("MethodArgumentNotValidException", Feedbacks.METHOD_ARGUMENT_NOT_VALID);
        put("MissingServletRequestParameterException", Feedbacks.MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION);
        put("NoResourceFoundException", Feedbacks.NO_RESOURCE_FOUND_EXCEPTION);
        put("NullPointerException", Feedbacks.NULL_POINTER_EXCEPTION);
        put("ProviderNotFoundException", Feedbacks.PROVIDER_NOT_FOUND);
        put("RedisPipelineException", Feedbacks.PIPELINE_INVALID_COMMANDS);
        put("TypeMismatchException", Feedbacks.TYPE_MISMATCH_EXCEPTION);
        put("TransactionRollbackException", Feedbacks.TRANSACTION_ROLLBACK);
    }};
    
    public static Result<String> resolveException(Exception ex, String path) {
        
        log.trace("[Engine Common Core] ||- Global Exception Handler, Path : [{}], Exception：", path, ex);
        
        Result<String> result;
        if (ex instanceof EngineException exception) {
            result = exception.getResult();
        } else {
            String exceptionName = ex.getClass().getSimpleName();
            Feedback feedback = EXCEPTION_DICTIONARY.get(exceptionName);
            if (feedback == null) {
                log.warn("[Engine Common Core] ||- Global Exception Handler, Can not find the exception name with [{}] in dictionary", exceptionName);
            }
            
            result = Result.failure(feedback, exceptionName);
            result.detail(ex.getMessage());
            result.stackTrace(ex.getStackTrace());
        }
        
        result.path(path);
        
        log.error("[Engine Common Core] ||- Global Exception Handler, Error is : {}", result);
        ex.printStackTrace();
        return result;
    }
    
    @ExceptionHandler({Exception.class, PlatformException.class, PlatformRuntimeException.class})
    public static Result<String> exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        Result<String> result = resolveException(ex, request.getRequestURI());
        response.setStatus(result.getStatus());
        return result;
    }
}