package org.js4bukkit.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.js4bukkit.commands.annotations.AutoRegisterCommand;
import org.js4bukkit.script.ScriptHandler;
import org.js4bukkit.script.objects.objects.ScriptPlugin;
import org.js4bukkit.script.thirdparty.ThirdPartyJarLoader;
import org.js4bukkit.utils.common.text.QuickUtils;

import java.io.File;
import java.util.Queue;

/**
 * 脚本命令。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/10/12
 */
@AutoRegisterCommand(
        command = "js4bukkit"
)
@SuppressWarnings("unused")
public class Js4BukkitCommand {
    /**
     * 主命令。
     *
     * @param commandSender 执行者
     */
    @Command(
            name = "",
            desc = "Js4Bukkit command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void defaultCommand(@Sender CommandSender commandSender) {
        QuickUtils.sendMessageByKey(commandSender, "default");
    }

    /**
     * {@code help} 命令。
     *
     * @param commandSender 执行者
     */
    @Command(
            name = "help",
            desc = "Js4Bukkit help command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void help(@Sender CommandSender commandSender) {
        QuickUtils.sendMessageByKey(commandSender, "help");
    }

    /**
     * {@code list} 命令。
     *
     * @param commandSender 执行者
     */
    @Command(
            name = "list",
            desc = "Js4Bukkit list command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void list(@Sender CommandSender commandSender) {
        Queue<ScriptPlugin> scriptPlugins =
                ScriptHandler.SCRIPT_PLUGINS;

        QuickUtils.sendMessageByKey(commandSender, "list-head");

        scriptPlugins.forEach(scriptPlugin -> QuickUtils.sendMessageByKey(
                commandSender,
                "list-plugins",

                "<name>", scriptPlugin.getName(),
                "<author>", scriptPlugin.getAuthor(),
                "<version>", scriptPlugin.getVersion(),
                "<description>", scriptPlugin.getDescription(),
                "<folder>", scriptPlugin.getFolder()
        ));

        QuickUtils.sendMessageByKey(
                commandSender, "list-end",
                "<total>", String.valueOf(scriptPlugins.size())
        );
    }

    /**
     * {@code libs} 命令。
     *
     * @param commandSender 执行者
     */
    @Command(
            name = "libs",
            desc = "Js4Bukkit list libs command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void libs(@Sender CommandSender commandSender) {
        Queue<File> loadedThirdPartyJarFiles =
                ThirdPartyJarLoader.LOADED_THIRD_PARTY_JAR_FILES;

        QuickUtils.sendMessageByKey(commandSender, "libs-head");

        loadedThirdPartyJarFiles.forEach(file -> QuickUtils.sendMessageByKey(
                commandSender,
                "libs-list",

                "<file_name>", file.getName(),
                "<path>", file.getPath()
        ));

        QuickUtils.sendMessageByKey(
                commandSender, "libs-end",
                "<total>", String.valueOf(loadedThirdPartyJarFiles.size())
        );
    }

    /**
     * {@code reload} 命令。
     *
     * @param commandSender 执行者
     */
    @Command(
            name = "reload",
            desc = "Js4Bukkit reload command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void reload(@Sender CommandSender commandSender) {
        ScriptHandler.reloadScripts();
    }
}
