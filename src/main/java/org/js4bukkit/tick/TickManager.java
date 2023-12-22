package org.js4bukkit.tick;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.js4bukkit.thread.Scheduler;
import org.js4bukkit.thread.annotations.AutoStartTask;
import org.js4bukkit.thread.enums.SchedulerExecutionMode;
import org.js4bukkit.thread.enums.SchedulerTypeEnum;
import org.js4bukkit.tick.interfaces.PostTickListener;
import org.js4bukkit.tick.interfaces.PreTickListener;

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

    private short preTick;

    private short postTick;

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
