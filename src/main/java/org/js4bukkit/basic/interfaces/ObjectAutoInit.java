package org.js4bukkit.basic.interfaces;

import de.leonhard.storage.Yaml;

/**
 * 该接口将简化从 Yaml 键值内读取属性并设置对象属性。
 *
 * @author NaerQAQ
 * @version 1.0
 * @since 2023/12/18
 */
public interface ObjectAutoInit {
    /**
     * 通过指定 Yaml 文件读取指定键值属性设置对象属性。
     *
     * @param yaml    指定 Yaml 文件
     * @param yamlKey 键值
     * @return 设置完成后的对象
     */
    default Object init(Yaml yaml, String yamlKey) {
        Class<?> clazz = this.getClass();

        // 通过反射进行 set 调用
        for (String key : yaml.singleLayerKeySet(yamlKey)) {
            String value = yaml.getString(
                    yamlKey + "." + key
            );

            // 如果信息并不全
            if (value.isEmpty()) {
                return null;
            }

            String capitalizeFirstLetterKey =
                    Character.toUpperCase(key.charAt(0)) + key.substring(1);

            String setMethodName = "set" + capitalizeFirstLetterKey;

            try {
                clazz.getDeclaredMethod(setMethodName, String.class).invoke(this, value);
            } catch (Exception exception) {
                return null;
            }
        }

        return this;
    }
}
