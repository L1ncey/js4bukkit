package org.js4bukkit.annotations.processors.interfaces;

import org.js4bukkit.annotations.processors.annotations.AutoAnnotationProcessor;
import org.js4bukkit.commands.annotations.impl.AutoRegisterCommandProcessorImpl;

/**
 * 实现该接口将可以使用 {@link AutoAnnotationProcessor} 注解进行自动化处理。
 *
 * @author NaerQAQ
 * @version 1.0
 * @see AutoAnnotationProcessor
 * @see AutoRegisterCommandProcessorImpl
 * @since 2023/12/16
 */
public interface AnnotatedClassProcessorInterface {
    /**
     * 处理前调用的方法。
     */
    void before();

    /**
     * 对带有指定注解的类进行处理。
     *
     * @param clazz 带有指定注解的类对象
     * @throws Exception 可能的抛出异常，将交由 {@link #exception(Class, Exception)} 方法处理
     */
    void process(Class<?> clazz) throws Exception;

    /**
     * 处理 {@link #process(Class)} 方法抛出的异常。
     *
     * @param clazz     抛出异常的带有指定注解的类对象
     * @param exception 抛出的异常
     */
    void exception(Class<?> clazz, Exception exception);

    /**
     * 当所有类都处理完毕后调用的方法
     */
    void after();
}
