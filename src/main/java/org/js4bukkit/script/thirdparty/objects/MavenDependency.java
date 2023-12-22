package org.js4bukkit.script.thirdparty.objects;

import de.leonhard.storage.Yaml;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.js4bukkit.basic.interfaces.ObjectAutoInit;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Maven 依赖对象。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/18
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MavenDependency implements ObjectAutoInit {
    /**
     * 所有实例。
     */
    public static final Queue<MavenDependency> MAVEN_DEPENDENCIES =
            new ConcurrentLinkedQueue<>();

    /**
     * 对应 Jar 文件。
     */
    private File file;

    /**
     * 仓库地址。
     */
    private String repository;

    /**
     * 组ID.
     */
    private String groupId;

    /**
     * 工件ID.
     */
    private String artifactId;

    /**
     * 版本。
     */
    private String version;

    /**
     * 通过指定 Yaml 文件读取指定键值属性设置对象属性。
     *
     * @param yaml    指定 Yaml 文件
     * @param yamlKey 键值
     * @return 设置完成后的对象
     */
    @Override
    public MavenDependency init(Yaml yaml, String yamlKey) {
        return (MavenDependency) ObjectAutoInit.super.init(yaml, yamlKey);
    }
}
