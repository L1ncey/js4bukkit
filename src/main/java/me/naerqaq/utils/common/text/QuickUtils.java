package me.naerqaq.utils.common.text;

import de.leonhard.storage.Yaml;
import lombok.experimental.UtilityClass;
import me.naerqaq.io.config.ConfigManager;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * 快捷工具类。
 *
 * @author L1ncey / NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/10/8
 */
@UtilityClass
public class QuickUtils {
    /**
     * 向控制台发送输出消息。
     *
     * @param consoleMessageTypeEnum 信息类型
     * @param message                要处理的发向控制台的字符串
     * @param params                 替换的可选参数
     */
    public static void sendMessage(ConsoleMessageTypeEnum consoleMessageTypeEnum, String message, String... params) {
        StringBuilder prefix = new StringBuilder()
                .append("&f[&6Js4Bukkit&f] ");

        switch (consoleMessageTypeEnum) {
            case DEBUG:
                prefix.append("&e[DEBUG] &e");
                break;

            case ERROR:
                prefix.append("&4[ERROR] &c");
                break;

            case NORMAL:
                prefix.append("&f");
                break;

            default:
            case NO_PREFIX:
                prefix.setLength(0);
                break;
        }

        Bukkit.getConsoleSender().sendMessage(
                StringUtils.handle(prefix + message, params)
        );
    }

    /**
     * 读取语言配置文件内对应键值字符串列表，向命令发送者发送输出消息。
     *
     * @param commandSender 命令发送者
     * @param key           键值
     * @param params        替换的可选参数
     */
    public static void sendMessageByKey(CommandSender commandSender, String key, String... params) {
        Yaml yaml = ConfigManager.getLanguage();

        List<String> messages = StringUtils.handle(
                yaml.getStringList(key), params
        );

        messages.forEach(commandSender::sendMessage);
    }

    /**
     * 读取语言配置文件内对应键值字符串列表，向控制台发送输出消息。
     *
     * @param consoleMessageTypeEnum 信息类型
     * @param key                    键值
     * @param params                 替换的可选参数
     */
    public static void sendMessageByKey(ConsoleMessageTypeEnum consoleMessageTypeEnum, String key, String... params) {
        Yaml yaml = ConfigManager.getLanguage();

        List<String> messages = StringUtils.handle(
                yaml.getStringList(key), params
        );

        messages.forEach(message -> sendMessage(consoleMessageTypeEnum, message));
    }
}
