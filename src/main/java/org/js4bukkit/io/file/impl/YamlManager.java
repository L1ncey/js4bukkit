package org.js4bukkit.io.file.impl;

import de.leonhard.storage.Yaml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.js4bukkit.io.file.ConfigurableFile;
import org.js4bukkit.io.file.interfaces.FileManagerInterface;

import java.io.File;

/**
 * 该类实现 {@link FileManagerInterface}，为 Yaml 管理类，单例模式。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/7/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlManager implements FileManagerInterface<Yaml> {
    /**
     * 单例模式实例。
     */
    @Getter
    private static final YamlManager instance = new YamlManager();

    /**
     * {@code Yaml} 文件后缀常量。
     */
    private static final String YAML_FILE_EXTENSION = ".yml";

    /**
     * {@inheritDoc}
     */
    @Override
    public Yaml get(File file) {
        return ConfigurableFile.builder()
                .setFile(file)
                .setInputStreamFromResource(true)
                .build()
                .getSimplixBuilder()
                .createYaml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Yaml get(String name, String path, boolean inputStreamFromResource) {
        return get(new File(path, name + YAML_FILE_EXTENSION));
    }
}
