package org.js4bukkit.commands.annotations.impl;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import org.js4bukkit.Js4Bukkit;
import org.js4bukkit.annotations.processors.annotations.AutoAnnotationProcessor;
import org.js4bukkit.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import org.js4bukkit.commands.annotations.AutoRegisterCommand;
import org.js4bukkit.utils.common.text.QuickUtils;
import org.js4bukkit.utils.common.text.enums.ConsoleMessageTypeEnum;

@SuppressWarnings("all")
@AutoAnnotationProcessor(
        annotationClass = AutoRegisterCommand.class
)
public class AutoRegisterCommandProcessorImpl implements AnnotatedClassProcessorInterface {
    @Override
    public void before() {
    }

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

    @Override
    public void after() {
        Drink.get(Js4Bukkit.getInstance()).registerCommands();
    }
}
