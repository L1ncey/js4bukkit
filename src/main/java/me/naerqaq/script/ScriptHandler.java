package me.naerqaq.script;

import de.leonhard.storage.Yaml;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.naerqaq.Js4Bukkit;
import me.naerqaq.io.config.ConfigManager;
import me.naerqaq.script.interop.command.CommandInterop;
import me.naerqaq.script.interop.listener.EasyEventListenerInterop;
import me.naerqaq.script.interop.listener.EventListenerInterop;
import me.naerqaq.script.interop.placeholder.PlaceholderInterop;
import me.naerqaq.script.objects.handler.CustomContextHandler;
import me.naerqaq.script.objects.handler.ScriptExecutorHandler;
import me.naerqaq.script.objects.objects.ScriptExecutor;
import me.naerqaq.script.objects.objects.ScriptPlugin;
import me.naerqaq.thread.Scheduler;
import me.naerqaq.thread.enums.SchedulerExecutionMode;
import me.naerqaq.thread.enums.SchedulerTypeEnum;
import me.naerqaq.utils.common.text.QuickUtils;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;

import java.io.File;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 脚本处理程序。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/10/12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScriptHandler {
    /**
     * 脚本所在文件夹。
     */
    public static final String SCRIPT_PATH =
            Js4Bukkit.getDataFolderAbsolutePath() + "/plugins/";

    /**
     * 获取上下文的函数名称。
     */
    public static final String GET_CONTEXT_FUNCTION = "getContext";

    /**
     * 所有脚本插件对象。
     */
    public static final Queue<ScriptPlugin> SCRIPT_PLUGINS =
            new ConcurrentLinkedQueue<>();

    /**
     * 注册脚本插件对象。
     */
    public static void registerScriptPlugins() {
        Yaml plugins = ConfigManager.getPlugins();

        SCRIPT_PLUGINS.addAll(
                plugins.singleLayerKeySet()
                        .stream()
                        .map(folder -> new ScriptPlugin().init(plugins, folder))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 获取所有脚本文件。
     *
     * @return 所有脚本文件
     */
    public static Queue<File> getScriptFiles() {
        return SCRIPT_PLUGINS.stream()
                .flatMap(scriptPlugin -> scriptPlugin.getScriptFiles().stream())
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    /**
     * 注册所有脚本。
     */
    @SneakyThrows
    public static void registerScripts() {
        registerScriptPlugins();

        Queue<File> scriptFiles = getScriptFiles();

        scriptFiles.forEach(scriptFile -> {
            String scriptName = scriptFile.getName();

            try {
                ScriptExecutorHandler.addScriptExecutor(
                        new ScriptExecutor(scriptFile)
                                .setName(scriptName)
                                .to(ScriptExecutor.class)
                );

                QuickUtils.sendMessage(
                        ConsoleMessageTypeEnum.NORMAL,
                        "&6Script successfully registered: <script_name>.",
                        "<script_name>", scriptName
                );
            } catch (Exception exception) {
                String message = exception.getMessage();

                QuickUtils.sendMessage(
                        ConsoleMessageTypeEnum.ERROR,
                        "Unable to register script: <script_name>, message: <message>.",
                        "<script_name>", scriptName,
                        "<message>", message
                );
            }
        });

        // 注册完毕后调用所有 Script 的 onLoad 函数
        ScriptExecutorHandler.invoke("onLoad");
    }

    /**
     * 卸载脚本。
     */
    public static void unloadScripts() {
        ScriptExecutorHandler.invoke("onUnload");

        // 指令注销
        CommandInterop.COMMAND_INTEROPS.forEach(
                CommandInterop::unregister
        );

        // Placeholder API 注销
        PlaceholderInterop.PLACEHOLDER_INTEROPS.forEach(
                PlaceholderInterop::unregister
        );

        // 简易监听器注销应在普通监听器之前
        EasyEventListenerInterop.EASY_EVENT_LISTENERS.forEach(
                EasyEventListenerInterop::unregister
        );

        // 监听器注销
        EventListenerInterop.EVENT_LISTENERS.forEach(
                EventListenerInterop::unregister
        );

        // 脚本清空
        ScriptExecutorHandler.SCRIPT_EXECUTORS.forEach(
                ScriptExecutorHandler::removeScriptExecutor
        );

        // 上下文清空
        CustomContextHandler.CUSTOM_CONTEXTS.forEach(
                CustomContextHandler::removeCustomContext
        );

        // 清空已注册的脚本插件
        SCRIPT_PLUGINS.clear();
    }

    /**
     * 重载脚本。
     *
     * <p>
     * 强制同步。
     * </p>
     */
    public static void reloadScripts() {
        // 同步
        new Scheduler()
                .setSchedulerTypeEnum(SchedulerTypeEnum.RUN)
                .setSchedulerExecutionMode(SchedulerExecutionMode.SYNC)
                .setRunnable(() -> {
                    ScriptExecutorHandler.invoke("onReload");

                    unloadScripts();
                    registerScripts();
                })
                .run();
    }
}
