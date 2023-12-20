package me.naerqaq;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.naerqaq.annotations.processors.AnnotatedClassProcessor;
import me.naerqaq.io.config.ConfigManager;
import me.naerqaq.script.ScriptHandler;
import me.naerqaq.script.thirdparty.MavenDependencyLoader;
import me.naerqaq.script.thirdparty.ThirdPartyJarLoader;
import me.naerqaq.utils.common.text.QuickUtils;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * 该类继承 {@link JavaPlugin}，插件主类。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/10/7
 */
public class Js4Bukkit extends JavaPlugin {
    /**
     * 实例。
     */
    @Getter
    @Setter
    private static Js4Bukkit instance;

    /**
     * 服务器版本。
     */
    @Getter
    @Setter
    private static Double serverVersion;

    /**
     * 插件配置文件夹路径。
     */
    @Getter
    @Setter
    private static String dataFolderAbsolutePath;

    @Getter
    @Setter
    private static String nmsVersion;

    /**
     * 插件开启。
     *
     * @author 2000000
     */
    @Override
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void onEnable() {
        setInstance(this);
        setDataFolderAbsolutePath(getDataFolder().getAbsolutePath());

        String serverPackage =
                getServer().getClass().getPackage().getName();

        setNmsVersion(
                serverPackage.replace(".", ",").split(",")[3]
        );

        String[] arrayVersion = StringUtils.substringsBetween(
                serverPackage, ".v", "_R"
        );

        String stringVersion = Arrays.toString(arrayVersion)
                .replace("_", "0")
                .replace("[", "")
                .replace("]", "");

        setServerVersion(
                Double.parseDouble(stringVersion)
        );

        // 配置文件与注解处理
        ConfigManager.getConfig();
        AnnotatedClassProcessor.processAnnotatedClasses();

        // Maven 依赖与外部依赖加载
        MavenDependencyLoader.download();
        ThirdPartyJarLoader.load();

        // 脚本注册
        ScriptHandler.registerScripts();
    }

    /**
     * 插件卸载。
     *
     * @author 2000000
     */
    @Override
    public void onDisable() {
        ScriptHandler.unloadScripts();

        QuickUtils.sendMessage(
                ConsoleMessageTypeEnum.NORMAL,
                "Plugin has been disabled."
        );
    }
}