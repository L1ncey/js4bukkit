package org.js4bukkit.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Require;
import com.jonahseguin.drink.annotation.Sender;
import org.js4bukkit.commands.annotations.AutoRegisterCommand;
import org.js4bukkit.script.ScriptHandler;
import org.js4bukkit.script.objects.objects.ScriptPlugin;
import org.js4bukkit.script.thirdparty.ThirdPartyJarLoader;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.bukkit.command.CommandSender;

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
    @Command(
            name = "",
            desc = "Js4Bukkit command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void defaultCommand(
            @Sender CommandSender commandSender
    ) {
        QuickUtils.sendMessageByKey(commandSender, "default");
    }

    @Command(
            name = "help",
            desc = "Js4Bukkit help command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void help(
            @Sender CommandSender commandSender
    ) {
        QuickUtils.sendMessageByKey(commandSender, "help");
    }

    @Command(
            name = "list",
            desc = "Js4Bukkit list command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void list(
            @Sender CommandSender commandSender
    ) {
        Queue<ScriptPlugin> scriptPlugins =
                ScriptHandler.SCRIPT_PLUGINS;

        QuickUtils.sendMessageByKey(commandSender, "list-head");

        for (ScriptPlugin scriptPlugin : scriptPlugins) {
            QuickUtils.sendMessageByKey(
                    commandSender,
                    "list-plugins",

                    "<name>", scriptPlugin.getName(),
                    "<author>", scriptPlugin.getAuthor(),
                    "<version>", scriptPlugin.getVersion(),
                    "<description>", scriptPlugin.getDescription(),
                    "<folder>", scriptPlugin.getFolder()
            );
        }

        QuickUtils.sendMessageByKey(
                commandSender, "list-end",
                "<total>", String.valueOf(scriptPlugins.size())
        );
    }

    @Command(
            name = "libs",
            desc = "Js4Bukkit list libs command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void libs(
            @Sender CommandSender commandSender
    ) {
        Queue<File> loadedThirdPartyJarFiles =
                ThirdPartyJarLoader.LOADED_THIRD_PARTY_JAR_FILES;

        QuickUtils.sendMessageByKey(commandSender, "libs-head");

        for (File file : loadedThirdPartyJarFiles) {
            QuickUtils.sendMessageByKey(
                    commandSender,
                    "libs-list",

                    "<file_name>", file.getName(),
                    "<path>", file.getPath()
            );
        }

        QuickUtils.sendMessageByKey(
                commandSender, "libs-end",
                "<total>", String.valueOf(loadedThirdPartyJarFiles.size())
        );
    }

    @Command(
            name = "reload",
            desc = "Js4Bukkit reload command.",
            async = true
    )
    @Require("js4bukkit.admin")
    public void reload(
            @Sender CommandSender commandSender
    ) {
        ScriptHandler.reloadScripts();
    }
}
