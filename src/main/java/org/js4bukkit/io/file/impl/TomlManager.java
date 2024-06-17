package org.js4bukkit.io.file.impl;

import de.leonhard.storage.Toml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.js4bukkit.io.file.ConfigurableFile;
import org.js4bukkit.io.file.interfaces.FileManagerInterface;
import org.js4bukkit.io.file.utils.IOUtils;

import java.io.File;

/**
 * 该类实现 {@link FileManagerInterface}，为 Toml 管理类，单例模式。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/7/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TomlManager implements FileManagerInterface<Toml> {
    /**
     * 单例模式实例。
     */
    @Getter
    private static final TomlManager instance = new TomlManager();

    /**
     * {@inheritDoc}
     */
    @Override
    public Toml get(File file, boolean inputStreamFromResource) {
        return ConfigurableFile.builder()
                .setFile(file)
                .setInputStreamFromResource(inputStreamFromResource)
                .build()
                .getSimplixBuilder()
                .createToml();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Toml get(String name, String path, boolean inputStreamFromResource) {
        return get(new File(path, IOUtils.getFinalFileName(name, ".toml")), inputStreamFromResource);
    }
}
