package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import com.bubbles.common.test.json.model.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.core.type.TypeReference;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} JSON Mapper 相关 API 的单元测试。
 */
@DisplayName("JacksonUtil - JSON Mapper API")
class JacksonUtilJsonApiTest {
    
    private static final SampleUser SAMPLE = new SampleUser(
            "张三", 25, new BigDecimal("9999.50"),
            LocalDate.of(2001, 1, 15),
            LocalDateTime.of(2026, 4, 21, 10, 30, 0),
            List.of("vip", "active"),
            Map.of("level", 5, "source", "web")
    );
    
    // ==================== toJson ====================
    
    @Nested
    @DisplayName("toJson - 对象转 JSON 字符串")
    class ToJson {
        
        @Test
        @DisplayName("普通对象序列化")
        void toJson_normalObject() {
            String json = JacksonUtil.toJson(SAMPLE);
            assertNotNull(json);
            assertTrue(json.contains("\"name\""));
            assertTrue(json.contains("张三"));
            assertTrue(json.contains("\"age\""));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toJson_nullInput() {
            assertNull(JacksonUtil.toJson(null));
        }
        
        @Test
        @DisplayName("Map 序列化")
        void toJson_map() {
            Map<String, Object> map = Map.of("key", "value", "num", 42);
            String json = JacksonUtil.toJson(map);
            assertNotNull(json);
            assertTrue(json.contains("\"key\""));
            assertTrue(json.contains("\"value\""));
        }
        
        @Test
        @DisplayName("List 序列化")
        void toJson_list() {
            List<String> list = List.of("a", "b", "c");
            String json = JacksonUtil.toJson(list);
            assertNotNull(json);
            assertTrue(json.startsWith("["));
            assertTrue(json.contains("\"a\""));
        }
        
        @Test
        @DisplayName("BigDecimal 保持精度")
        void toJson_bigDecimalPrecision() {
            String json = JacksonUtil.toJson(SAMPLE);
            assertTrue(json.contains("9999.50"));
        }
    }
    
    // ==================== toPrettyJson ====================
    
    @Nested
    @DisplayName("toPrettyJson - 对象转格式化 JSON")
    class ToPrettyJson {
        
        @Test
        @DisplayName("格式化输出包含换行")
        void toPrettyJson_containsNewlines() {
            String pretty = JacksonUtil.toPrettyJson(SAMPLE);
            assertNotNull(pretty);
            assertTrue(pretty.contains("\n"));
            assertTrue(pretty.contains("  "));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toPrettyJson_nullInput() {
            assertNull(JacksonUtil.toPrettyJson(null));
        }
        
        @Test
        @DisplayName("格式化 JSON 仍可被解析")
        void toPrettyJson_stillParseable() {
            String pretty = JacksonUtil.toPrettyJson(SAMPLE);
            SampleUser parsed = JacksonUtil.fromJson(pretty, SampleUser.class);
            assertEquals(SAMPLE.getName(), parsed.getName());
            assertEquals(SAMPLE.getAge(), parsed.getAge());
        }
    }
    
    // ==================== fromJson (Class) ====================
    
    @Nested
    @DisplayName("fromJson - JSON 字符串转对象（Class）")
    class FromJsonClass {
        
        @Test
        @DisplayName("正常反序列化")
        void fromJson_normal() {
            String json = """
                    {"name":"李四","age":30}
                    """;
            SampleUser user = JacksonUtil.fromJson(json, SampleUser.class);
            assertNotNull(user);
            assertEquals("李四", user.getName());
            assertEquals(30, user.getAge());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromJson_nullInput() {
            assertNull(JacksonUtil.fromJson(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromJson_emptyInput() {
            assertNull(JacksonUtil.fromJson("", SampleUser.class));
        }
        
        @Test
        @DisplayName("包含未知字段时不报错（FAIL_ON_UNKNOWN_PROPERTIES 已禁用）")
        void fromJson_unknownFieldsIgnored() {
            String json = """
                    {"name":"王五","age":28,"unknownField":"值"}
                    """;
            SampleUser user = JacksonUtil.fromJson(json, SampleUser.class);
            assertNotNull(user);
            assertEquals("王五", user.getName());
        }
    }
    
    // ==================== fromJson (TypeReference) ====================
    
    @Nested
    @DisplayName("fromJson - JSON 字符串转泛型对象（TypeReference）")
    class FromJsonTypeRef {
        
        @Test
        @DisplayName("反序列化为 List<String>")
        void fromJson_listOfString() {
            String json = "[\"a\",\"b\",\"c\"]";
            List<String> list = JacksonUtil.fromJson(json, new TypeReference<>() {});
            assertEquals(List.of("a", "b", "c"), list);
        }
        
        @Test
        @DisplayName("反序列化为 Map<String, Object>")
        void fromJson_mapOfStringObject() {
            String json = "{\"key\":\"value\"}";
            Map<String, Object> map = JacksonUtil.fromJson(json, new TypeReference<>() {});
            assertNotNull(map);
            assertEquals("value", map.get("key"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromJson_nullInput() {
            assertNull(JacksonUtil.fromJson(null, new TypeReference<List<String>>() {}));
        }
    }
    
    // ==================== fromJsonList ====================
    
    @Nested
    @DisplayName("fromJsonList - JSON 字符串转 List 对象")
    class FromJsonList {
        
        @Test
        @DisplayName("正常反序列化为 List<SampleUser>")
        void fromJsonList_normal() {
            String json = """
                    [{"name":"A","age":10},{"name":"B","age":20}]
                    """;
            List<SampleUser> list = JacksonUtil.fromJsonList(json, SampleUser.class);
            assertEquals(2, list.size());
            assertEquals("A", list.getFirst().getName());
            assertEquals(20, list.getLast().getAge());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromJsonList_nullInput() {
            assertNull(JacksonUtil.fromJsonList(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromJsonList_emptyInput() {
            assertNull(JacksonUtil.fromJsonList("", SampleUser.class));
        }
    }
    
    // ==================== fromJsonMap ====================
    
    @Nested
    @DisplayName("fromJsonMap - JSON 字符串转 Map 对象")
    class FromJsonMap {
        
        @Test
        @DisplayName("正常反序列化为 Map<String, String>")
        void fromJsonMap_normal() {
            String json = "{\"key1\":\"val1\",\"key2\":\"val2\"}";
            Map<String, String> map = JacksonUtil.fromJsonMap(json, String.class, String.class);
            assertEquals("val1", map.get("key1"));
            assertEquals("val2", map.get("key2"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromJsonMap_nullInput() {
            assertNull(JacksonUtil.fromJsonMap(null, String.class, String.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromJsonMap_emptyInput() {
            assertNull(JacksonUtil.fromJsonMap("", String.class, String.class));
        }
    }
    
    // ==================== readTree ====================
    
    @Nested
    @DisplayName("readTree - JSON 解析为 JsonNode 树")
    class ReadTree {
        
        @Test
        @DisplayName("正常解析 JSON 为 JsonNode")
        void readTree_normal() {
            String json = "{\"name\":\"test\",\"value\":42}";
            var node = JacksonUtil.readTree(json);
            assertNotNull(node);
            assertEquals("test", node.get("name").asText());
            assertEquals(42, node.get("value").asInt());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void readTree_nullInput() {
            assertNull(JacksonUtil.readTree(null));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void readTree_emptyInput() {
            assertNull(JacksonUtil.readTree(""));
        }
    }
    
    // ==================== convert ====================
    
    @Nested
    @DisplayName("convert - 对象间转换")
    class Convert {
        
        @Test
        @DisplayName("Map 转 POJO")
        void convert_mapToPojo() {
            Map<String, Object> map = Map.of("name", "转换测试", "age", 33);
            SampleUser user = JacksonUtil.convert(map, SampleUser.class);
            assertNotNull(user);
            assertEquals("转换测试", user.getName());
            assertEquals(33, user.getAge());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void convert_nullInput() {
            assertNull(JacksonUtil.convert(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("POJO 转 Map（TypeReference）")
        void convert_pojoToMap() {
            SampleUser user = new SampleUser("映射", 20, null, null, null, null, null);
            Map<String, Object> map = JacksonUtil.convert(user, new TypeReference<>() {});
            assertNotNull(map);
            assertEquals("映射", map.get("name"));
        }
    }
    
    // ==================== isValidJson ====================
    
    @Nested
    @DisplayName("isValidJson - 校验 JSON 合法性")
    class IsValidJson {
        
        @Test
        @DisplayName("合法 JSON 返回 true")
        void isValidJson_valid() {
            assertTrue(JacksonUtil.isValidJson("{\"key\":\"value\"}"));
            assertTrue(JacksonUtil.isValidJson("[1,2,3]"));
            assertTrue(JacksonUtil.isValidJson("\"hello\""));
            assertTrue(JacksonUtil.isValidJson("42"));
        }
        
        @Test
        @DisplayName("非法 JSON 返回 false")
        void isValidJson_invalid() {
            assertFalse(JacksonUtil.isValidJson("{invalid}"));
            assertFalse(JacksonUtil.isValidJson("not json"));
        }
    }
    
    // ==================== 文件读写 ====================
    
    @Nested
    @DisplayName("文件读写 - writeJsonToFile / readJsonFromFile")
    class FileOperations {
        
        @Test
        @DisplayName("写入并读取 JSON 文件")
        void writeAndReadFile(@TempDir Path tempDir) {
            File file = tempDir.resolve("test-user.json").toFile();
            JacksonUtil.writeJsonToFile(SAMPLE, file);
            assertTrue(file.exists());
            assertTrue(file.length() > 0);
            
            SampleUser loaded = JacksonUtil.readJsonFromFile(file, SampleUser.class);
            assertNotNull(loaded);
            assertEquals(SAMPLE.getName(), loaded.getName());
            assertEquals(SAMPLE.getAge(), loaded.getAge());
        }
        
        @Test
        @DisplayName("null 对象写入时不创建文件")
        void writeJsonToFile_nullObject(@TempDir Path tempDir) {
            File file = tempDir.resolve("null-test.json").toFile();
            JacksonUtil.writeJsonToFile(null, file);
            assertFalse(file.exists());
        }
        
        @Test
        @DisplayName("null 文件参数时安全返回")
        void readJsonFromFile_nullFile() {
            assertNull(JacksonUtil.readJsonFromFile(null, SampleUser.class));
        }
    }
}
