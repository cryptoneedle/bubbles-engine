package com.bubbles.common.json;

import cn.hutool.v7.core.reflect.ClassUtil;
import com.google.common.collect.ImmutableList;
import tools.jackson.core.JacksonException;
import tools.jackson.core.json.PackageVersion;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.databind.ext.javatime.deser.*;
import tools.jackson.databind.ext.javatime.ser.*;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.dataformat.cbor.CBORMapper;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.csv.CsvSchema;
import tools.jackson.dataformat.javaprop.JavaPropsMapper;
import tools.jackson.dataformat.smile.SmileMapper;
import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.yaml.YAMLMapper;
import tools.jackson.dataformat.yaml.YAMLWriteFeature;
import tools.jackson.datatype.guava.GuavaModule;
import tools.jackson.datatype.hibernate7.Hibernate7Module;
import tools.jackson.module.blackbird.BlackbirdModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p>description:  </p>
 *
 * @author CryptoNeedle
 * @date 2026-04-21
 */
public class JacksonUtil {
    
    private JacksonUtil() {
        throw new UnsupportedOperationException("工具类禁止实例化");
    }
    
    private static final DateFormat DATE_FORMAT;
    
    /** JSON Mapper */
    private static final JsonMapper JSON_MAPPER;
    /** YAML Mapper */
    private static final YAMLMapper YAML_MAPPER;
    /** XML Mapper */
    private static final XmlMapper XML_MAPPER;
    /** CSV Mapper */
    private static final CsvMapper CSV_MAPPER;
    /** Properties Mapper */
    private static final JavaPropsMapper PROPERTIES_MAPPER;
    /** CBOR Mapper */
    private static final CBORMapper CBOR_MAPPER;
    /** Smile Mapper */
    private static final SmileMapper SMILE_MAPPER;
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        JSON_MAPPER = initBuilder(JsonMapper.builder()).build();
        YAML_MAPPER = initBuilder(YAMLMapper.builder()).disable(YAMLWriteFeature.WRITE_DOC_START_MARKER)
                                                       .enable(YAMLWriteFeature.MINIMIZE_QUOTES)
                                                       .enable(YAMLWriteFeature.INDENT_ARRAYS)
                                                       .build();
        XML_MAPPER = initBuilder(XmlMapper.builder()).build();
        CSV_MAPPER = initBuilder(CsvMapper.builder()).build();
        PROPERTIES_MAPPER = initBuilder(JavaPropsMapper.builder()).build();
        CBOR_MAPPER = initBuilder(CBORMapper.builder()).build();
        SMILE_MAPPER = initBuilder(SmileMapper.builder()).build();
    }
    
    private static <M extends ObjectMapper, B extends MapperBuilder<M, B>> B initBuilder(B builder) {
        // 1.注册基础模块
        CustomJavaTimeModule javaTimeModule = new CustomJavaTimeModule();
        // 支持 Google Guava 集合
        GuavaModule guavaModule = new GuavaModule();
        // 支持 MethodHandle 反射加速
        BlackbirdModule blackbirdModule = new BlackbirdModule();
        // todo 需引入 Hibernate 依赖
        // 支持 Hibernate 懒加载代理
        Hibernate7Module hibernate7Module = new Hibernate7Module()
                // 关闭强制懒加载，避免 N+1
                .disable(Hibernate7Module.Feature.FORCE_LAZY_LOADING)
                // 使用序列化标识符而非完整对象
                .enable(Hibernate7Module.Feature.USE_TRANSIENT_ANNOTATION);
        // 动态注入 Hibernate 模块
        boolean hasHibernate = ClassUtil.isClassExists("org.hibernate.proxy.HibernateProxy", null);
        if (hasHibernate) {
            builder.addModules(ImmutableList.of(javaTimeModule, guavaModule, blackbirdModule, hibernate7Module));
        } else {
            builder.addModules(ImmutableList.of(javaTimeModule, guavaModule, blackbirdModule));
        }
        
        // 2.配置通用特性
        // 2.1 序列化配置
        // 禁止在空 Bean 时报错，序列化为 {}
        builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许将单个值反序列化为数组
        builder.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        builder.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 3. 反序列化配置
        // 忽略未知字段（兼容旧版本/版本升级）
        builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 允许空字符串转为 null 对象
        builder.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        // 允许空数组转为 null 对象
        builder.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        // 默认开启美化输出（todo 生产环境按需关闭以提升性能）
        //builder.enable(SerializationFeature.INDENT_OUTPUT);
        builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        builder.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
        builder.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        builder.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        
        // 4. 树模型配置
        // 允许标量类型的强制转换
        builder.enable(MapperFeature.ALLOW_COERCION_OF_SCALARS);
        
        // 5. 默认配置
        builder.defaultLocale(Locale.CHINA);
        builder.defaultTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        builder.defaultDateFormat(DATE_FORMAT);
        
        return builder;
    }
    
    // ==================== Mapper 访问 ====================
    
    public static JsonMapper jsonMapper() {
        return JSON_MAPPER;
    }
    
    public static YAMLMapper yamlMapper() {
        return YAML_MAPPER;
    }
    
    public static XmlMapper xmlMapper() {
        return XML_MAPPER;
    }
    
    public static CsvMapper csvMapper() {
        return CSV_MAPPER;
    }
    
    public static JavaPropsMapper propertiesMapper() {
        return PROPERTIES_MAPPER;
    }
    
    public static CBORMapper cborMapper() {
        return CBOR_MAPPER;
    }
    
    public static SmileMapper smileMapper() {
        return SMILE_MAPPER;
    }
    
    /** 默认对外暴露 JSON ObjectMapper */
    public static ObjectMapper objectMapper() {
        return JSON_MAPPER;
    }
    
    // ==================== JSON 常用 API ====================
    
    /** 对象 -> JSON 字符串 */
    public static String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 序列化失败: " + e.getMessage(), e);
        }
    }
    
    /** 对象 -> 格式化 JSON 字符串 */
    public static String toPrettyJson(Object obj) {
        if (obj == null) return null;
        try {
            return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 序列化失败: " + e.getMessage(), e);
        }
    }
    
    /** JSON -> 对象 */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON_MAPPER.readValue(json, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 反序列化失败: " + e.getMessage(), e);
        }
    }
    
    /** JSON -> 泛型对象（如 List<User>） */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON_MAPPER.readValue(json, typeRef);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 反序列化失败: " + e.getMessage(), e);
        }
    }
    
    /** JSON -> List对象（如 List<User>） */
    public static <T> List<T> fromJsonList(String json, Class<T> elementClass) {
        if (json == null || json.isEmpty()) return null;
        try {
            // Jackson 3 中构建集合类型的新方式
            var listType = JSON_MAPPER.getTypeFactory().constructCollectionType(List.class, elementClass);
            return JSON_MAPPER.readValue(json, listType);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 反序列化失败:", e);
        }
    }
    
    public static <K, V> Map<K, V> fromJsonMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (json == null || json.isEmpty()) return null;
        try {
            JavaType type = JSON_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
            return JSON_MAPPER.readValue(json, type);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 反序列化失败:", e);
        }
    }
    
    /** JSON -> JsonNode 树 */
    public static JsonNode readTree(String json) {
        if (json == null || json.isEmpty()) return null;
        try {
            return JSON_MAPPER.readTree(json);
        } catch (JacksonException e) {
            throw new IllegalStateException("JSON 解析失败: " + e.getMessage(), e);
        }
    }
    
    /** 对象 <-> 对象 之间的转换（基于 JSON 模型） */
    public static <T> T convert(Object from, Class<T> toClass) {
        if (from == null) return null;
        return JSON_MAPPER.convertValue(from, toClass);
    }
    
    /** 对象 <-> 对象 之间的转换（基于 JSON 模型） */
    public static <T> T convert(Object from, TypeReference<T> toType) {
        if (from == null) return null;
        return JSON_MAPPER.convertValue(from, toType);
    }
    
    // ==================== YAML ====================
    public static String toYaml(Object obj) {
        if (obj == null) return null;
        try {
            return YAML_MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("YAML 序列化失败", e);
        }
    }
    
    public static <T> T fromYaml(String yaml, Class<T> clazz) {
        if (yaml == null || yaml.isEmpty()) return null;
        try {
            return YAML_MAPPER.readValue(yaml, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("YAML 反序列化失败", e);
        }
    }
    
    public static <T> T fromYaml(InputStream in, Class<T> clazz) throws IOException {
        return YAML_MAPPER.readValue(in, clazz);
    }
    
    // ==================== XML ====================
    public static String toXml(Object obj) {
        if (obj == null) return null;
        try {
            return XML_MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("XML 序列化失败", e);
        }
    }
    
    public static <T> T fromXml(String xml, Class<T> clazz) {
        if (xml == null || xml.isEmpty()) return null;
        try {
            return XML_MAPPER.readValue(xml, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("XML 反序列化失败", e);
        }
    }
    
    // ==================== Properties ====================
    public static String toProperties(Object obj) {
        if (obj == null) return null;
        try {
            return PROPERTIES_MAPPER.writeValueAsString(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("Properties 序列化失败", e);
        }
    }
    
    public static <T> T fromProperties(String props, Class<T> clazz) {
        if (props == null || props.isEmpty()) return null;
        try {
            return PROPERTIES_MAPPER.readValue(props, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("Properties 反序列化失败", e);
        }
    }
    // ==================== CSV ====================
    
    /**
     * 写出 CSV（使用首行为列头）。
     *
     * @param list  POJO 列表
     * @param clazz POJO 类型（用于生成 Schema）
     */
    public static <T> String toCsv(List<T> list, Class<T> clazz) {
        if (list == null || list.isEmpty()) return null;
        try {
            CsvSchema schema = CSV_MAPPER.schemaFor(clazz).withHeader();
            return CSV_MAPPER.writer(schema).writeValueAsString(list);
        } catch (JacksonException e) {
            throw new IllegalStateException("CSV 序列化失败", e);
        }
    }
    
    /** 读取 CSV 为对象列表（期望首行为列头） */
    public static <T> List<T> fromCsv(String csv, Class<T> clazz) {
        if (csv == null || csv.isEmpty()) return null;
        try {
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            return CSV_MAPPER.readerFor(clazz).with(schema).<T>readValues(csv).readAll();
        } catch (JacksonException e) {
            throw new IllegalStateException("CSV 反序列化失败", e);
        }
    }
    
    // ==================== CBOR (二进制) ====================
    public static byte[] toCbor(Object obj) {
        if (obj == null) return null;
        try {
            return CBOR_MAPPER.writeValueAsBytes(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("CBOR 序列化失败", e);
        }
    }
    
    public static <T> T fromCbor(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) return null;
        try {
            return CBOR_MAPPER.readValue(data, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("CBOR 反序列化失败", e);
        }
    }
    
    // ==================== Smile (二进制 JSON) ====================
    public static byte[] toSmile(Object obj) {
        if (obj == null) return null;
        try {
            return SMILE_MAPPER.writeValueAsBytes(obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("Smile 序列化失败", e);
        }
    }
    
    public static <T> T fromSmile(byte[] data, Class<T> clazz) {
        if (data == null || data.length == 0) return null;
        try {
            return SMILE_MAPPER.readValue(data, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("Smile 反序列化失败", e);
        }
    }
    
    // ==================== 跨格式转换 ====================
    
    /** JSON -> YAML */
    public static String jsonToYaml(String json) {
        return toYaml(readTree(json));
    }
    
    /** YAML -> JSON */
    public static String yamlToJson(String yaml) {
        try {
            return toJson(YAML_MAPPER.readTree(yaml));
        } catch (JacksonException e) {
            throw new IllegalStateException("YAML -> JSON 失败", e);
        }
    }
    
    /** JSON -> XML */
    public static String jsonToXml(String json) {
        return toXml(readTree(json));
    }
    
    /** Properties -> JSON */
    public static String propertiesToJson(String props) {
        return toJson(fromProperties(props, Map.class));
    }
    
    // ==================== 文件 ====================
    public static void writeJsonToFile(Object obj, File file) {
        if (obj == null || file == null) return;
        try {
            JSON_MAPPER.writeValue(file, obj);
        } catch (JacksonException e) {
            throw new IllegalStateException("写入 JSON 文件失败", e);
        }
    }
    
    public static <T> T readJsonFromFile(File file, Class<T> clazz) {
        if (file == null) return null;
        try {
            return JSON_MAPPER.readValue(file, clazz);
        } catch (JacksonException e) {
            throw new IllegalStateException("读取 JSON 文件失败", e);
        }
    }
    
    // ==================== 工具 ====================
    public static boolean isValidJson(String json) {
        try {
            JSON_MAPPER.readTree(json);
            return true;
        } catch (JacksonException e) {
            return false;
        }
    }
}