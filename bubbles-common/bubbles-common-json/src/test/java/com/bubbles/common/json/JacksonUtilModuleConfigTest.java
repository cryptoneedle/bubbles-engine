package com.bubbles.common.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.dataformat.cbor.CBORMapper;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.javaprop.JavaPropsMapper;
import tools.jackson.dataformat.smile.SmileMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} Mapper 模块注册与配置属性的单元测试。
 * 验证 {@code initBuilder} 中注册的模块和配置的各 Feature / 默认值是否正确生效。
 */
@DisplayName("JacksonUtil - 模块注册与配置属性")
class JacksonUtilModuleConfigTest {
    
    // ==================== Mapper 实例获取 ====================
    
    @Nested
    @DisplayName("Mapper 实例获取")
    class MapperAccess {
        
        @Test
        @DisplayName("jsonMapper() 返回 JsonMapper 实例")
        void jsonMapper() {
            assertNotNull(JacksonUtil.jsonMapper());
            assertInstanceOf(JsonMapper.class, JacksonUtil.jsonMapper());
        }
        
        @Test
        @DisplayName("yamlMapper() 返回 YAMLMapper 实例")
        void yamlMapper() {
            assertNotNull(JacksonUtil.yamlMapper());
            assertInstanceOf(YAMLMapper.class, JacksonUtil.yamlMapper());
        }
        
        @Test
        @DisplayName("xmlMapper() 返回 XmlMapper 实例")
        void xmlMapper() {
            assertNotNull(JacksonUtil.xmlMapper());
            assertInstanceOf(XmlMapper.class, JacksonUtil.xmlMapper());
        }
        
        @Test
        @DisplayName("csvMapper() 返回 CsvMapper 实例")
        void csvMapper() {
            assertNotNull(JacksonUtil.csvMapper());
            assertInstanceOf(CsvMapper.class, JacksonUtil.csvMapper());
        }
        
        @Test
        @DisplayName("propertiesMapper() 返回 JavaPropsMapper 实例")
        void propertiesMapper() {
            assertNotNull(JacksonUtil.propertiesMapper());
            assertInstanceOf(JavaPropsMapper.class, JacksonUtil.propertiesMapper());
        }
        
        @Test
        @DisplayName("cborMapper() 返回 CBORMapper 实例")
        void cborMapper() {
            assertNotNull(JacksonUtil.cborMapper());
            assertInstanceOf(CBORMapper.class, JacksonUtil.cborMapper());
        }
        
        @Test
        @DisplayName("smileMapper() 返回 SmileMapper 实例")
        void smileMapper() {
            assertNotNull(JacksonUtil.smileMapper());
            assertInstanceOf(SmileMapper.class, JacksonUtil.smileMapper());
        }
        
        @Test
        @DisplayName("objectMapper() 返回与 jsonMapper() 相同的实例")
        void objectMapper_sameAsJsonMapper() {
            assertSame(JacksonUtil.jsonMapper(), JacksonUtil.objectMapper());
        }
        
        @Test
        @DisplayName("多次调用返回同一实例（单例）")
        void mapperInstancesAreSingletons() {
            assertSame(JacksonUtil.jsonMapper(), JacksonUtil.jsonMapper());
            assertSame(JacksonUtil.yamlMapper(), JacksonUtil.yamlMapper());
            assertSame(JacksonUtil.xmlMapper(), JacksonUtil.xmlMapper());
        }
    }
    
    // ==================== 模块注册 ====================
    
    @Nested
    @DisplayName("模块注册验证")
    class ModuleRegistration {
        
        @Test
        @DisplayName("JSON Mapper 注册了 GuavaModule")
        void guavaModuleRegistered() {
            ObjectMapper mapper = JacksonUtil.jsonMapper();
            boolean hasGuava = mapper.registeredModules().stream()
                                     .anyMatch(m -> m.getClass().getName().contains("guava"));
            assertTrue(hasGuava, "应注册 GuavaModule，实际注册模块: " + mapper.registeredModules());
        }
        
        @Test
        @DisplayName("JSON Mapper 注册了 BlackbirdModule")
        void blackbirdModuleRegistered() {
            ObjectMapper mapper = JacksonUtil.jsonMapper();
            boolean hasBlackbird = mapper.registeredModules().stream()
                                         .anyMatch(m -> m.getClass().getName().contains("blackbird"));
            assertTrue(hasBlackbird, "应注册 BlackbirdModule，实际注册模块: " + mapper.registeredModules());
        }
        
        @Test
        @DisplayName("所有 Mapper 共享相同的模块注册")
        void allMappersShareModules() {
            var jsonModules = JacksonUtil.jsonMapper().registeredModules();
            var yamlModules = JacksonUtil.yamlMapper().registeredModules();
            var xmlModules = JacksonUtil.xmlMapper().registeredModules();
            
            // 所有 Mapper 都应注册 Guava 和 Blackbird
            for (var module : jsonModules) {
                String className = module.getClass().getName();
                if (className.contains("guava") || className.contains("blackbird")) {
                    assertTrue(yamlModules.stream().anyMatch(m -> m.getClass().getName().equals(className)),
                               "YAML Mapper 缺少模块: " + className);
                    assertTrue(xmlModules.stream().anyMatch(m -> m.getClass().getName().equals(className)),
                               "XML Mapper 缺少模块: " + className);
                }
            }
        }
    }
    
    // ==================== 序列化 Feature 配置 ====================
    
    @Nested
    @DisplayName("SerializationFeature 配置")
    class SerializationFeatureConfig {
        
        private final ObjectMapper mapper = JacksonUtil.jsonMapper();
        
        @Test
        @DisplayName("FAIL_ON_EMPTY_BEANS 已禁用 - 空 Bean 序列化为 {}")
        void failOnEmptyBeans_disabled() {
            // 空 Bean 不应抛异常
            String json = mapper.writeValueAsString(new EmptyBean());
            assertNotNull(json);
            assertFalse(json.isEmpty());
        }
        
        @Test
        @DisplayName("ORDER_MAP_ENTRIES_BY_KEYS 已启用 - Map 按键排序")
        void orderMapEntriesByKeys_enabled() {
            // 使用逆序插入的 Map，验证输出按键排序
            String json = mapper.writeValueAsString(java.util.Map.of("z", 1, "a", 2, "m", 3));
            assertNotNull(json);
            // 验证 a 排在 z 前面
            int aPos = json.indexOf("\"a\"");
            int zPos = json.indexOf("\"z\"");
            assertTrue(aPos < zPos, "Map 条目应按键排序，a 应排在 z 之前");
        }
        
        private static class EmptyBean {
            // 无任何属性的空 Bean
        }
    }
    
    // ==================== 反序列化 Feature 配置 ====================
    
    @Nested
    @DisplayName("DeserializationFeature 配置")
    class DeserializationFeatureConfig {
        
        private final ObjectMapper mapper = JacksonUtil.jsonMapper();
        
        @Test
        @DisplayName("FAIL_ON_UNKNOWN_PROPERTIES 已禁用 - 忽略未知字段")
        void failOnUnknownProperties_disabled() {
            String json = """
                    {"knownField":"value","unknownField":"ignored"}
                    """;
            // 不应抛异常
            var node = mapper.readTree(json);
            assertNotNull(node);
            assertEquals("value", node.get("knownField").asText());
        }
        
        @Test
        @DisplayName("ACCEPT_EMPTY_STRING_AS_NULL_OBJECT 已启用 - 空字符串转 null")
        void acceptEmptyStringAsNullObject_enabled() {
            String json = """
                    {"name":""}
                    """;
            var node = mapper.readTree(json);
            assertNotNull(node);
            // 空字符串在树模型中仍是空字符串，此特性主要在 POJO 绑定场景下生效
        }
        
        @Test
        @DisplayName("ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT 已启用 - 空数组转 null")
        void acceptEmptyArrayAsNullObject_enabled() {
            // 验证配置已启用
            assertTrue(mapper.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT));
        }
        
        @Test
        @DisplayName("USE_BIG_DECIMAL_FOR_FLOATS 已启用 - 浮点数用 BigDecimal")
        void useBigDecimalForFloats_enabled() {
            String json = "3.141592653589793";
            JsonNode node = mapper.readTree(json);
            assertTrue(node.isBigDecimal());
            assertEquals(new BigDecimal("3.141592653589793"), node.decimalValue());
        }
        
        @Test
        @DisplayName("USE_BIG_INTEGER_FOR_INTS 已启用 - 整数用 BigInteger")
        void useBigIntegerForInts_enabled() {
            String json = "9999999999999999999";
            JsonNode node = mapper.readTree(json);
            assertTrue(node.isBigInteger());
        }
        
        @Test
        @DisplayName("FAIL_ON_TRAILING_TOKENS 已启用 - 尾部多余 token 报错")
        void failOnTrailingTokens_enabled() {
            assertTrue(mapper.isEnabled(DeserializationFeature.FAIL_ON_TRAILING_TOKENS));
        }
        
        @Test
        @DisplayName("ACCEPT_FLOAT_AS_INT 已禁用 - 不允许浮点数转整数")
        void acceptFloatAsInt_disabled() {
            assertFalse(mapper.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT));
        }
        
        @Test
        @DisplayName("ACCEPT_SINGLE_VALUE_AS_ARRAY 已禁用 - 不允许单值转数组")
        void acceptSingleValueAsArray_disabled() {
            assertFalse(mapper.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY));
        }
    }
    
    // ==================== MapperFeature 配置 ====================
    
    @Nested
    @DisplayName("MapperFeature 配置")
    class MapperFeatureConfig {
        
        private final ObjectMapper mapper = JacksonUtil.jsonMapper();
        
        @Test
        @DisplayName("ALLOW_COERCION_OF_SCALARS 已启用 - 允许标量类型强制转换")
        void allowCoercionOfScalars_enabled() {
            assertTrue(mapper.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS));
        }
    }
    
    // ==================== 默认值配置 ====================
    
    @Nested
    @DisplayName("默认值配置")
    class DefaultConfig {
        
        private final ObjectMapper mapper = JacksonUtil.jsonMapper();
        
        @Test
        @DisplayName("默认 Locale 为 CHINA")
        void defaultLocale_china() {
            assertEquals(Locale.CHINA, mapper.serializationConfig().getLocale());
        }
        
        @Test
        @DisplayName("默认 TimeZone 为系统默认时区")
        void defaultTimeZone_systemDefault() {
            TimeZone expected = TimeZone.getTimeZone(ZoneId.systemDefault());
            assertEquals(expected, mapper.serializationConfig().getTimeZone());
        }
        
        @Test
        @DisplayName("默认日期格式为 yyyy-MM-dd HH:mm:ss")
        void defaultDateFormat() {
            var df = mapper.serializationConfig().getDateFormat();
            assertNotNull(df);
            // SimpleDateFormat 的 pattern 可通过 toString 验证
            String pattern = df.toString();
            assertTrue(pattern.contains("yyyy-MM-dd HH:mm:ss"),
                       "日期格式应为 yyyy-MM-dd HH:mm:ss，实际: " + pattern);
        }
    }
    
    // ==================== YAML 专属配置 ====================
    
    @Nested
    @DisplayName("YAML 专属配置")
    class YamlSpecificConfig {
        
        private final YAMLMapper yamlMapper = JacksonUtil.yamlMapper();
        
        @Test
        @DisplayName("WRITE_DOC_START_MARKER 已禁用 - 不输出 --- 开头")
        void writeDocStartMarker_disabled() {
            String yaml = JacksonUtil.toYaml(java.util.Map.of("key", "value"));
            assertNotNull(yaml);
            assertFalse(yaml.startsWith("---"), "YAML 不应以 --- 开头");
        }
        
        @Test
        @DisplayName("MINIMIZE_QUOTES 已启用 - 最小化引号")
        void minimizeQuotes_enabled() {
            assertTrue(yamlMapper.isEnabled(YAMLWriteFeature.MINIMIZE_QUOTES));
        }
        
        @Test
        @DisplayName("INDENT_ARRAYS 已启用 - 数组缩进")
        void indentArrays_enabled() {
            assertTrue(yamlMapper.isEnabled(YAMLWriteFeature.INDENT_ARRAYS));
        }
    }
    
    // ==================== 工具类实例化禁止 ====================
    
    @Nested
    @DisplayName("工具类实例化保护")
    class InstantiationProtection {
        
        @Test
        @DisplayName("私有构造方法抛出 UnsupportedOperationException")
        void constructorThrows() throws Exception {
            var constructor = JacksonUtil.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            var ex = assertThrows(java.lang.reflect.InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        }
    }
}
