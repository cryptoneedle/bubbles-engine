package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import com.google.common.collect.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} GuavaModule 模块的单元测试。
 * 验证 Google Guava 集合类型在注册 GuavaModule 后的序列化与反序列化能力。
 */
@DisplayName("JacksonUtil - GuavaModule 模块")
class JacksonUtilGuavaModuleTest {
    
    // ==================== 模块注册验证 ====================
    
    @Nested
    @DisplayName("模块注册验证")
    class ModuleRegistration {
        
        @Test
        @DisplayName("JSON Mapper 已注册 GuavaModule")
        void guavaModuleRegistered() {
            ObjectMapper mapper = JacksonUtil.jsonMapper();
            boolean hasGuava = mapper.registeredModules().stream()
                                     .anyMatch(m -> m.getClass().getName().contains("GuavaModule"));
            assertTrue(hasGuava, "应注册 GuavaModule");
        }
        
        @Test
        @DisplayName("所有 Mapper 均已注册 GuavaModule")
        void allMappersHaveGuavaModule() {
            for (ObjectMapper mapper : List.of(
                    JacksonUtil.jsonMapper(),
                    JacksonUtil.yamlMapper(),
                    JacksonUtil.xmlMapper()
            )) {
                boolean hasGuava = mapper.registeredModules().stream()
                                         .anyMatch(m -> m.getClass().getName().contains("GuavaModule"));
                assertTrue(hasGuava, mapper.getClass().getSimpleName() + " 应注册 GuavaModule");
            }
        }
    }
    
    // ==================== ImmutableList ====================
    
    @Nested
    @DisplayName("ImmutableList 序列化/反序列化")
    class ImmutableListTest {
        
        @Test
        @DisplayName("ImmutableList 序列化为 JSON 数组")
        void serialize_immutableList() {
            ImmutableList<String> list = ImmutableList.of("alpha", "beta", "gamma");
            String json = JacksonUtil.toJson(list);
            assertNotNull(json);
            assertTrue(json.contains("alpha"));
            assertTrue(json.contains("beta"));
            assertTrue(json.contains("gamma"));
        }
        
        @Test
        @DisplayName("JSON 数组反序列化为 ImmutableList")
        void deserialize_immutableList() {
            String json = "[\"x\",\"y\",\"z\"]";
            ImmutableList<String> list = JacksonUtil.fromJson(json, new TypeReference<ImmutableList<String>>() {});
            assertNotNull(list);
            assertEquals(3, list.size());
            assertEquals("x", list.getFirst());
            assertEquals("z", list.getLast());
        }
        
        @Test
        @DisplayName("ImmutableList 往返转换")
        void roundTrip_immutableList() {
            ImmutableList<Integer> original = ImmutableList.of(10, 20, 30);
            String json = JacksonUtil.toJson(original);
            ImmutableList<Integer> restored = JacksonUtil.fromJson(json, new TypeReference<ImmutableList<Integer>>() {});
            assertEquals(original, restored);
        }
    }
    
    // ==================== ImmutableMap ====================
    
    @Nested
    @DisplayName("ImmutableMap 序列化/反序列化")
    class ImmutableMapTest {
        
        @Test
        @DisplayName("ImmutableMap 序列化为 JSON 对象")
        void serialize_immutableMap() {
            ImmutableMap<String, Integer> map = ImmutableMap.of("a", 1, "b", 2);
            String json = JacksonUtil.toJson(map);
            assertNotNull(json);
            assertTrue(json.contains("\"a\""));
            assertTrue(json.contains("\"b\""));
        }
        
        @Test
        @DisplayName("JSON 对象反序列化为 ImmutableMap")
        void deserialize_immutableMap() {
            String json = "{\"x\":100,\"y\":200}";
            ImmutableMap<String, Integer> map = JacksonUtil.fromJson(json, new TypeReference<ImmutableMap<String, Integer>>() {});
            assertNotNull(map);
            assertEquals(100, map.get("x"));
            assertEquals(200, map.get("y"));
        }
        
        @Test
        @DisplayName("ImmutableMap 往返转换")
        void roundTrip_immutableMap() {
            ImmutableMap<String, String> original = ImmutableMap.of("key1", "val1", "key2", "val2");
            String json = JacksonUtil.toJson(original);
            ImmutableMap<String, String> restored = JacksonUtil.fromJson(json, new TypeReference<ImmutableMap<String, String>>() {});
            assertEquals(original, restored);
        }
    }
    
    // ==================== ImmutableSet ====================
    
    @Nested
    @DisplayName("ImmutableSet 序列化/反序列化")
    class ImmutableSetTest {
        
        @Test
        @DisplayName("ImmutableSet 序列化为 JSON 数组")
        void serialize_immutableSet() {
            ImmutableSet<String> set = ImmutableSet.of("one", "two", "three");
            String json = JacksonUtil.toJson(set);
            assertNotNull(json);
            assertTrue(json.startsWith("["));
            assertTrue(json.contains("one"));
        }
        
        @Test
        @DisplayName("JSON 数组反序列化为 ImmutableSet")
        void deserialize_immutableSet() {
            String json = "[\"a\",\"b\"]";
            ImmutableSet<String> set = JacksonUtil.fromJson(json, new TypeReference<ImmutableSet<String>>() {});
            assertNotNull(set);
            assertEquals(2, set.size());
            assertTrue(set.contains("a"));
            assertTrue(set.contains("b"));
        }
    }
    
    // ==================== ImmutableListMultimap ====================
    
    @Nested
    @DisplayName("ImmutableListMultimap 序列化/反序列化")
    class ImmutableListMultimapTest {
        
        @Test
        @DisplayName("ImmutableListMultimap 序列化")
        void serialize_listMultimap() {
            ImmutableListMultimap<String, String> multimap = ImmutableListMultimap.of(
                    "fruits", "apple", "fruits", "banana", "veggies", "carrot");
            String json = JacksonUtil.toJson(multimap);
            assertNotNull(json);
            assertTrue(json.contains("fruits"));
            assertTrue(json.contains("apple"));
        }
        
        @Test
        @DisplayName("ImmutableListMultimap 反序列化")
        void deserialize_listMultimap() {
            String json = """
                    {"fruits":["apple","banana"],"veggies":["carrot"]}
                    """;
            ImmutableListMultimap<String, String> multimap =
                    JacksonUtil.fromJson(json, new TypeReference<ImmutableListMultimap<String, String>>() {});
            assertNotNull(multimap);
            assertEquals(2, multimap.get("fruits").size());
            assertEquals("apple", multimap.get("fruits").getFirst());
        }
    }
    
    // ==================== Optional (Guava) ====================
    
    @Nested
    @DisplayName("Guava Optional 序列化/反序列化")
    class GuavaOptionalTest {
        
        @Test
        @DisplayName("Guava Optional 有值时序列化")
        void serialize_present() {
            com.google.common.base.Optional<String> opt = com.google.common.base.Optional.of("hello");
            String json = JacksonUtil.toJson(opt);
            assertNotNull(json);
            assertTrue(json.contains("hello"));
        }
        
        @Test
        @DisplayName("Guava Optional 空值时序列化为 null")
        void serialize_absent() {
            com.google.common.base.Optional<String> opt = com.google.common.base.Optional.absent();
            String json = JacksonUtil.toJson(opt);
            assertNotNull(json);
            assertEquals("null", json);
        }
    }
    
    // ==================== 跨格式验证 ====================
    
    @Nested
    @DisplayName("Guava 类型跨格式验证")
    class CrossFormatTest {
        
        @Test
        @DisplayName("ImmutableList 可通过 YAML Mapper 序列化")
        void yaml_immutableList() {
            ImmutableList<String> list = ImmutableList.of("yaml1", "yaml2");
            String yaml = JacksonUtil.toYaml(Map.of("items", list));
            assertNotNull(yaml);
            assertTrue(yaml.contains("yaml1"));
        }
        
        @Test
        @DisplayName("ImmutableMap 可通过 XML Mapper 序列化")
        void xml_immutableMap() {
            ImmutableMap<String, String> map = ImmutableMap.of("key", "xml-value");
            String xml = JacksonUtil.toXml(map);
            assertNotNull(xml);
            assertTrue(xml.contains("key"));
            assertTrue(xml.contains("xml-value"));
        }
    }
}
