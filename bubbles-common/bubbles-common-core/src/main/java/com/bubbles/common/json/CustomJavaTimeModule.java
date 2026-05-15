package com.bubbles.common.json;

import tools.jackson.core.json.PackageVersion;
import tools.jackson.databind.ext.javatime.deser.*;
import tools.jackson.databind.ext.javatime.ser.*;
import tools.jackson.databind.module.SimpleModule;

import java.io.Serial;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * <p>description:  </p>
 *
 * @author CryptoNeedle
 * @date 2026-04-21
 */
public class CustomJavaTimeModule extends SimpleModule {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * PigJavaTimeModule构造函数，用于初始化时间序列化和反序列化规则
     */
    public CustomJavaTimeModule() {
        super(PackageVersion.VERSION);
        
        // ======================= 时间序列化规则 ===============================
        // yyyy-MM-dd HH:mm:ss
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // yyyy-MM-dd
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // HH:mm:ss
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        // Instant 类型序列化
        this.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        // Duration 类型序列化
        this.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        
        // ======================= 时间反序列化规则 ==============================
        // yyyy-MM-dd HH:mm:ss
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // yyyy-MM-dd
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // HH:mm:ss
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
        // Instant 反序列化
        this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        // Duration 反序列化
        this.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
    }
}