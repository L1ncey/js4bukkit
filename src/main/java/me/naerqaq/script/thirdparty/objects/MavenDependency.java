package me.naerqaq.script.thirdparty.objects;

import de.leonhard.storage.Yaml;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.naerqaq.basic.interfaces.ObjectAutoInit;

import java.io.File;
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
    public static final ConcurrentLinkedQueue<MavenDependency> MAVEN_DEPENDENCIES =
            new ConcurrentLinkedQueue<>();

    /**
     * 对应 Jar 文件。
     */
    private File file;

    /**
     * 仓库地址、组ID、工件ID、版本。
     */
    private String repository, groupId, artifactId, version;

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
