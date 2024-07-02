package org.js4bukkit.io.config;

import de.leonhard.storage.Yaml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.io.file.impl.YamlManager;

/**
 * 配置文件管理类，该类提供配置文件的创建、获取等等。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/7/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigManager {
    /**
     * 配置文件所在文件夹。
     */
    public static final String CONFIG_PATH = Js4Bukkit.getDataFolderAbsolutePath() + "/configs/";

    /**
     * {@code language.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml language = YamlManager.getInstance().get(
            "language",
            CONFIG_PATH,
            true
    );

    /**
     * {@code plugins.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml plugins = YamlManager.getInstance().get(
            "plugins",
            CONFIG_PATH,
            true
    );

    /**
     * {@code maven-dependencies.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml mavenDependencies = YamlManager.getInstance().get(
            "maven-dependencies",
            CONFIG_PATH,
            true
    );
}
