package me.naerqaq.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标记指令类，使其能够自动注册。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/10/8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRegisterCommand {
    /**
     * 指令。
     *
     * @return 指令
     */
    String command();

    /**
     * 别名。
     *
     * @return 别名
     */
    String[] aliases() default {};
}
