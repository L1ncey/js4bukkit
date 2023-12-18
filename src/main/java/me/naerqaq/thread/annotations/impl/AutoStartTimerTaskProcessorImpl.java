package me.naerqaq.thread.annotations.impl;

import me.naerqaq.annotations.processors.annotations.AutoAnnotationProcessor;
import me.naerqaq.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import me.naerqaq.thread.Scheduler;
import me.naerqaq.thread.annotations.AutoStartTimerTask;
import me.naerqaq.thread.enums.SchedulerExecutionMode;
import me.naerqaq.thread.enums.SchedulerTypeEnum;
import me.naerqaq.utils.common.text.QuickUtils;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("all")
@AutoAnnotationProcessor(
        annotationClass = AutoStartTimerTask.class
)
public class AutoStartTimerTaskProcessorImpl implements AnnotatedClassProcessorInterface {
    @Override
    public void before() {
    }

    @Override
    public void process(Class<?> clazz) throws Exception {
        String className = clazz.getName();

        AutoStartTimerTask annotation = clazz.getAnnotation(AutoStartTimerTask.class);
        BukkitRunnable bukkitRunnable = (BukkitRunnable) clazz.getDeclaredConstructor().newInstance();

        int delay = annotation.delay();
        int period = annotation.period();
        SchedulerExecutionMode schedulerExecutionMode = annotation.schedulerExecutionMode();

        new Scheduler()
                .setSchedulerTypeEnum(SchedulerTypeEnum.TIMER)
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
