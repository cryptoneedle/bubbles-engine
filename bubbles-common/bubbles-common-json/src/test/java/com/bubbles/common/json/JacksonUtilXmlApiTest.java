package com.bubbles.common.json;

import com.bubbles.common.json.model.SampleUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} XML Mapper 相关 API 的单元测试。
 */
@DisplayName("JacksonUtil - XML Mapper API")
class JacksonUtilXmlApiTest {
    
    // ==================== toXml ====================
    
    @Nested
    @DisplayName("toXml - 对象转 XML 字符串")
    class ToXml {
        
        @Test
        @DisplayName("简单对象序列化为 XML")
        void toXml_simpleObject() {
            SampleUser user = new SampleUser();
            user.setName("XML测试");
            user.setAge(30);
            String xml = JacksonUtil.toXml(user);
            assertNotNull(xml);
            assertTrue(xml.contains("<name>XML测试</name>"));
            assertTrue(xml.contains("<age>30</age>"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toXml_nullInput() {
            assertNull(JacksonUtil.toXml(null));
        }
        
        @Test
        @DisplayName("Map 序列化为 XML")
        void toXml_map() {
            var map = java.util.Map.of("key1", "value1", "key2", "value2");
            String xml = JacksonUtil.toXml(map);
            assertNotNull(xml);
            assertTrue(xml.contains("key1"));
            assertTrue(xml.contains("value1"));
        }
    }
    
    // ==================== fromXml ====================
    
    @Nested
    @DisplayName("fromXml - XML 字符串转对象")
    class FromXml {
        
        @Test
        @DisplayName("简单 XML 反序列化")
        void fromXml_simple() {
            String xml = """
                    <SampleUser>
                        <name>XML反序列化</name>
                        <age>25</age>
                    </SampleUser>
                    """;
            SampleUser user = JacksonUtil.fromXml(xml, SampleUser.class);
            assertNotNull(user);
            assertEquals("XML反序列化", user.getName());
            assertEquals(25, user.getAge());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromXml_nullInput() {
            assertNull(JacksonUtil.fromXml(null, SampleUser.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromXml_emptyInput() {
            assertNull(JacksonUtil.fromXml("", SampleUser.class));
        }
        
        @Test
        @DisplayName("忽略未知 XML 元素（FAIL_ON_UNKNOWN_PROPERTIES 已禁用）")
        void fromXml_unknownFieldsIgnored() {
            String xml = """
                    <SampleUser>
                        <name>未知字段测试</name>
                        <age>20</age>
                        <unknownField>值</unknownField>
                    </SampleUser>
                    """;
            SampleUser user = JacksonUtil.fromXml(xml, SampleUser.class);
            assertNotNull(user);
            assertEquals("未知字段测试", user.getName());
        }
    }
    
    // ==================== jsonToXml ====================
    
    @Nested
    @DisplayName("jsonToXml - JSON 转 XML")
    class JsonToXml {
        
        @Test
        @DisplayName("JSON 字符串转 XML")
        void jsonToXml_normal() {
            String json = """
                    {"name":"JSON2XML","age":50}
                    """;
            String xml = JacksonUtil.jsonToXml(json);
            assertNotNull(xml);
            assertTrue(xml.contains("name"));
            assertTrue(xml.contains("JSON2XML"));
        }
    }
}
