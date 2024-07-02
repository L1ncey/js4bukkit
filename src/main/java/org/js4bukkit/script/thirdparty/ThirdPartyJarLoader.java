package org.js4bukkit.script.thirdparty;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.io.file.utils.IOUtils;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于通过反射将 Jar 文件动态加载到当前类加载器中。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThirdPartyJarLoader {
    /**
     * 第三方 Jar 所在的文件夹路径。
     */
    public static final String THIRD_PARTY_JARS_FOLDER =
            Js4Bukkit.getDataFolderAbsolutePath() + "/libs/";

    /**
     * 已加载的第三方 Jar.
     */
    public static final Queue<File> LOADED_THIRD_PARTY_JAR_FILES =
            new ConcurrentLinkedQueue<>();

    /**
     * {@link URLClassLoader} 中的 ucp 字段。
     */
    private static URLClassLoader ucp;

    /**
     * {@link URLClassLoader} 中的 addURL 方法。
     */
    private static Method addURLMethod;

    static {
        init();
    }

    /**
     * 加载 libs 文件夹下所有 Jar 文件。
     */
    public static void load() {
        IOUtils.getFiles(THIRD_PARTY_JARS_FOLDER).stream()
                .filter(file -> file.getName().endsWith(".jar"))
                .forEach(ThirdPartyJarLoader::load);
    }

    /**
     * 将指定的 Jar 文件加载到类加载器中。
     *
     * @param file 要加载的 Jar 文件
     */
    @SneakyThrows
    public static void load(File file) {
        addURLMethod.invoke(
                ucp, file.toURI().toURL()
        );

        LOADED_THIRD_PARTY_JAR_FILES.add(file);

        QuickUtils.sendMessageByKey(
                ConsoleMessageTypeEnum.NORMAL,
                "libs-load-done",
                "<path>", file.getPath(),
                "<file_name>", file.getName()
        );
    }

    /**
     * 初始化反射所需的字段和方法。
     */
    @SneakyThrows
    private static void init() {
        // 获取当前类的类加载器
        ClassLoader classLoader = ThirdPartyJarLoader.class.getClassLoader();

        // 获取 URLClassLoader 中的 ucp 字段
        Field ucpField = URLClassLoader.class.getDeclaredField("ucp");
        ucpField.setAccessible(true);

        ucp = (URLClassLoader) classLoader;

        // 获取 URLClassLoader 中的 addURL 方法
        addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURLMethod.setAccessible(true);
    }
}
