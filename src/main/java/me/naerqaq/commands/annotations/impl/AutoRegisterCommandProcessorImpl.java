package me.naerqaq.commands.annotations.impl;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.naerqaq.Js4Bukkit;
import me.naerqaq.annotations.processors.annotations.AutoAnnotationProcessor;
import me.naerqaq.annotations.processors.interfaces.AnnotatedClassProcessorInterface;
import me.naerqaq.commands.annotations.AutoRegisterCommand;
import me.naerqaq.utils.common.text.QuickUtils;
import me.naerqaq.utils.common.text.enums.ConsoleMessageTypeEnum;

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
