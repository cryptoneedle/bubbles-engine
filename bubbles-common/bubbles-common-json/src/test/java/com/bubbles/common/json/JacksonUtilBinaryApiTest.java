package com.bubbles.common.json;

import com.bubbles.common.json.model.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} CBOR / Smile 二进制格式 Mapper 的 API 单元测试。
 */
@DisplayName("JacksonUtil - CBOR / Smile Mapper API")
class JacksonUtilBinaryApiTest {
    
    private static final SampleUser SAMPLE = new SampleUser(
            "二进制测试", 35, new BigDecimal("1234.56"),
            LocalDate.of(1991, 3, 15),
            LocalDateTime.of(2026, 4, 21, 12, 0, 0),
            List.of("binary"),
            Map.of("format", "cbor")
    );
    
    // ==================== CBOR ====================
    
    @Nested
    @DisplayName("CBOR - 二进制格式序列化/反序列化")
    class Cbor {
        
        @Test
        @DisplayName("对象 -> CBOR 字节")
        void toCbor_normal() {
            byte[] data = JacksonUtil.toCbor(SAMPLE);
            assertNotNull(data);
            assertTrue(data.length > 0);
        }
        
        @Test
        @DisplayName("CBOR 字节 -> 对象")
        void fromCbor_normal() {
            byte[] data = JacksonUtil.toCbor(SAMPLE);
            SampleUser restored = JacksonUtil.fromCbor(data, SampleUser.class);
            assertNotNull(restored);
            assertEquals(SAMPLE.getName(), restored.getName());
            assertEquals(SAMPLE.getAge(), restored.getAge());
        }
        
        @Test
        @DisplayName("CBOR 往返转换保持数据一致")
        void toCbor_roundTrip() {
            byte[] data = JacksonUtil.toCbor(SAMPLE);
            SampleUser restored = JacksonUtil.fromCbor(data, SampleUser.class);
            assertEquals(SAMPLE, restored);
        }
        
        @Test
        @DisplayName("null 输入序列化返回 null")
        void toCbor_nullInput() {
            assertNull(JacksonUtil.toCbor(null));
        }
        
        @Test
        @DisplayName("null 字节数组反序列化返回 null")
        void fromCbor_nullInput() {
            assertNull(JacksonUtil.fromCbor(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字节数组反序列化返回 null")
        void fromCbor_emptyInput() {
            assertNull(JacksonUtil.fromCbor(new byte[0], SampleUser.class));
        }
    }
    
    // ==================== Smile ====================
    
    @Nested
    @DisplayName("Smile - 二进制 JSON 格式序列化/反序列化")
    class Smile {
        
        @Test
        @DisplayName("对象 -> Smile 字节")
        void toSmile_normal() {
            byte[] data = JacksonUtil.toSmile(SAMPLE);
            assertNotNull(data);
            assertTrue(data.length > 0);
        }
        
        @Test
        @DisplayName("Smile 字节 -> 对象")
        void fromSmile_normal() {
            byte[] data = JacksonUtil.toSmile(SAMPLE);
            SampleUser restored = JacksonUtil.fromSmile(data, SampleUser.class);
            assertNotNull(restored);
            assertEquals(SAMPLE.getName(), restored.getName());
            assertEquals(SAMPLE.getAge(), restored.getAge());
        }
        
        @Test
        @DisplayName("Smile 往返转换保持数据一致")
        void toSmile_roundTrip() {
            byte[] data = JacksonUtil.toSmile(SAMPLE);
            SampleUser restored = JacksonUtil.fromSmile(data, SampleUser.class);
            assertEquals(SAMPLE, restored);
        }
        
        @Test
        @DisplayName("null 输入序列化返回 null")
        void toSmile_nullInput() {
            assertNull(JacksonUtil.toSmile(null));
        }
        
        @Test
        @DisplayName("null 字节数组反序列化返回 null")
        void fromSmile_nullInput() {
            assertNull(JacksonUtil.fromSmile(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字节数组反序列化返回 null")
        void fromSmile_emptyInput() {
            assertNull(JacksonUtil.fromSmile(new byte[0], SampleUser.class));
        }
    }
    
    // ==================== CBOR 与 Smile 互比 ====================
    
    @Nested
    @DisplayName("CBOR 与 Smile 格式对比")
    class BinaryComparison {
        
        @Test
        @DisplayName("同一对象 CBOR 和 Smile 均可正确还原")
        void bothFormatsProduceSameObject() {
            byte[] cborData = JacksonUtil.toCbor(SAMPLE);
            byte[] smileData = JacksonUtil.toSmile(SAMPLE);
            
            SampleUser fromCbor = JacksonUtil.fromCbor(cborData, SampleUser.class);
            SampleUser fromSmile = JacksonUtil.fromSmile(smileData, SampleUser.class);
            
            assertEquals(fromCbor, fromSmile);
        }
    }
}
