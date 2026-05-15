package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 日期时间序列化/反序列化格式规则的专项测试。
 *
 * <p>对应以下配置规则：
 * <pre>
 * 序列化：
 *   LocalDateTime → yyyy-MM-dd HH:mm:ss
 *   LocalDate     → yyyy-MM-dd
 *   LocalTime     → HH:mm:ss
 *   Instant       → 标准 ISO-8601
 *   Duration      → 标准 ISO-8601 Duration
 *
 * 反序列化：
 *   yyyy-MM-dd HH:mm:ss → LocalDateTime
 *   yyyy-MM-dd          → LocalDate
 *   HH:mm:ss            → LocalTime
 *   标准 ISO-8601       → Instant
 *   标准 ISO-8601       → Duration
 * </pre>
 * </p>
 */
@DisplayName("日期时间格式化规则")
class JacksonDateTimeFormatRuleTest {

    // ==================== 序列化格式规则 ====================

    @Nested
    @DisplayName("序列化格式规则")
    class SerializationRules {

        @Test
        @DisplayName("LocalDateTime 序列化为 yyyy-MM-dd HH:mm:ss 格式")
        void serialize_localDateTime_yyyyMMddHHmmss() {
            LocalDateTime dateTime = LocalDateTime.of(2026, 4, 21, 10, 30, 0);
            String json = JacksonUtil.toJson(dateTime);
            assertNotNull(json);
            // 去除首尾引号后应为 "2026-04-21 10:30:00"（空格分隔，无 T）
            String value = json.replace("\"", "");
            assertEquals("2026-04-21 10:30:00", value,
                    "LocalDateTime 应序列化为 yyyy-MM-dd HH:mm:ss 格式");
        }

        @Test
        @DisplayName("LocalDate 序列化为 yyyy-MM-dd 格式")
        void serialize_localDate_yyyyMMdd() {
            LocalDate date = LocalDate.of(2026, 4, 21);
            String json = JacksonUtil.toJson(date);
            assertNotNull(json);
            String value = json.replace("\"", "");
            assertEquals("2026-04-21", value,
                    "LocalDate 应序列化为 yyyy-MM-dd 格式");
        }

        @Test
        @DisplayName("LocalTime 序列化为 HH:mm:ss 格式")
        void serialize_localTime_HHmmss() {
            LocalTime time = LocalTime.of(14, 30, 0);
            String json = JacksonUtil.toJson(time);
            assertNotNull(json);
            String value = json.replace("\"", "");
            assertEquals("14:30:00", value,
                    "LocalTime 应序列化为 HH:mm:ss 格式");
        }

        @Test
        @DisplayName("Instant 序列化输出标准格式")
        void serialize_instant_standardFormat() {
            Instant instant = Instant.parse("2026-04-21T10:30:00Z");
            String json = JacksonUtil.toJson(instant);
            assertNotNull(json);
            assertFalse(json.isEmpty());
            // Instant 包含时区信息 Z 后缀
            assertTrue(json.contains("Z") || json.contains("+00"),
                    "Instant 序列化应包含时区信息");
        }

        @Test
        @DisplayName("Duration 序列化输出标准 ISO 格式")
        void serialize_duration_standardFormat() {
            Duration duration = Duration.ofHours(2).plusMinutes(30);
            String json = JacksonUtil.toJson(duration);
            assertNotNull(json);
            assertFalse(json.isEmpty());
            // Duration 应序列化为 ISO-8601 格式（如 "PT2H30M"）
            assertTrue(json.contains("PT"),
                    "Duration 应序列化为 ISO-8601 格式");
        }
    }

    // ==================== 反序列化格式规则 ====================

    @Nested
    @DisplayName("反序列化格式规则")
    class DeserializationRules {

        @Test
        @DisplayName("yyyy-MM-dd HH:mm:ss 格式字符串反序列化为 LocalDateTime")
        void deserialize_localDateTime_yyyyMMddHHmmss() {
            String json = "\"2026-04-21 10:30:00\"";
            LocalDateTime result = JacksonUtil.fromJson(json, LocalDateTime.class);
            assertNotNull(result);
            assertEquals(LocalDateTime.of(2026, 4, 21, 10, 30, 0), result);
        }

        @Test
        @DisplayName("yyyy-MM-dd 格式字符串反序列化为 LocalDate")
        void deserialize_localDate_yyyyMMdd() {
            String json = "\"2026-04-21\"";
            LocalDate result = JacksonUtil.fromJson(json, LocalDate.class);
            assertNotNull(result);
            assertEquals(LocalDate.of(2026, 4, 21), result);
        }

        @Test
        @DisplayName("HH:mm:ss 格式字符串反序列化为 LocalTime")
        void deserialize_localTime_HHmmss() {
            String json = "\"14:30:00\"";
            LocalTime result = JacksonUtil.fromJson(json, LocalTime.class);
            assertNotNull(result);
            assertEquals(LocalTime.of(14, 30, 0), result);
        }

        @Test
        @DisplayName("标准 ISO-8601 格式字符串反序列化为 Instant")
        void deserialize_instant_standardFormat() {
            String json = "\"2026-04-21T10:30:00Z\"";
            Instant result = JacksonUtil.fromJson(json, Instant.class);
            assertNotNull(result);
            assertEquals(Instant.parse("2026-04-21T10:30:00Z"), result);
        }

        @Test
        @DisplayName("标准 ISO-8601 格式字符串反序列化为 Duration")
        void deserialize_duration_standardFormat() {
            String json = "\"PT2H30M\"";
            Duration result = JacksonUtil.fromJson(json, Duration.class);
            assertNotNull(result);
            assertEquals(Duration.ofHours(2).plusMinutes(30), result);
        }
    }

    // ==================== 往返一致性 ====================

    @Nested
    @DisplayName("往返一致性验证")
    class RoundTripConsistency {

        @Test
        @DisplayName("LocalDateTime 往返一致性")
        void roundTrip_localDateTime() {
            LocalDateTime original = LocalDateTime.of(2026, 12, 31, 23, 59, 59);
            String json = JacksonUtil.toJson(original);
            LocalDateTime restored = JacksonUtil.fromJson(json, LocalDateTime.class);
            assertEquals(original, restored);
        }

        @Test
        @DisplayName("LocalDate 往返一致性")
        void roundTrip_localDate() {
            LocalDate original = LocalDate.of(2000, 1, 1);
            String json = JacksonUtil.toJson(original);
            LocalDate restored = JacksonUtil.fromJson(json, LocalDate.class);
            assertEquals(original, restored);
        }

        @Test
        @DisplayName("LocalTime 往返一致性")
        void roundTrip_localTime() {
            LocalTime original = LocalTime.of(0, 0, 0);
            String json = JacksonUtil.toJson(original);
            LocalTime restored = JacksonUtil.fromJson(json, LocalTime.class);
            assertEquals(original, restored);
        }

        @Test
        @DisplayName("Instant 往返一致性")
        void roundTrip_instant() {
            Instant original = Instant.parse("2026-04-21T10:30:00Z");
            String json = JacksonUtil.toJson(original);
            Instant restored = JacksonUtil.fromJson(json, Instant.class);
            assertEquals(original, restored);
        }

        @Test
        @DisplayName("Duration 往返一致性")
        void roundTrip_duration() {
            Duration original = Duration.ofDays(1).plusHours(2).plusMinutes(30).plusSeconds(15);
            String json = JacksonUtil.toJson(original);
            Duration restored = JacksonUtil.fromJson(json, Duration.class);
            assertEquals(original, restored);
        }
    }

    // ==================== 格式精确性验证 ====================

    @Nested
    @DisplayName("格式精确性验证")
    class FormatPrecision {

        @Test
        @DisplayName("LocalDateTime 格式精确匹配（无多余空格/分隔符）")
        void localDateTime_formatExact() {
            LocalDateTime dateTime = LocalDateTime.of(2026, 1, 2, 3, 4, 5);
            String json = JacksonUtil.toJson(dateTime);
            assertNotNull(json);
            // 验证格式为精确的 "2026-01-02 03:04:05"
            String value = json.replace("\"", "");
            DateTimeFormatter expectedFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parsed = LocalDateTime.parse(value, expectedFormat);
            assertEquals(dateTime, parsed, "序列化结果应可被 yyyy-MM-dd HH:mm:ss 解析");
        }

        @Test
        @DisplayName("LocalDate 格式精确匹配（不含时间部分）")
        void localDate_formatExact() {
            LocalDate date = LocalDate.of(2026, 6, 15);
            String json = JacksonUtil.toJson(date);
            assertNotNull(json);
            String value = json.replace("\"", "");
            LocalDate parsed = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            assertEquals(date, parsed, "序列化结果应可被 ISO_LOCAL_DATE 解析");
        }

        @Test
        @DisplayName("LocalTime 格式精确匹配（不含日期部分）")
        void localTime_formatExact() {
            LocalTime time = LocalTime.of(9, 0, 0);
            String json = JacksonUtil.toJson(time);
            assertNotNull(json);
            String value = json.replace("\"", "");
            LocalTime parsed = LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
            assertEquals(time, parsed, "序列化结果应可被 ISO_LOCAL_TIME 解析");
        }
    }
}
