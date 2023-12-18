package me.naerqaq.script.interop.command;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.naerqaq.script.function.TriFunction;
import me.naerqaq.script.interfaces.InteropInterface;
import me.naerqaq.script.interop.command.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 允许 JavaScript 创建命令。
 *
 * @author 2000000 / NaerQAQ
 * @version 1.0
 * @since 2023/4/29
 */
@Getter
@Setter
@Accessors(chain = true)
public class CommandInterop implements InteropInterface {
    /**
     * 所有实例。
     */
    @Getter
    private static final ConcurrentLinkedQueue<CommandInterop> COMMAND_INTEROPS =
            new ConcurrentLinkedQueue<>();

    /**
     * 指令主名。
     */
    private String name;

    /**
     * 指令别名。
     */
    private String aliases;

    /**
     * 描述。
     */
    private String description;

    /**
     * 用于处理指令执行的代码。
     */
    private TriFunction<CommandSender, String, String[], Boolean> commandExecute;

    /**
     * 用于处理 Tab 补全的代码。
     */
    private TriFunction<CommandSender, String, String[], List<String>> tabComplete;

    /**
     * Command 实例。
     */
    private Command command = null;

    /**
     * 注册指令。
     */
    @Override
    public void register() {
        toCommand();
        CommandUtils.registerCommand(command);
        COMMAND_INTEROPS.add(this);
    }

    /**
     * 注销指令。
     */
    @Override
    public void unregister() {
        if (command != null) {
            CommandUtils.unregister(command);
            COMMAND_INTEROPS.remove(this);
        }
    }

    /**
     * 转换 Command 实例。
     */
    private void toCommand() {
        command = new Command(name) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                return commandExecute.apply(sender, commandLabel, args);
            }

            @NotNull
            @Override
            public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                return tabComplete.apply(sender, alias, args);
            }
        }.setAliases(Arrays.stream(aliases.split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()))
                .setDescription(description);
    }
}
