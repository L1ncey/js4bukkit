package org.js4bukkit.thread.annotations.impl;

import org.bukkit.scheduler.BukkitRunnable;
import org.js4bukkit.annotations.processors.annotations.AutoAnnotationProcessor;
import org.js4bukkit.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import org.js4bukkit.thread.Scheduler;
import org.js4bukkit.thread.annotations.AutoStartTask;
import org.js4bukkit.thread.enums.SchedulerExecutionMode;
import org.js4bukkit.thread.enums.SchedulerTypeEnum;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

/**
 * {@link AutoStartTask} 注解处理器。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/29
 */
@AutoAnnotationProcessor(
        annotationClass = AutoStartTask.class
)
@SuppressWarnings("unused")
public class AutoStartTaskProcessorImpl implements AnnotatedClassProcessorInterface {
    /**
     * 处理前调用的方法。
     */
    @Override
    public void before() {
    }

    /**
     * 对带有指定注解的类进行处理。
     *
     * @param clazz 带有指定注解的类对象
     * @throws Exception 可能的抛出异常，将交由 {@link #exception(Class, Exception)} 方法处理
     */
    @Override
    public void process(Class<?> clazz) throws Exception {
        String className = clazz.getName();

        AutoStartTask annotation = clazz.getAnnotation(AutoStartTask.class);
        BukkitRunnable bukkitRunnable = (BukkitRunnable) clazz.getDeclaredConstructor().newInstance();

        int delay = annotation.delay();
        int period = annotation.period();
        SchedulerTypeEnum schedulerTypeEnum = annotation.schedulerTypeEnum();
        SchedulerExecutionMode schedulerExecutionMode = annotation.schedulerExecutionMode();

        new Scheduler()
                .setSchedulerTypeEnum(schedulerTypeEnum)
                .setSchedulerExecutionMode(schedulerExecutionMode)
                .setDelay(delay)
                .setPeriod(period)
                .setBukkitRunnable(bukkitRunnable)
                .run();

        QuickUtils.sendMessage(
                ConsoleMessageTypeEnum.NORMAL,
                "Task successfully started: <class_name>, execution mode: <execution_mode>, delay: <delay>, period: <period>.",
                "<class_name>", className,
                "<execution_mode>", schedulerExecutionMode.name(),
                "<delay>", String.valueOf(delay),
                "<period>", String.valueOf(period)
        );
    }

    /**
     * 处理 {@link #process(Class)} 方法抛出的异常。
     *
     * @param clazz     抛出异常的带有指定注解的类对象
     * @param exception 抛出的异常
     */
    @Override
    public void exception(Class<?> clazz, Exception exception) {
        String className = clazz.getName();
        String message = exception.getMessage();

        QuickUtils.sendMessage(
                ConsoleMessageTypeEnum.ERROR,
                "Unable to start task: <class_name>, message: <message>.",
                "<class_name>", className,
                "message", message
        );
    }

    /**
     * 当所有类都处理完毕后调用的方法
     */
    @Override
    public void after() {
    }
}
