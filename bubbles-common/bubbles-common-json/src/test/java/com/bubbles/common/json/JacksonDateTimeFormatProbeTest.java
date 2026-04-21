package com.bubbles.common.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Map;

/**
 * 探测性测试 - 仅打印当前序列化格式，用于确定测试数据
 */
@DisplayName("格式探测")
class JacksonDateTimeFormatProbeTest {
    
    @Test
    @DisplayName("探测日期时间格式")
    void probeFormats() {
        LocalDateTime ldt = LocalDateTime.of(2026, 4, 21, 10, 30, 0);
        LocalDate ld = LocalDate.of(2026, 4, 21);
        LocalTime lt = LocalTime.of(14, 30, 0);
        Instant inst = Instant.parse("2026-04-21T10:30:00Z");
        Duration dur = Duration.ofHours(2).plusMinutes(30);
        
        System.out.println("LocalDateTime => " + JacksonUtil.toJson(Map.of("v", ldt)));
        System.out.println("LocalDate    => " + JacksonUtil.toJson(Map.of("v", ld)));
        System.out.println("LocalTime    => " + JacksonUtil.toJson(Map.of("v", lt)));
        System.out.println("Instant      => " + JacksonUtil.toJson(Map.of("v", inst)));
        System.out.println("Duration     => " + JacksonUtil.toJson(Map.of("v", dur)));
        
        // 直接序列化（非 Map 包装）
        System.out.println("LDT direct   => " + JacksonUtil.toJson(ldt));
        System.out.println("LD direct    => " + JacksonUtil.toJson(ld));
        System.out.println("LT direct    => " + JacksonUtil.toJson(lt));
    }
}
