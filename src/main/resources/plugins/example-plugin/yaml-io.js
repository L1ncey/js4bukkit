// noinspection JSUnusedGlobalSymbols, JSUnresolvedReference

/**
 * @fileoverview
 * Yaml io example.
 *
 * @version        1.0
 * @since          2023/12/22
 * @author         NaerQAQ
 */


/**
 * Get the name of a specific context.
 *
 * @returns {string | null} - Returns the name of the context, or null if it's the context of this JS file
 */
function getContext() {
    return null;
}

// Thread scheduling
const Scheduler = Packages.org.js4bukkit.thread.Scheduler;
const SchedulerExecutionMode = Packages.org.js4bukkit.thread.enums.SchedulerExecutionMode;
const SchedulerTypeEnum = Packages.org.js4bukkit.thread.enums.SchedulerTypeEnum;

// Utilities
const QuickUtils = Packages.org.js4bukkit.utils.common.text.QuickUtils;
const StringUtils = Packages.org.js4bukkit.utils.common.text.StringUtils;
const ConsoleMessageTypeEnum = Packages.org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

// IO
const YamlManager = Packages.org.js4bukkit.io.file.impl.YamlManager;

// Js4Bukkit
const Js4Bukkit = Packages.org.js4bukkit.Js4Bukkit;

// Yaml
const Yaml = Packages.de.leonhard.storage.Yaml;

/**
 * Method automatically executed after the plugin script has been initialized.
 *
 * There is usually no concept of a main class; this method is called after the plugin script is loaded.
 */
function onLoad() {
    // Get instance
    const js4BukkitInstance = Js4Bukkit.getInstance();
    const yamlManagerInstance = YamlManager.getInstance();

    /*
     * If the file does not exist, it will be created
     * and then a Yaml(de.leonhard.storage.Yaml) object will be returned normally
     */
    const yamlObject = yamlManagerInstance.get(
        "example-config.yml",
        js4BukkitInstance.getDataFolderAbsolutePath() + "/plugins/example-plugin/configs/",
        false
    );

    // Performing these operations asynchronously is typically safe
    new Scheduler()
        .setSchedulerTypeEnum(SchedulerTypeEnum.RUN)
        .setSchedulerExecutionMode(SchedulerExecutionMode.ASYNC)
        .setRunnable(() => {
            yamlObject.set("key-int", 1);
            yamlObject.set("key-boolean", true);
            yamlObject.set("key-string", "value");
        })
        .run();

    /*
     * We support Yaml, Json, and Toml formats
     *
     * Everything is almost the same; the only changes are in
     * Packages.org.js4bukkit.io.file.impl.YamlManager / JsonManager / TomlManager
     *
     * and
     * Packages.de.leonhard.storage.Yaml / Json / Toml
     */
}

/**
 * Called when the plugin script is unloaded.
 */
function onUnload() {
}

/**
 * Called before the plugin script is reloaded.
 *
 * This method is called before reloading, followed by the normal script loading and the invocation of its onLoad method.
 */
function onReload() {
}