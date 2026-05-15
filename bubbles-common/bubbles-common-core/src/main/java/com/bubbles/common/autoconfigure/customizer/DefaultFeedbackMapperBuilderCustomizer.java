package com.bubbles.common.autoconfigure.customizer;


import com.bubbles.common.result.feedback.FeedBackMapperBuilderFactoryCustomizer;
import com.bubbles.common.result.feedback.FeedbackMapperBuilderFactory;
import com.bubbles.common.result.feedback.FeedbackMapperBuilderOrdered;
import com.bubbles.common.result.feedback.Feedbacks;
import org.springframework.core.Ordered;

import static com.bubbles.common.result.feedback.Feedbacks.*;

/**
 * <p>description: 默认错误反馈配置 </p>
 * 通用内容参考 {@link Feedbacks}
 *
 * @author CryptoNeedle
 * @date 2024-12-12
 */
public class DefaultFeedbackMapperBuilderCustomizer implements FeedBackMapperBuilderFactoryCustomizer, Ordered {
    
    @Override
    public void customize(FeedbackMapperBuilderFactory factory) {
        factory
                // 2xx
                .successBuilder()
                .ok(OK)
                .noContent(NO_CONTENT)
                .end()
                
                // 4xx
                .clientBuilder()
                .unauthorized(UNAUTHORIZED,
                              ACCESS_DENIED,
                              ACCOUNT_DISABLED,
                              ACCOUNT_ENDPOINT_LIMITED,
                              ACCOUNT_EXPIRED,
                              ACCOUNT_LOCKED,
                              BAD_CREDENTIALS,
                              CREDENTIALS_EXPIRED,
                              INVALID_CLIENT,
                              INVALID_TOKEN,
                              INVALID_GRANT,
                              UNAUTHORIZED_CLIENT,
                              USERNAME_NOT_FOUND,
                              SESSION_EXPIRED)
                .forbidden(FORBIDDEN,
                           INSUFFICIENT_SCOPE,
                           SQL_INJECTION_REQUEST)
                .notFound(NO_RESOURCE_FOUND_EXCEPTION)
                .methodNotAllowed(METHOD_NOT_ALLOWED,
                                  HTTP_REQUEST_METHOD_NOT_SUPPORTED)
                .notAcceptable(NOT_ACCEPTABLE,
                               UNSUPPORTED_GRANT_TYPE,
                               UNSUPPORTED_RESPONSE_TYPE,
                               UNSUPPORTED_TOKEN_TYPE)
                .preconditionFailed(PRECONDITION_FAILED,
                                    INVALID_REDIRECT_URI,
                                    INVALID_REQUEST,
                                    INVALID_SCOPE,
                                    METHOD_ARGUMENT_NOT_VALID)
                .unsupportedMediaType(UNSUPPORTED_MEDIA_TYPE,
                                      HTTP_MEDIA_TYPE_NOT_ACCEPTABLE)
                .end()
                
                // 5xx
                .serverBuilder()
                .internalServerError(INTERNAL_SERVER_ERROR,
                                     SERVER_ERROR,
                                     HTTP_MESSAGE_NOT_READABLE_EXCEPTION,
                                     ILLEGAL_ARGUMENT_EXCEPTION,
                                     IO_EXCEPTION,
                                     MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION,
                                     NULL_POINTER_EXCEPTION,
                                     TYPE_MISMATCH_EXCEPTION,
                                     BORROW_OBJECT_FROM_POOL_ERROR_EXCEPTION)
                .notImplemented(NOT_IMPLEMENTED,
                                DISCOVERED_UNRECORDED_ERROR_EXCEPTION,
                                PROPERTY_VALUE_IS_NOT_SET_EXCEPTION,
                                URL_FORMAT_INCORRECT_EXCEPTION,
                                ILLEGAL_SYMMETRIC_KEY)
                .serviceUnavailable(SERVICE_UNAVAILABLE,
                                    COOKIE_THEFT,
                                    INVALID_COOKIE,
                                    PROVIDER_NOT_FOUND,
                                    TEMPORARILY_UNAVAILABLE,
                                    SEARCH_IP_LOCATION,
                                    OPEN_API_REQUEST_FAILURE)
                .end()
                
                // 自定义
                .customizeBuilder()
                .customize(TRANSACTION_ROLLBACK,
                           BAD_SQL_GRAMMAR,
                           DATA_INTEGRITY_VIOLATION)
                .customize(PIPELINE_INVALID_COMMANDS)
                .end();
    }
    
    @Override
    public int getOrder() {
        return FeedbackMapperBuilderOrdered.COMMON;
    }
}