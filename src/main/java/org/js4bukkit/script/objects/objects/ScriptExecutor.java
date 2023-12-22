package org.js4bukkit.script.objects.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;
import org.js4bukkit.basic.impl.ObjectNameImpl;
import org.js4bukkit.script.ScriptHandler;
import org.js4bukkit.script.objects.handler.CustomContextHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 脚本执行器。
 *
 * @author NaerQAQ / 2000000
 * @version 1.0
 * @since 2023/10/12
 */
@Getter
@Setter
@Accessors(chain = true)
public class ScriptExecutor extends ObjectNameImpl {
    /**
     * 脚本文件。
     */
    private final File scriptFile;

    /**
     * 脚本文件文件名。
     */
    private final String fileName;

    /**
     * 脚本内容。
     */
    private final String scriptCode;

    /**
     * {@link Context} 对象。
     */
    private org.js4bukkit.script.objects.objects.CustomContext customContext;

    /**
     * 构造器。
     *
     * @param file 脚本文件
     * @throws IOException 读取脚本文件时出现异常
     */
    public ScriptExecutor(File file) throws IOException {
        scriptFile = file;
        fileName = file.getName();

        scriptCode = FileUtils.readFileToString(
                scriptFile, StandardCharsets.UTF_8
        );

        customContext = getCustomContext();
        CustomContextHandler.addCustomContext(customContext);
    }

    /**
     * 调用指定函数。
     *
     * @param function 函数名称
     * @return 结果
     */
    public Object invoke(String function) {
        return invoke(function, customContext);
    }

    /**
     * 使用指定 {@link org.js4bukkit.script.objects.objects.CustomContext} 对象调用指定函数。
     *
     * @param function      函数名称
     * @param customContext 指定 {@link org.js4bukkit.script.objects.objects.CustomContext} 对象
     * @return 结果
     */
    public Object invoke(String function, org.js4bukkit.script.objects.objects.CustomContext customContext) {
        Context context =
                customContext.getContext();
        ScriptableObject scriptableObject =
                customContext.getScriptableObject();

        Object object = scriptableObject.get(
                function, scriptableObject
        );

        Function functionObject = (Function) object;

        return functionObject.call(
                context, scriptableObject, scriptableObject, null
        );
    }

    /**
     * 获取该脚本应该使用的 {@link org.js4bukkit.script.objects.objects.CustomContext} 对象。
     *
     * @return 该脚本应该使用的 {@link org.js4bukkit.script.objects.objects.CustomContext} 对象
     */
    private org.js4bukkit.script.objects.objects.CustomContext getCustomContext() {
        Context preContext = Context.enter();
        ScriptableObject preScriptableObject = preContext.initStandardObjects();

        org.js4bukkit.script.objects.objects.CustomContext preCustomContext =
                new org.js4bukkit.script.objects.objects.CustomContext()
                        .setName(fileName)
                        .to(org.js4bukkit.script.objects.objects.CustomContext.class)
                        .setContext(preContext)
                        .setScriptableObject(preScriptableObject);

        // 预加载
        preContext.evaluateString(
                preScriptableObject, scriptCode, fileName, 1, null
        );

        // 使用 preCustomContext 执行 getContext() 函数
        Object result = invoke(
                ScriptHandler.GET_CONTEXT_FUNCTION, preCustomContext
        );

        // 如果返回的是 null 则直接使用 preCustomContext
        if (result == null) {
            return preCustomContext;
        }

        // 如果有指定 context 名称
        String name = result.toString();
        org.js4bukkit.script.objects.objects.CustomContext newCustomContext =
                CustomContextHandler.getCustomContext(name);

        // 如果靠这个名称找到了对应 context
        if (newCustomContext != null) {
            // 先释放 preCustomContext 再返回对应 context
            CustomContextHandler.removeCustomContext(preCustomContext);
            return newCustomContext;
        }

        // 如果没有找到则将 preCustomContext 改名返回
        return preCustomContext
                .setName(name)
                .to(CustomContext.class);
    }
}
