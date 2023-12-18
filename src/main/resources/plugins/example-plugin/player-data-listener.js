// noinspection JSUnusedGlobalSymbols, JSUnresolvedReference

/**
 * @fileoverview
 * Player data processing listener.
 *
 * @version        1.0
 * @since          2023/10/12
 * @author         NaerQAQ
 */


/*
 * Usually, all script plugins have a preloading process,
 * which significantly improves efficiency when a method is repeatedly called.
 *
 * If you want to execute certain methods during the preloading phase, simply call them directly.
 *
 * Eg: QuickUtils.sendMessage(ConsoleMessageTypeEnum.NORMAL, "onPreload");
 */


/**
 * Get the name of a specific context.
 *
 * @returns {string | null} - Returns the name of the context, or null if it's the context of this JS file
 */
function getContext() {
    return null;
}

// Bukkit
const EventPriority = Packages.org.bukkit.event.EventPriority;
const PlayerJoinEvent = Packages.org.bukkit.event.player.PlayerJoinEvent;

// Event listening
const EventListenerInterop = Packages.me.naerqaq.script.interop.listener.EventListenerInterop;
const EventRegistrationInfo = Packages.me.naerqaq.script.interop.listener.objects.EventRegistrationInfo;

// Placeholder API
const PlaceholderInterop = Packages.me.naerqaq.script.interop.placeholder.PlaceholderInterop;

// Thread scheduling
const Scheduler = Packages.me.naerqaq.thread.Scheduler;
const SchedulerExecutionMode = Packages.me.naerqaq.thread.enums.SchedulerExecutionMode;
const SchedulerTypeEnum = Packages.me.naerqaq.thread.enums.SchedulerTypeEnum;

// Utilities
const QuickUtils = Packages.me.naerqaq.utils.common.text.QuickUtils;
const StringUtils = Packages.me.naerqaq.utils.common.text.StringUtils;
const ConsoleMessageTypeEnum = Packages.me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;

/**
 * Method automatically executed after the plugin script has been initialized.
 *
 * There is usually no concept of a main class; this method is called after the plugin script is loaded.
 */
function onLoad() {
    QuickUtils.sendMessage(
        ConsoleMessageTypeEnum.NORMAL,
        "&aThe JS4Bukkit demo plugin has been successfully loaded. Path: js4bukkit/plugins/example-plugin"
    );

    // Player join listener information
    const playerJoinEventRegistrationInfo = new EventRegistrationInfo()
        .setEventClass(PlayerJoinEvent)
        .setEventPriority(EventPriority.LOWEST);

    // Player join event
    new EventListenerInterop()
        .setExecutor(onPlayerJoin)
        .setEventRegistrationInfo(playerJoinEventRegistrationInfo)
        .register();

    // Placeholder API registration
    new PlaceholderInterop()
        .setAuthor("Js4Bukkit")
        .setVersion("1.0")
        .setIdentifier("js4bukkit")
        .setExecutor(handlePlaceholder)
        .register();
}

/**
 * Called when the plugin script is unloaded.
 */
function onUnload() {
    QuickUtils.sendMessage(
        ConsoleMessageTypeEnum.NORMAL,
        "&aThe JS4Bukkit demo plugin has been successfully unloaded."
    );
}

/**
 * Called before the plugin script is reloaded.
 *
 * This method is called before reloading, followed by the normal script loading and the invocation of its onLoad method.
 */
function onReload() {
    QuickUtils.sendMessage(
        ConsoleMessageTypeEnum.NORMAL,
        "&aThe JS4Bukkit demo plugin has been successfully reloaded."
    );
}

/**
 * Handles the player join event.
 *
 * @param event - PlayerJoinEvent
 */
function onPlayerJoin(event) {
    const player = event.getPlayer();

    // Asynchronously execute, sending a "Hi" message to the player
    new Scheduler()
        .setSchedulerTypeEnum(SchedulerTypeEnum.RUN)
        .setSchedulerExecutionMode(SchedulerExecutionMode.ASYNC)
        .setRunnable(() => player.sendMessage(StringUtils.handle("&cHi")))
        .run();
}

/**
 * Handles Placeholder API.
 *
 * @param player - The player object requesting the placeholder value, possibly null
 * @param string - The placeholder identifier for a specific value
 * @returns {string | *} The value of the requested identifier
 */
function handlePlaceholder(player, string) {
    // If it starts with "github," return the URL; otherwise, return the input string
    return string.toLowerCase().startsWith("github") ?
        "https://github.com/NaerQAQ/js4bukkit" : string
}
