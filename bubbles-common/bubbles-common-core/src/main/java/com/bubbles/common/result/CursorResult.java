package com.bubbles.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * <p>description: 游标响应结果实体 </p>
 * <p>
 * 应用场景：Elasticsearch支持使用滚动（scroll）API和Search After机制来实现高效的分页，这两种机制都基于游标的概念
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
@Schema(name = "游标响应结果实体")
@Getter
public class CursorResult<T> extends Result<T> {
    
    @Schema(name = "是否有更多数据")
    private boolean hasMore;
    
    @Schema(name = "数据游标")
    private long nextCursor;
}