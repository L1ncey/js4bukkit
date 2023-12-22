package org.js4bukkit.thread.annotations;

import org.js4bukkit.thread.enums.SchedulerExecutionMode;
import org.js4bukkit.thread.enums.SchedulerTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标记一个自动开启的重复任务，使其能够自动注册。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/10/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoStartTask {
    /**
     * 延迟。
     *
     * @return 延迟
     */
    int delay();

    /**
     * 重复间隔。
     *
     * @return 重复间隔
     */
    int period();

    /**
     * 任务类型。
     *
     * @return 任务类型
     */
    SchedulerTypeEnum schedulerTypeEnum();

    /**
     * 任务执行模式。
     *
     * @return 任务执行模式
     */
    SchedulerExecutionMode schedulerExecutionMode();
}
