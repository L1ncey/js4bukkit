package org.js4bukkit.io.file.utils;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.js4bukkit.Js4Bukkit;

import java.io.File;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * I/O 工具类。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/10/9
 */
@UtilityClass
public class IOUtils {
    /**
     * 获取指定文件夹内的所有文件，包括子文件夹。
     *
     * @param folderPath 文件夹路径
     * @return 包含所有文件的并发链接队列
     */
    public static Queue<File> getFiles(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            return new ConcurrentLinkedQueue<>();
        }

        File[] files = folder.listFiles();

        if (files == null) {
            return new ConcurrentLinkedQueue<>();
        }

        return Stream.of(files)
                .flatMap(file -> file.isFile() ? Stream.of(file) : getFiles(file.getAbsolutePath()).stream())
                .collect(ConcurrentLinkedQueue::new, ConcurrentLinkedQueue::offer, ConcurrentLinkedQueue::addAll);
    }

    /**
     * 获取最终文件名。
     *
     * @param name      文件名
     * @param extension 文件拓展名
     * @return 包含后缀的最终文件名
     */
    public static String getFinalFileName(String name, String extension) {
        return name.endsWith(extension) ? name : name + extension;
    }

    /**
     * 从 Jar 文件中加载指定文件夹内的配置文件。
     *
     * <p>
     * 该方法将配置文件从 Jar 文件复制到本地文件系统中的目标文件夹。如果目标文件夹已经存在，则不执行任何操作。
     * </p>
     *
     * @param folder 文件夹名称
     */
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void loadConfigs(String folder) {
        // 路径构造
        String targetFolder =
                Js4Bukkit.getDataFolderAbsolutePath() + "/" + folder;

        File destinationFolder = new File(targetFolder);

        // 如果已经存在
        if (destinationFolder.exists()) {
            return;
        }

        // 读取 Jar
        String jarFilePath = Js4Bukkit.class
                .getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        @Cleanup
        JarFile jarFile = new JarFile(jarFilePath);
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
