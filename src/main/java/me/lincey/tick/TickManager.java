package me.lincey.tick;

import lombok.Getter;
import me.lincey.tick.interfaces.PostTickListener;
import me.lincey.tick.interfaces.PreTickListener;
import me.naerqaq.thread.Scheduler;
import me.naerqaq.thread.annotations.AutoStartTask;
import me.naerqaq.thread.enums.SchedulerExecutionMode;
import me.naerqaq.thread.enums.SchedulerTypeEnum;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Lincey
 */
@AutoStartTask(
        delay = 0, period = 0,
        schedulerTypeEnum = SchedulerTypeEnum.RUN,
        schedulerExecutionMode = SchedulerExecutionMode.SYNC
)
@SuppressWarnings("unused")
public class TickManager extends BukkitRunnable {
    @Getter
    private static final Deque<Runnable> queuedPreTasks = new LinkedList<>();

    @Getter
    private static final Deque<Runnable> queuedPostTasks = new LinkedList<>();

    @Getter
    private static final Set<PreTickListener> preTickListeners = new HashSet<>();

    @Getter
    private static final Set<PostTickListener> postTickListeners = new HashSet<>();

    private int serverTick;

    private short preTick, postTick;

    @Override
    public void run() {
        new Scheduler()
                .setDelay(0)
                .setPeriod(1)
                .setSchedulerTypeEnum(SchedulerTypeEnum.TIMER)
                .setSchedulerExecutionMode(SchedulerExecutionMode.SYNC)
                .setRunnable(() -> {
                    ++this.serverTick;

                    this.preTick =
                            (short) ((this.serverTick * 2) + 1 % Short.MIN_VALUE);

                    this.postTick =
                            (short) ((this.serverTick * 2) % Short.MIN_VALUE);

                    queuedPreTasks.forEach(Runnable::run);
                    preTickListeners.forEach(preTickListener -> preTickListener.preTick(this.preTick));
                })
                .run();

        new Scheduler()
                .setDelay(1)
                .setPeriod(1)
                .setSchedulerTypeEnum(SchedulerTypeEnum.RUN)
                .setSchedulerExecutionMode(SchedulerExecutionMode.SYNC)
                .setRunnable(() -> {
                    queuedPostTasks.forEach(Runnable::run);
                    postTickListeners.forEach(postTickListener -> postTickListener.preTick(this.postTick));
                })
                .run();
    }

    public short nextPreTick() {
        return (short) (preTick + 1);
    }

    public short nextPostTick() {
        return (short) (postTick + 1);
    }
}
