package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} Properties Mapper 相关 API 的单元测试。
 */
@DisplayName("JacksonUtil - Properties Mapper API")
class JacksonUtilPropertiesApiTest {
    
    /**
     * Properties 专用简单 POJO
     */
    public static class AppConfig {
        private String host;
        private int port;
        private boolean enabled;
        
        public AppConfig() {
        }
        
        public AppConfig(String host, int port, boolean enabled) {
            this.host = host;
            this.port = port;
            this.enabled = enabled;
        }
        
        public String getHost() {
            return host;
        }
        
        public void setHost(String host) {
            this.host = host;
        }
        
        public int getPort() {
            return port;
        }
        
        public void setPort(int port) {
            this.port = port;
        }
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    
    // ==================== toProperties ====================
    
    @Nested
    @DisplayName("toProperties - 对象转 Properties 字符串")
    class ToProperties {
        
        @Test
        @DisplayName("普通对象序列化为 Properties 格式")
        void toProperties_normal() {
            AppConfig config = new AppConfig("localhost", 8080, true);
            String props = JacksonUtil.toProperties(config);
            assertNotNull(props);
            assertTrue(props.contains("host"));
            assertTrue(props.contains("localhost"));
            assertTrue(props.contains("port"));
            assertTrue(props.contains("8080"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toProperties_nullInput() {
            assertNull(JacksonUtil.toProperties(null));
        }
    }
    
    // ==================== fromProperties ====================
    
    @Nested
    @DisplayName("fromProperties - Properties 字符串转对象")
    class FromProperties {
        
        @Test
        @DisplayName("正常 Properties 反序列化")
        void fromProperties_normal() {
            String props = """
                    host=127.0.0.1
                    port=9090
                    enabled=true
                    """;
            AppConfig config = JacksonUtil.fromProperties(props, AppConfig.class);
            assertNotNull(config);
            assertEquals("127.0.0.1", config.getHost());
            assertEquals(9090, config.getPort());
            assertTrue(config.isEnabled());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromProperties_nullInput() {
            assertNull(JacksonUtil.fromProperties(null, AppConfig.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromProperties_emptyInput() {
            assertNull(JacksonUtil.fromProperties("", AppConfig.class));
        }
    }
    
    // ==================== propertiesToJson ====================
    
    @Nested
    @DisplayName("propertiesToJson - Properties 转 JSON")
    class PropertiesToJson {
        
        @Test
        @DisplayName("Properties 字符串转 JSON")
        void propertiesToJson_normal() {
            String props = """
                    name=test
                    value=123
                    """;
            String json = JacksonUtil.propertiesToJson(props);
            assertNotNull(json);
            assertTrue(json.contains("\"name\""));
            assertTrue(json.contains("\"test\""));
        }
    }
}
