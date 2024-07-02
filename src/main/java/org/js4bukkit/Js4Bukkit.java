package org.js4bukkit;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.js4bukkit.annotations.processors.AnnotatedClassProcessor;
import org.js4bukkit.io.config.ConfigManager;
import org.js4bukkit.io.file.utils.IOUtils;
import org.js4bukkit.script.ScriptHandler;
import org.js4bukkit.script.thirdparty.MavenDependencyLoader;
import org.js4bukkit.script.thirdparty.ThirdPartyJarLoader;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

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

        // 配置文件
        ConfigManager.getLanguage();
        IOUtils.loadConfigs("plugins");

        // 注解处理
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