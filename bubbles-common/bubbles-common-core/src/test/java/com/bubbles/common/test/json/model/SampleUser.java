package com.bubbles.common.test.json.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 测试用的通用模型，包含多种数据类型字段，用于验证各 Mapper 的序列化/反序列化能力。
 */
public class SampleUser implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private BigDecimal balance;
    private LocalDate birthday;
    private LocalDateTime createdAt;
    private List<String> tags;
    private Map<String, Object> metadata;
    
    public SampleUser() {
    }
    
    public SampleUser(String name, int age, BigDecimal balance,
                      LocalDate birthday, LocalDateTime createdAt,
                      List<String> tags, Map<String, Object> metadata) {
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.tags = tags;
        this.metadata = metadata;
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
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public LocalDate getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SampleUser that)) return false;
        return age == that.age
                && Objects.equals(name, that.name)
                && Objects.equals(balance, that.balance)
                && Objects.equals(birthday, that.birthday)
                && Objects.equals(createdAt, that.createdAt)
                && Objects.equals(tags, that.tags)
                && Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age, balance, birthday, createdAt, tags, metadata);
    }
}
