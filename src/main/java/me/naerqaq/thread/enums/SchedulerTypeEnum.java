package me.naerqaq.thread.enums;

/**
 * 任务类型枚举类。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/10/9
 */
public enum SchedulerTypeEnum {
    /**
     * 直接运行。
     */
    RUN,

    /**
     * 重复运行。
     */
    TIMER,

    /**
     * 延迟运行。
     */
    LATER,

    /**
     * 新建线程
     */
    NEW_THREAD
}
