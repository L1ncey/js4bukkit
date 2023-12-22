package org.js4bukkit.io.config;

import de.leonhard.storage.Yaml;
import lombok.*;
import org.apache.commons.io.FileUtils;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.io.file.impl.YamlManager;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置文件管理类。
 *
 * <p>
 * 该类提供配置文件的创建、获取等等。
 * </p>
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/7/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigManager {
    static {
        loadConfigs("configs");
        loadConfigs("plugins");
    }

    /**
     * {@code config.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml config = YamlManager.getInstance().get(
            "config",
            Js4Bukkit.getDataFolderAbsolutePath() + "/configs/",
            true
    );

    /**
     * {@code language.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml language = YamlManager.getInstance().get(
            "language",
            Js4Bukkit.getDataFolderAbsolutePath() + "/configs/",
            true
    );

    /**
     * {@code plugins.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml plugins = YamlManager.getInstance().get(
            "plugins",
            Js4Bukkit.getDataFolderAbsolutePath() + "/configs/",
            true
    );

    /**
     * {@code maven-dependencies.yml} 配置文件实例。
     */
    @Getter
    private static final Yaml mavenDependencies = YamlManager.getInstance().get(
            "maven-dependencies",
            Js4Bukkit.getDataFolderAbsolutePath() + "/configs/",
            true
    );

    /**
     * 从 Jar 文件中加载指定文件夹内的配置文件。
     *
     * <p>
     * 该方法将配置文件从JAR文件复制到本地文件系统中的目标文件夹。
     * </p>
     *
     * <p>
     * 如果目标文件夹已经存在，则不执行任何操作。
     * </p>
     *
     * @param folder 文件夹名称
     */
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadConfigs(String folder) {
        // 路径构造
        String dataFolderAbsolutePath =
                Js4Bukkit.getDataFolderAbsolutePath();

        String targetFolder =
                dataFolderAbsolutePath + "/" + folder;

        File destinationFolder = new File(targetFolder);

        // 如果已经存在
        if (destinationFolder.exists()) {
            return;
        }

        // 读取 Jar
        String jarFilePath =
                Js4Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        @Cleanup JarFile jarFile = new JarFile(jarFilePath);

        Enumeration<JarEntry> entries = jarFile.entries();

        // 遍历条目
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            String entryName = entry.getName();

            // 如果不属于指定文件夹
            if (!entryName.startsWith(folder)) {
                continue;
            }

            // 删除文件夹前缀构造文件名
            String fileName = entryName.substring(folder.length());
            File destinationFile = new File(targetFolder, fileName);

            if (entry.isDirectory()) {
                destinationFile.mkdirs();
                continue;
            }

            FileUtils.copyInputStreamToFile(
                    jarFile.getInputStream(entry), destinationFile
            );
        }
    }
}
