package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import com.bubbles.common.test.json.model.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} BlackbirdModule 模块的单元测试。
 * BlackbirdModule 基于 MethodHandle 实现高性能反射替代，验证其注册及功能正确性。
 */
@DisplayName("JacksonUtil - BlackbirdModule 模块")
class JacksonUtilBlackbirdModuleTest {
    
    // ==================== 模块注册验证 ====================
    
    @Nested
    @DisplayName("模块注册验证")
    class ModuleRegistration {
        
        @Test
        @DisplayName("JSON Mapper 已注册 BlackbirdModule")
        void blackbirdModuleRegistered() {
            ObjectMapper mapper = JacksonUtil.jsonMapper();
            boolean hasBlackbird = mapper.registeredModules().stream()
                                         .anyMatch(m -> m.getClass().getName().contains("BlackbirdModule"));
            assertTrue(hasBlackbird, "应注册 BlackbirdModule");
        }
        
        @Test
        @DisplayName("所有 Mapper 均已注册 BlackbirdModule")
        void allMappersHaveBlackbirdModule() {
            for (ObjectMapper mapper : List.of(
                    JacksonUtil.jsonMapper(),
                    JacksonUtil.yamlMapper(),
                    JacksonUtil.xmlMapper(),
                    JacksonUtil.csvMapper(),
                    JacksonUtil.cborMapper(),
                    JacksonUtil.smileMapper()
            )) {
                boolean hasBlackbird = mapper.registeredModules().stream()
                                             .anyMatch(m -> m.getClass().getName().contains("BlackbirdModule"));
                assertTrue(hasBlackbird, mapper.getClass().getSimpleName() + " 应注册 BlackbirdModule");
            }
        }
    }
    
    // ==================== 功能正确性验证 ====================
    
    @Nested
    @DisplayName("Blackbird 加速下的序列化/反序列化正确性")
    class FunctionalCorrectness {
        
        private static final SampleUser SAMPLE = new SampleUser(
                "Blackbird测试", 28, new BigDecimal("8888.88"),
                LocalDate.of(1998, 3, 10),
                LocalDateTime.of(2026, 4, 21, 15, 30, 0),
                List.of("fast"),
                Map.of("engine", "blackbird")
        );
        
        @Test
        @DisplayName("Blackbird 下对象序列化正确")
        void serialization_correct() {
            String json = JacksonUtil.toJson(SAMPLE);
            assertNotNull(json);
            assertTrue(json.contains("Blackbird测试"));
            assertTrue(json.contains("28"));
        }
        
        @Test
        @DisplayName("Blackbird 下对象反序列化正确")
        void deserialization_correct() {
            String json = JacksonUtil.toJson(SAMPLE);
            SampleUser restored = JacksonUtil.fromJson(json, SampleUser.class);
            assertNotNull(restored);
            assertEquals(SAMPLE.getName(), restored.getName());
            assertEquals(SAMPLE.getAge(), restored.getAge());
            assertEquals(SAMPLE.getBalance(), restored.getBalance());
        }
        
        @Test
        @DisplayName("Blackbird 下往返转换保持数据一致")
        void roundTrip_consistent() {
            String json = JacksonUtil.toJson(SAMPLE);
            SampleUser restored = JacksonUtil.fromJson(json, SampleUser.class);
            assertEquals(SAMPLE, restored);
        }
        
        @Test
        @DisplayName("Blackbird 下嵌套对象序列化正确")
        void nestedObject_correct() {
            Map<String, SampleUser> wrapper = Map.of("user", SAMPLE);
            String json = JacksonUtil.toJson(wrapper);
            assertNotNull(json);
            
            Map<String, SampleUser> restored = JacksonUtil.fromJson(json,
                                                                    new tools.jackson.core.type.TypeReference<>() {});
            assertNotNull(restored);
            assertEquals(SAMPLE.getName(), restored.get("user").getName());
        }
    }
    
    // ==================== 性能对比基准 ====================
    
    @Nested
    @DisplayName("性能对比 - Blackbird vs 纯反射")
    class PerformanceComparison {
        
        /**
         * 不使用 Blackbird 的纯净 JsonMapper，用于对比基准
         */
        private static final JsonMapper PURE_MAPPER = JsonMapper.builder().build();
        
        @Test
        @DisplayName("Blackbird Mapper 与纯反射 Mapper 结果一致")
        void blackbirdProducesSameResultAsPureReflection() {
            SampleUser user = new SampleUser(
                    "性能对比", 30, new BigDecimal("100.00"),
                    LocalDate.of(1995, 5, 20),
                    LocalDateTime.of(2026, 1, 1, 0, 0, 0),
                    List.of("benchmark"),
                    Map.of("type", "perf")
            );
            
            // 用 Blackbird 的 Mapper 序列化
            String jsonWithBlackbird = JacksonUtil.toJson(user);
            // 用纯反射 Mapper 序列化
            String jsonWithPureReflection = PURE_MAPPER.writeValueAsString(user);
            
            // 两者反序列化后应产生相同对象
            SampleUser fromBlackbird = JacksonUtil.fromJson(jsonWithBlackbird, SampleUser.class);
            SampleUser fromPure = PURE_MAPPER.readValue(jsonWithPureReflection, SampleUser.class);
            
            assertEquals(fromBlackbird.getName(), fromPure.getName());
            assertEquals(fromBlackbird.getAge(), fromPure.getAge());
        }
        
        @Test
        @DisplayName("批量序列化 Blackbird 不抛异常")
        void bulkSerialization_noException() {
            SampleUser user = new SampleUser("批量", 25, BigDecimal.TEN,
                                             LocalDate.now(), LocalDateTime.now(), null, null);
            // 快速执行 100 次确保 Blackbird 动态生成不出错
            for (int i = 0; i < 100; i++) {
                String json = JacksonUtil.toJson(user);
                assertNotNull(json);
            }
        }
    }
}
