# Json

[Json官方网站](https://www.json.org/json-zh.html)

## Jackson

[Jackson-Github](https://github.com/FasterXML/jackson)

[Jackson-Document](https://github.com/FasterXML/jackson-docs)

### 模块

- jackson-dataformat-yaml 绑定了
- jackson-datatype-jsr310 支持JSR 310规范下的新类型。eg.LocalDateTime等类型
- jackson-datatype-jdk8
- jackson-module-blackbird 取代 jackson-module-afterburner 使用字节码增强转换速度

### 三种处理JSON的方式

- Streaming API ：流式API，将JSON内容作为离散事件读取和写入，使用JsonParser、JsonGenerator进行读写操作，开销最小，效率最高
- Tree Model：树模型，类似于XML的DOM解析器，将JSON内容使用树状结构表示，比较灵活
- Data Binding：数据绑定，简单易用
    - Simple Data Binding：简单的数据类型绑定，Java基本数据类型、集合和JSON之间的转换
    - Full Data Binding：完整的数据类型绑定，Java Bean和JSON之间的转换

### 三个低级类

- JsonFactory：JSON工厂类，用于创建JsonGenerator、JsonParser。
    ```java
    // 线程安全，一般在应用中只使用一个全局共享的工厂实例即可
    JsonFactory jsonFactoryByBuilder =JsonFactory.builder().build(); // 方式一：Builder 构建
    JsonFactory jsonFactory = new JsonFactory(); // 方式二：直接 new
    ```
- JsonGenerator：JSON生成器，用于写入数据
    ```java
    // 通过 createGenerator() 方法创建
    JsonGenerator jsonGenerator = jsonFactory.createGenerator(xxx);
  
    // 写操作
    jsonGenerator.writeStartObject(); // 开始写入对象 => {
    jsonGenerator.writeNumberField("id", 1699632398490275840L); // long
    jsonGenerator.writeStringField("name", "坤坤"); // String
    jsonGenerator.writeNumberField("age", 18); // int
  
    // 写对象
    jsonGenerator.writeFieldName("org"); // 属性名称
    jsonGenerator.writeStartObject(); // 开始写入对象=》{
    jsonGenerator.writeNumberField("id", 1699967647585800192L); // long
    jsonGenerator.writeStringField("orgName", "阿里巴巴");
    jsonGenerator.writeStringField("address", "浙江杭州");
    jsonGenerator.writeEndObject(); // 结束写入对象 => }
  
    // 写对象集合
    jsonGenerator.writeFieldName("roleList"); // 属性名称
    jsonGenerator.writeStartArray(); // 写入集合开始=》 [ 
    // 第一个角色对象
    jsonGenerator.writeStartObject(); // 开始写入对象=》{
    jsonGenerator.writeNumberField("id", 1699972559514243072L); // long
    jsonGenerator.writeStringField("roleName", "系统管理员");
    jsonGenerator.writeStringField("roleCode", "ROLE_ADMIN");
    jsonGenerator.writeEndObject(); // 结束写入对象=》}
    // 第二个角色对象
    jsonGenerator.writeStartObject(); // 开始写入对象=》{
    jsonGenerator.writeNumberField("id", 1701893746586685440L); // long
    jsonGenerator.writeStringField("roleName", "企业员工");
    jsonGenerator.writeStringField("roleCode", "ROLE_STAFF");
    jsonGenerator.writeEndObject(); // 结束写入对象=》}
    jsonGenerator.writeEndArray();// 写入集合结束=》 ]
  
    // 结束并关流
    jsonGenerator.writeEndObject(); // 结束写入对象=》}
    jsonGenerator.close();
    ```
- JsonParser：JSON解析器，用于读取数据。扫描并生成 JsonToken 对象
    ```java
    // 通过 createParser() 方法创建
    JsonParser jsonParser = jsonFactory.createParser(file);
    
    // 循环解析所有元素
    while (!jsonParser.isClosed()) {
        JsonToken jsonToken = jsonParser.nextToken();
        System.out.println("当前解析到的令牌类型：" + jsonToken);
    }
    ```

### 一个重要的高级类

ObjectMapper

```java
ObjectMapper objectMapper = new ObjectMapper();
// 写Json
String json = objectMapper.writeValueAsString(user);
// 读Json
User user = objectMapper.readValue(jsonStr, User.class);
// 在JSON不太好转换为某个标准的对象时，可以直接转换为统一的树模型
JsonNode jsonNode = objectMapper.readTree(userJsonStr);
long userId = jsonNode.get("id").asLong();
String roleName = jsonNode.get("roleList").get(0).get("roleName").asText();
```

### 泛型擦除问题

```java
Result<User> userResult = objectMapper.readValue(jsonStr, Result.class);
// Exception in thread "main" java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.pearl.jacksoncore.demo.pojo.User (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; com.pearl.jacksoncore.demo.pojo.User is in unnamed module of loader 'app')

// 解决方式：Jackson提供了 TypeReference<T> 抽象类指定转换时的泛型类型
Result<User> response = objectMapper.readValue(jsonStr, new TypeReference<Result<User>>() {
});
```

### 注解

参考 [Jackson 2.x 系列【5】注解大全篇一](https://juejin.cn/post/7362119848661827647)
参考 [Jackson 2.x 系列【6】注解大全篇二](https://juejin.cn/post/7362057695975997479)
参考 [Jackson 2.x 系列【7】注解大全篇三](https://juejin.cn/post/7362547971059630118)
参考 [Jackson 2.x 系列【8】注解大全篇四](https://juejin.cn/post/7362858912019922995)

# TODO

- [ ] 在微服务内部请求过程中使用二进制序列化。[jackson-dataformats-binary](https://github.com/FasterXML/jackson-dataformats-binary)
    - 采用 Smile 格式
      ```pom
      <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-smile</artifactId>
        <version>2.16.0</version>
      </dependency>
      ```
      ```java
      SmileMapper mapper = new SmileMapper();
      // (or can construct factory, configure instance with 'SmileParser.Feature'
      // and 'SmileGenerator.Feature' first)
      SomeType value = "...";
      byte[] smileData = mapper.writeValueAsBytes(value);
      SomeType otherValue = mapper.readValue(smileData, SomeType.class);
      ```
- [ ] 引入 jackson-module-blackbird，使用字节码提高性能。注意多ObjectMappers实例化会引起OOM
    ```pom
    <dependency>
      <groupId>tools.jackson.module</groupId>
      <artifactId>jackson-module-blackbird</artifactId>
    </dependency>
    ```
    ```java
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new BlackbirdModule())
            .build();
    ```
- jackson-module-kotlin 模块是对于Kotlin语言的支持，是否需要引入有待考究

