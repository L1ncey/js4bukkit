package me.lincey.script.interop.tick;

import me.lincey.tick.TickManager;
import me.lincey.tick.interfaces.PostTickListener;
import me.lincey.tick.interfaces.PreTickListener;
import me.naerqaq.script.interfaces.InteropInterface;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Lincey / NaerQAQ
 */
@SuppressWarnings("unused")
public class TickManagerInterop implements InteropInterface {
    private Runnable queuedPreTask;
    private Runnable queuedPostTask;

    private PreTickListener preTickListener;
    private PostTickListener postTickListener;

    private Consumer<Integer> preTickListenerConsumer;
    private Consumer<Integer> postTickListenerConsumer;

    @Override
    public void register() {
        Optional.ofNullable(queuedPreTask).ifPresent(TickManager.getQueuedPreTasks()::add);
        Optional.ofNullable(queuedPostTask).ifPresent(TickManager.getQueuedPostTasks()::add);

        Optional.ofNullable(preTickListenerConsumer).ifPresent(integerConsumer -> {
            preTickListener = integerConsumer::accept;
            TickManager.getPreTickListeners().add(preTickListener);
        });

        Optional.ofNullable(postTickListenerConsumer).ifPresent(integerConsumer -> {
            postTickListener = integerConsumer::accept;
            TickManager.getPostTickListeners().add(postTickListener);
        });
    }

    @Override
    public void unregister() {
        TickManager.getQueuedPreTasks().remove(queuedPreTask);
        TickManager.getQueuedPostTasks().remove(queuedPostTask);
        TickManager.getPreTickListeners().remove(preTickListener);
        TickManager.getPreTickListeners().remove(preTickListener);
    }
}
