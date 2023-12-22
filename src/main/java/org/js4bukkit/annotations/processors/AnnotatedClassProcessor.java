package org.js4bukkit.annotations.processors;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.annotations.processors.annotations.AutoAnnotationProcessor;
import org.js4bukkit.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 该类是用于处理注解的工具类。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/7/31
 */
@UtilityClass
public class AnnotatedClassProcessor {
    /**
     * 获取带有指定注解的类集合。
     *
     * @param annotation 要查找的注解类的 Class 对象
     * @return 包含所有带有指定注解的类的集合
     */
    public static Set<Class<?>> getClassesWithAnnotation(Class<? extends Annotation> annotation) {
        // 创建 Reflections 对象扫描指定包下的所有类，并使用 getTypesAnnotatedWith 方法获取带有指定注解的类集合
        return new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(Js4Bukkit.class.getPackage().getName())))
                .getTypesAnnotatedWith(annotation);
    }

    /**
     * 处理所有带有 {@link AutoAnnotationProcessor} 注解的类。
     */
    @SuppressWarnings("unchecked")
    public static void processAnnotatedClasses() {
        getClassesWithAnnotation(AutoAnnotationProcessor.class).forEach(
                aClass -> processAnnotatedClasses((Class<? extends AnnotatedClassProcessorInterface>) aClass)
        );
    }

    /**
     * 处理指定 {@link AnnotatedClassProcessorInterface} 接口的实现类。
     *
     * @param clazz {@link AnnotatedClassProcessorInterface} 接口的实现类
     */
    @SneakyThrows
    public static void processAnnotatedClasses(Class<? extends AnnotatedClassProcessorInterface> clazz) {
        AnnotatedClassProcessorInterface annotatedClassProcessorInterface =
                clazz.getDeclaredConstructor().newInstance();

        AutoAnnotationProcessor autoAnnotationProcessor =
                clazz.getAnnotation(AutoAnnotationProcessor.class);

        Class<? extends Annotation> annotationClass =
                autoAnnotationProcessor.annotationClass();

        annotatedClassProcessorInterface.before();

        getClassesWithAnnotation(annotationClass).forEach(aClass -> {
            try {
                annotatedClassProcessorInterface.process(aClass);
            } catch (Exception exception) {
                annotatedClassProcessorInterface.exception(aClass, exception);
            }
        });

        annotatedClassProcessorInterface.after();
    }
}
