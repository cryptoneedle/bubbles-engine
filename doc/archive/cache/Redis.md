功能：

开启功能注解

Redis缓存管理器

Redis工具类
通用工具类
操作String
操作Bitmap

配置RedisTemplate
配置Redis相关的Bean

分布式锁实现（借助Redission或者自定义Lua脚本）

注意Redis必须引入 commons-pool2 包才能使链接池生效，生效后可以通过Debug到redisConnectionFactory查看clientConfiguration下多了一个poolConfig子类，如果不引入这个包是没有这个类的 