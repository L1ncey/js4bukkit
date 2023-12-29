package org.js4bukkit.commands.annotations.impl;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.annotations.processors.annotations.AutoAnnotationProcessor;
import org.js4bukkit.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import org.js4bukkit.commands.annotations.AutoRegisterCommand;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

/**
 * {@link AutoRegisterCommand} 注解处理器。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/29
 */
@AutoAnnotationProcessor(
        annotationClass = AutoRegisterCommand.class
)
@SuppressWarnings("unused")
public class AutoRegisterCommandProcessorImpl implements AnnotatedClassProcessorInterface {
    /**
     * 处理前调用的方法。
     */
    @Override
    public void before() {
    }

    /**
     * 对带有指定注解的类进行处理。
     *
     * @param clazz 带有指定注解的类对象
     * @throws Exception 可能的抛出异常，将交由 {@link #exception(Class, Exception)} 方法处理
     */
    @Override
    public void process(Class<?> clazz) throws Exception {
        String className = clazz.getName();
        CommandService drink = Drink.get(Js4Bukkit.getInstance());

        Object object = clazz.getDeclaredConstructor().newInstance();

        AutoRegisterCommand autoRegisterCommand =
                clazz.getAnnotation(AutoRegisterCommand.class);

        String command = autoRegisterCommand.command();
        String[] aliases = autoRegisterCommand.aliases();

        if (aliases.length == 0) {
            drink.register(object, command);
        } else {
            drink.register(object, command, aliases);
        }

        QuickUtils.sendMessage(
                ConsoleMessageTypeEnum.NORMAL,
                "&6Command successfully registered: <class_name>.",
                "<class_name>", className
        );
    }

    /**
     * 处理 {@link #process(Class)} 方法抛出的异常。
     *
     * @param clazz     抛出异常的带有指定注解的类对象
     * @param exception 抛出的异常
     */
    @Override
    public void exception(Class<?> clazz, Exception exception) {
        String className = clazz.getName();
        String message = exception.getMessage();

        QuickUtils.sendMessage(
                ConsoleMessageTypeEnum.ERROR,
                "Unable to register command: <class_name>, message: <message>.",
                "<class_name>", className,
                "<message>", message
        );
    }

    /**
     * 当所有类都处理完毕后调用的方法
     */
    @Override
    public void after() {
        Drink.get(Js4Bukkit.getInstance()).registerCommands();
    }
}
