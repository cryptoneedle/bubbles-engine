package com.bubbles.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * <p>description: 分页响应结果实体 </p>
 *
 * @author CryptoNeedle
 * @date 2024-12-05
 */
@Schema(name = "分页响应结果实体")
@Getter
public class PageResult<T> extends Result<T> {
    
    @Schema(name = "页码")
    private int pageNumber;
    
    @Schema(name = "每页数据条数")
    private int pageSize;
    
    @Schema(name = "数据总量")
    private int totalElements;
    
    @Schema(name = "总页数")
    private int totalPages;
    
    @Schema(name = "是否是第一页")
    private boolean isFirstPage;
    
    @Schema(name = "是否是最后一页")
    private boolean isLastPage;
}