package org.js4bukkit.io.file.impl;

import de.leonhard.storage.Json;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.js4bukkit.io.file.ConfigurableFile;
import org.js4bukkit.io.file.interfaces.FileManagerInterface;
import org.js4bukkit.io.file.utils.IOUtils;

import java.io.File;

/**
 * 该类实现 {@link FileManagerInterface}，为 Json 管理类，单例模式。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/7/29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonManager implements FileManagerInterface<Json> {
    /**
     * 单例模式实例。
     */
    @Getter
    private static final JsonManager instance = new JsonManager();

    /**
     * {@inheritDoc}
     */
    @Override
    public Json get(File file, boolean inputStreamFromResource) {
        return ConfigurableFile.builder()
                .setFile(file)
                .setInputStreamFromResource(inputStreamFromResource)
                .build()
                .getSimplixBuilder()
                .createJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Json get(String name, String path, boolean inputStreamFromResource) {
        return get(new File(path, IOUtils.getFinalFileName(name, ".json")), inputStreamFromResource);
    }
}
