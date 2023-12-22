package org.js4bukkit.script.interop.placeholder;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.js4bukkit.script.interfaces.InteropInterface;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiFunction;

/**
 * 允许 JavaScript 使用 Placeholder API 拓展。
 *
 * @author 2000000 / NaerQAQ
 * @version 1.0
 * @since 2023/4/28
 */
@Getter
@Setter
@Accessors(chain = true)
public class PlaceholderInterop implements InteropInterface {
    /**
     * 所有实例。
     */
    public static final Queue<PlaceholderInterop> PLACEHOLDER_INTEROPS =
            new ConcurrentLinkedQueue<>();

    /**
     * 作者。
     */
    private String author;

    /**
     * 版本。
     */
    private String version;

    /**
     * 标识符。
     */
    private String identifier;

    /**
     * 用于处理占位符执行的函数。
     */
    private BiFunction<OfflinePlayer, String, String> executor;

    /**
     * PlaceholderExpansion 实例。
     */
    private PlaceholderExpansion placeholderExpansion = null;

    /**
     * 注册 PlaceholderExpansion 实例。
     */
    @Override
    public void register() {
        boolean succeed = toPlaceholderExpansion();

        if (!succeed) {
            return;
        }

        PLACEHOLDER_INTEROPS.add(this);
        placeholderExpansion.register();
    }

    /**
     * 注销 PlaceholderExpansion 实例。
     * 低版本 Placeholder API 没有 unregister 方法，捕获异常以取消注销。
     */
    @Override
    public void unregister() {
        if (placeholderExpansion == null) {
            return;
        }

        try {
            placeholderExpansion.unregister();
            PLACEHOLDER_INTEROPS.remove(this);
        } catch (Throwable throwable) {
            QuickUtils.sendMessage(
                    ConsoleMessageTypeEnum.ERROR,
                    "&ePlaceholder API&c logout exception. Is this the latest version? Please try updating it: &ehttps://www.spigotmc.org/resources/placeholderapi.6245\n"
            );
        }
    }

    /**
     * 转换 PlaceholderExpansion 实例。
     */
    private boolean toPlaceholderExpansion() {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            QuickUtils.sendMessage(
                    ConsoleMessageTypeEnum.ERROR,
                    "&cYou haven't installed the &ePlaceholder API&c. If needed, please download it: &ehttps://www.spigotmc.org/resources/placeholderapi.6245\n"
            );

            return false;
        }

        placeholderExpansion = new PlaceholderExpansion() {
            @Override
            public @NotNull String getIdentifier() {
                return identifier;
            }

            @Override
            public @NotNull String getAuthor() {
                return author;
            }

            @Override
            public @NotNull String getVersion() {
                return version;
            }

            @Override
            public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
                return executor.apply(player, params);
            }
        };

        return true;
    }
}
