package me.naerqaq.tick;

import lombok.Getter;
import me.naerqaq.Js4Bukkit;
import me.naerqaq.tick.inter.PostTickListener;
import me.naerqaq.tick.inter.PreTickListener;
import me.naerqaq.utils.common.text.QuickUtils;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;
import org.bukkit.Bukkit;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Lincey
 */
public class TickManager {

    @Getter
    private static final TickManager Instance = new TickManager();

    @Getter
    private Deque<Runnable> queuedPreTasks = new LinkedList<>();
    @Getter
    private Deque<Runnable> queuedPostTasks = new LinkedList<>();

    @Getter
    private Set<PreTickListener> preTickListeners = new HashSet<>();
    @Getter
    private Set<PostTickListener> postTickListeners = new HashSet<>();

    private int serverTick;
    private short preTick, postTick;

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Js4Bukkit.getInstance(), () -> {
            ++this.serverTick;

            this.preTick = (short) ((this.serverTick * 2) + 1 % Short.MIN_VALUE);
            this.postTick = (short) ((this.serverTick * 2) % Short.MIN_VALUE);

            Task: {
                if (queuedPreTasks.isEmpty())
                    break Task;

                if (queuedPreTasks.size() > 1){
                    QuickUtils.sendMessage(ConsoleMessageTypeEnum.ERROR,
                            "&4More than one queued task found! \nIts kind of unstable!");

//                    very unstable trust me
                    for (Runnable r : queuedPreTasks)
                        r.run();

                    break Task;
                }

                queuedPreTasks.pop().run();
            }

            for (PreTickListener listener : preTickListeners) {
                listener.preTick(this.preTick);
            }
        }, 0L, 1L);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Js4Bukkit.getInstance(), () -> {

            Task: {
                if (queuedPostTasks.isEmpty())
                    break Task;

                if (queuedPostTasks.size() > 1){
                    QuickUtils.sendMessage(ConsoleMessageTypeEnum.ERROR,
                            "&4More than one queued task found! \nIts kind of unstable!");

//                    very unstable trust me
                    for (Runnable r : queuedPostTasks)
                        r.run();

                    break Task;
                }

                queuedPostTasks.pop().run();
            }

            for (PostTickListener listener : postTickListeners) {
                listener.preTick(this.postTick);
            }
        }, 1L, 1L);
    }

    public short nextPreTick() {
        return (short) (preTick + 1);
    }

    public short nextPostTick() {
        return (short) (postTick + 1);
    }
}
