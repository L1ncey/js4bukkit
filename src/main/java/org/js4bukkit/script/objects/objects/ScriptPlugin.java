package org.js4bukkit.script.objects.objects;

import de.leonhard.storage.Yaml;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.js4bukkit.basic.interfaces.ObjectAutoInit;
import org.js4bukkit.io.file.utils.IOUtils;
import org.js4bukkit.script.ScriptHandler;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 脚本插件对象。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/17
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ScriptPlugin implements ObjectAutoInit {
    /**
     * 文件夹。
     */
    private String folder;

    /**
     * 名称。
     */
    private String name;

    /**
     * 作者。
     */
    private String author;

    /**
     * 版本。
     */
    private String version;

    /**
     * 描述。
     */
    private String description;

    /**
     * 所有的脚本插件对象实例。
     */
    private Queue<File> scriptFiles =
            new ConcurrentLinkedQueue<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public ScriptPlugin init(Yaml yaml, String yamlKey) {
        setFolder(yamlKey);

        ObjectAutoInit.super.init(yaml, yamlKey);

        // 处理本文件后返回
        return setScriptFiles(
                IOUtils.getFiles(ScriptHandler.SCRIPT_PATH + folder).stream()
                        .filter(file -> file.getName().endsWith(".js"))
                        .collect(Collectors.toCollection(ConcurrentLinkedQueue::new))
        );
    }
}
