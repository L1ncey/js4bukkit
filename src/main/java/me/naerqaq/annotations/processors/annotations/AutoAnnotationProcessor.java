package me.naerqaq.annotations.processors.annotations;

import me.naerqaq.annotations.processors.interfaces.AnnotatedClassProcessorInterface;

import java.lang.annotation.*;

/**
 * 该注解用于标记需要自动处理注解的具体实现类。
 *
 * <p>
 * 使用该注解的类必须实现 {@link AnnotatedClassProcessorInterface} 接口。
 * </p>
 *
 * @author NaerQAQ
 * @version 1.0
 * @see AnnotatedClassProcessorInterface
 * @since 2023/12/16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoAnnotationProcessor {
    /**
     * 需要处理的注解类对象。
     *
     * @return 需要处理的注解类对象
     */
    Class<? extends Annotation> annotationClass();
}
