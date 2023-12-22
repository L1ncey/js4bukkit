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

@SuppressWarnings("all")
@AutoAnnotationProcessor(
        annotationClass = AutoStartTask.class
)
public class AutoStartTaskProcessorImpl implements AnnotatedClassProcessorInterface {
    @Override
    public void before() {
    }

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

    @Override
    public void after() {
    }
}
