package me.naerqaq.utils.common.text;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 效果工具类。
 *
 * @author L1ncey / 2000000
 * @version 1.0
 * @since 2023/10/8
 */
@UtilityClass
public class StringUtils {
    /**
     * 通过替换指定参数对来生成新的字符串，并进行颜色处理等。
     *
     * @param string 原始字符串
     * @param params 替换参数对，格式为 {@code key1, value1, key2, value2, ...}
     * @return 替换后的新字符串
     * @author 2000000
     */
    public static String handle(String string, String... params) {
        if (params == null) {
            return string;
        }

        for (int i = 0; i < params.length; i += 2) {
            string = string.replace(params[i], params[i + 1]);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * 通过替换指定参数对来生成新的字符串，并进行颜色处理等。
     *
     * @param strings 原始字符串列表
     * @param params  替换参数对，格式为 {@code key1, value1, key2, value2, ...}
     * @return 替换后的新字符串列表
     * @author 2000000
     */
    public static List<String> handle(List<String> strings, String... params) {
        return strings.stream()
                .map(string -> StringUtils.handle(string, params))
                .collect(Collectors.toList());
    }
}
