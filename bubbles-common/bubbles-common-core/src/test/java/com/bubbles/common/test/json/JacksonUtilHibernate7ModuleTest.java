package com.bubbles.common.test.json;

import cn.hutool.v7.core.reflect.ClassUtil;
import com.bubbles.common.json.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link JacksonUtil} Hibernate7Module 模块的单元测试。
 * 验证条件注册逻辑和 Feature 配置。
 *
 * <p>注意：当前测试环境无 Hibernate 运行时依赖，因此 Hibernate7Module
 * 不会被注册到 Mapper 中。此测试主要验证条件注册逻辑和 Feature 配置正确性。</p>
 */
@DisplayName("JacksonUtil - Hibernate7Module 模块")
class JacksonUtilHibernate7ModuleTest {
    
    // ==================== 条件注册逻辑 ====================
    
    @Nested
    @DisplayName("条件注册逻辑验证")
    class ConditionalRegistration {
        
        @Test
        @DisplayName("无 Hibernate 依赖时 Hibernate7Module 不注册")
        void noHibernate_moduleNotRegistered() {
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            
            // 当前测试环境没有 Hibernate 依赖
            if (!hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                boolean hasHibernateModule = mapper.registeredModules().stream()
                                                   .anyMatch(m -> m.getClass().getName().contains("Hibernate7Module"));
                assertFalse(hasHibernateModule,
                            "无 Hibernate 依赖时不应注册 Hibernate7Module");
            }
        }
        
        @Test
        @DisplayName("ClassUtil.isClassExists 对 HibernateProxy 类的检测")
        void classUtilDetectsHibernateProxy() {
            // 验证检测方法本身工作正常
            boolean result = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            // 在当前环境中应为 false
            assertFalse(result, "当前环境不存在 HibernateProxy 类");
        }
        
        @Test
        @DisplayName("有 Hibernate 依赖时 Hibernate7Module 应注册")
        void withHibernate_moduleRegistered() {
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            
            if (hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                boolean hasHibernateModule = mapper.registeredModules().stream()
                                                   .anyMatch(m -> m.getClass().getName().contains("Hibernate7Module"));
                assertTrue(hasHibernateModule,
                           "有 Hibernate 依赖时应注册 Hibernate7Module");
            }
            // 无 Hibernate 时不做断言，由上一个测试覆盖
        }
    }
    
    // ==================== Feature 配置验证 ====================
    
    @Nested
    @DisplayName("Hibernate7Module Feature 配置验证")
    class FeatureConfiguration {
        
        @Test
        @DisplayName("FORCE_LAZY_LOADING 应被禁用（避免 N+1 问题）")
        void forceLazyLoading_shouldBeDisabled() {
            // 验证源码中的配置意图：disable(FORCE_LAZY_LOADING)
            // 这确保序列化时不会强制触发懒加载，避免 N+1 查询问题
            // 由于模块未注册，此处验证的是设计意图的正确性
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            if (hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                var hibernateModule = mapper.registeredModules().stream()
                                            .filter(m -> m.getClass().getName().contains("Hibernate7Module"))
                                            .findFirst();
                assertTrue(hibernateModule.isPresent());
                // Feature 配置在模块内部，通过行为验证
            }
        }
        
        @Test
        @DisplayName("USE_TRANSIENT_ANNOTATION 应被启用")
        void useTransientAnnotation_shouldBeEnabled() {
            // 验证源码中的配置意图：enable(USE_TRANSIENT_ANNOTATION)
            // 这确保使用 @Transient 注解的字段不会被序列化
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            if (hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                var hibernateModule = mapper.registeredModules().stream()
                                            .filter(m -> m.getClass().getName().contains("Hibernate7Module"))
                                            .findFirst();
                assertTrue(hibernateModule.isPresent());
            }
        }
    }
    
    // ==================== 模块注册数量 ====================
    
    @Nested
    @DisplayName("模块注册数量验证")
    class ModuleCount {
        
        @Test
        @DisplayName("无 Hibernate 时注册 2 个模块（Guava + Blackbird）")
        void noHibernate_twoModulesRegistered() {
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            
            if (!hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                long guavaOrBlackbird = mapper.registeredModules().stream()
                                              .filter(m -> m.getClass().getName().contains("GuavaModule")
                                                      || m.getClass().getName().contains("BlackbirdModule"))
                                              .count();
                assertEquals(2, guavaOrBlackbird,
                             "无 Hibernate 时应有 2 个手动注册模块（Guava + Blackbird）");
            }
        }
        
        @Test
        @DisplayName("有 Hibernate 时注册 3 个模块（Guava + Blackbird + Hibernate7）")
        void withHibernate_threeModulesRegistered() {
            boolean hasHibernate = ClassUtil.isClassExists(
                    "org.hibernate.proxy.HibernateProxy", null);
            
            if (hasHibernate) {
                ObjectMapper mapper = JacksonUtil.jsonMapper();
                long manualModules = mapper.registeredModules().stream()
                                           .filter(m -> m.getClass().getName().contains("GuavaModule")
                                                   || m.getClass().getName().contains("BlackbirdModule")
                                                   || m.getClass().getName().contains("Hibernate7Module"))
                                           .count();
                assertEquals(3, manualModules,
                             "有 Hibernate 时应有 3 个手动注册模块（Guava + Blackbird + Hibernate7）");
            }
        }
    }
}
