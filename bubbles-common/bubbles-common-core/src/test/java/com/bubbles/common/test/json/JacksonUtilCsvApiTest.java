package com.bubbles.common.test.json;

import com.bubbles.common.json.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} CSV Mapper 相关 API 的单元测试。
 */
@DisplayName("JacksonUtil - CSV Mapper API")
class JacksonUtilCsvApiTest {
    
    /**
     * CSV 专用简单 POJO
     */
    public static class CsvRow {
        private String name;
        private int age;
        private String city;
        
        public CsvRow() {
        }
        
        public CsvRow(String name, int age, String city) {
            this.name = name;
            this.age = age;
            this.city = city;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            this.age = age;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CsvRow csvRow)) return false;
            return age == csvRow.age && java.util.Objects.equals(name, csvRow.name) && java.util.Objects.equals(city, csvRow.city);
        }
    }
    
    private static final List<CsvRow> ROWS = List.of(
            new CsvRow("Alice", 30, "Beijing"),
            new CsvRow("Bob", 25, "Shanghai")
    );
    
    // ==================== toCsv ====================
    
    @Nested
    @DisplayName("toCsv - 对象列表转 CSV 字符串")
    class ToCsv {
        
        @Test
        @DisplayName("正常序列化为 CSV（含表头）")
        void toCsv_normal() {
            String csv = JacksonUtil.toCsv(ROWS, CsvRow.class);
            assertNotNull(csv);
            assertTrue(csv.contains("name"));
            assertTrue(csv.contains("age"));
            assertTrue(csv.contains("city"));
            assertTrue(csv.contains("Alice"));
            assertTrue(csv.contains("30"));
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void toCsv_nullInput() {
            assertNull(JacksonUtil.toCsv(null, CsvRow.class));
        }
        
        @Test
        @DisplayName("空列表输入返回 null")
        void toCsv_emptyList() {
            assertNull(JacksonUtil.toCsv(List.of(), CsvRow.class));
        }
    }
    
    // ==================== fromCsv ====================
    
    @Nested
    @DisplayName("fromCsv - CSV 字符串转对象列表")
    class FromCsv {
        
        @Test
        @DisplayName("正常反序列化 CSV")
        void fromCsv_normal() {
            String csv = """
                    name,age,city
                    Alice,30,Beijing
                    Bob,25,Shanghai
                    """;
            List<CsvRow> rows = JacksonUtil.fromCsv(csv, CsvRow.class);
            assertNotNull(rows);
            assertEquals(2, rows.size());
            assertEquals("Alice", rows.getFirst().getName());
            assertEquals(30, rows.getFirst().getAge());
            assertEquals("Shanghai", rows.getLast().getCity());
        }
        
        @Test
        @DisplayName("null 输入返回 null")
        void fromCsv_nullInput() {
            assertNull(JacksonUtil.fromCsv(null, CsvRow.class));
        }
        
        @Test
        @DisplayName("空字符串输入返回 null")
        void fromCsv_emptyInput() {
            assertNull(JacksonUtil.fromCsv("", CsvRow.class));
        }
    }
}
