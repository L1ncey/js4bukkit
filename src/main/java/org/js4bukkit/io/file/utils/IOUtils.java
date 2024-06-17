package org.js4bukkit.io.file.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
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
}
