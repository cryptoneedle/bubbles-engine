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
 * {@link JacksonUtil} YAML Mapper 相关 API 的单元测试。
 */
@DisplayName("JacksonUtil - YAML Mapper API")
class JacksonUtilYamlApiTest {
    
    private static final SampleUser SAMPLE = new SampleUser(
            "赵六", 28, new BigDecimal("5000.00"),
            LocalDate.of(1998, 6, 20),
            LocalDateTime.of(2026, 4, 21, 8, 0, 0),
            List.of("admin"),
            Map.of("dept", "工程部")
    );
    
    // ==================== toYaml ====================
    
    @Nested
    @DisplayName("toYaml - 对象转 YAML 字符串")
    class ToYaml {
        
        @Test
        @DisplayName("普通对象序列化为 YAML")
        void toYaml_normalObject() {
            String yaml = JacksonUtil.toYaml(SAMPLE);
            assertNotNull(yaml);
            assertFalse(yaml.startsWith("---")); // WRITE_DOC_START_MARKER 已禁用
            assertTrue(yaml.contains("name"));
            assertTrue(yaml.contains("赵六"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toYaml_nullInput() {
            assertNull(JacksonUtil.toYaml(null));
        }
        
        @Test
        @DisplayName("List 序列化为 YAML 数组（缩进风格）")
        void toYaml_listWithIndentArrays() {
            List<String> list = List.of("item1", "item2", "item3");
            String yaml = JacksonUtil.toYaml(Map.of("items", list));
            assertNotNull(yaml);
            assertTrue(yaml.contains("- "));
        }
        
        @Test
        @DisplayName("YAML 最小化引号（MINIMIZE_QUOTES）")
        void toYaml_minimizeQuotes() {
            Map<String, String> map = Map.of("status", "active", "count", "5");
            String yaml = JacksonUtil.toYaml(map);
            assertNotNull(yaml);
            // 纯数字字符串值在 MINIMIZE_QUOTES 下仍保留引号以区分类型
            // 普通字符串值应不加引号
            assertFalse(yaml.contains("'active'"));
        }
    }
    
    // ==================== fromYaml (String) ====================
    
    @Nested
    @DisplayName("fromYaml - YAML 字符串转对象")
    class FromYaml {
        
        @Test
        @DisplayName("正常 YAML 反序列化")
        void fromYaml_normal() {
            String yaml = """
                    name: "钱七"
                    age: 35
                    """;
            SampleUser user = JacksonUtil.fromYaml(yaml, SampleUser.class);
            assertNotNull(user);
            assertEquals("钱七", user.getName());
            assertEquals(35, user.getAge());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromYaml_nullInput() {
            assertNull(JacksonUtil.fromYaml((String) null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromYaml_emptyInput() {
            assertNull(JacksonUtil.fromYaml("", SampleUser.class));
        }
        
        @Test
        @DisplayName("包含复杂结构的 YAML")
        void fromYaml_complexStructure() {
            String yaml = """
                    name: "测试"
                    age: 22
                    tags:
                      - "tag1"
                      - "tag2"
                    """;
            SampleUser user = JacksonUtil.fromYaml(yaml, SampleUser.class);
            assertNotNull(user);
            assertEquals("测试", user.getName());
            assertNotNull(user.getTags());
            assertEquals(2, user.getTags().size());
        }
    }
    
    // ==================== 跨格式转换 ====================
    
    @Nested
    @DisplayName("跨格式转换 - JSON <-> YAML")
    class CrossFormat {
        
        @Test
        @DisplayName("jsonToYaml - JSON 转 YAML")
        void jsonToYaml() {
            String json = """
                    {"name":"孙八","age":40}
                    """;
            String yaml = JacksonUtil.jsonToYaml(json);
            assertNotNull(yaml);
            assertTrue(yaml.contains("name"));
            assertTrue(yaml.contains("孙八"));
        }
        
        @Test
        @DisplayName("yamlToJson - YAML 转 JSON")
        void yamlToJson() {
            String yaml = """
                    name: "周九"
                    age: 45
                    """;
            String json = JacksonUtil.yamlToJson(yaml);
            assertNotNull(json);
            assertTrue(json.contains("\"name\""));
            assertTrue(json.contains("周九"));
        }
        
        @Test
        @DisplayName("JSON -> YAML -> JSON 往返转换")
        void jsonToYamlAndBack() {
            String originalJson = JacksonUtil.toJson(SAMPLE);
            String yaml = JacksonUtil.jsonToYaml(originalJson);
            String roundTripJson = JacksonUtil.yamlToJson(yaml);
            
            SampleUser fromOriginal = JacksonUtil.fromJson(originalJson, SampleUser.class);
            SampleUser fromRoundTrip = JacksonUtil.fromJson(roundTripJson, SampleUser.class);
            assertEquals(fromOriginal.getName(), fromRoundTrip.getName());
            assertEquals(fromOriginal.getAge(), fromRoundTrip.getAge());
        }
    }
}
