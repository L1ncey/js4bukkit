package org.js4bukkit.utils.common.nms;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.js4bukkit.Js4Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 简易反射库，用于提供反射的基本处理与 NMS 操作。
 *
 * @author Lincey
 * @version 1.0
 * @since 2023/12/20
 */
@UtilityClass
@SuppressWarnings("unused")
public class NMSUtil {
    /**
     * 通过反射获取指定对象的字段值。
     *
     * @param field  要获取的字段
     * @param object 目标对象
     * @return 字段的值
     */
    @SneakyThrows
    public static Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * 通过字段名称获取类的字段。
     *
     * @param clazz      类
     * @param fieldName  字段名称
     * @return 类的字段
     */
    @SneakyThrows
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    /**
     * 通过反射调用指定对象的方法。
     *
     * @param method 方法
     * @param object 目标对象
     * @param args   方法参数
     * @return 方法的返回值
     */
    @SneakyThrows
    public static Object getMethodValue(Method method, Object object, Object... args) {
        return method.invoke(object, args);
    }

    /**
     * 通过方法名和参数类型获取类的方法。
     *
     * @param clazz      类
     * @param methodName 方法名
     * @param args       方法参数类型
     * @return 类的方法
     */
    @SneakyThrows
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) {
        Method method = clazz.getMethod(methodName, args);
        method.setAccessible(true);
        return method;
    }

    /**
     * 获取玩家碰撞箱 (AABB) 实例。
     *
     * @param minX 最小 X 坐标
     * @param minY 最小 Y 坐标
     * @param minZ 最小 Z 坐标
     * @param maxX 最大 X 坐标
     * @param maxY 最大 Y 坐标
     * @param maxZ 最大 Z 坐标
     * @return 玩家碰撞箱
     */
    @SneakyThrows
    public static Object newAxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return getNMSClass("AxisAlignedBB")
                .getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE)
                .newInstance(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * 获取 Entity 类的 Class 对象。
     *
     * @return Entity 类的 Class 对象
     */
    public static Class<?> getEntity() {
        return getClass("Entity");
    }

    /**
     * 通过给定的类名获取 Class 对象。
     *
     * @param string 类名
     * @return Class 对象
     */
    @SneakyThrows
    public static Class<?> getClass(String string) {
        return Class.forName(string);
    }

    /**
     * 通过给定的类名获取 NMS 类的 Class 对象。
     *
     * @param string 类名
     * @return NMS 类的 Class 对象
     */
    public static Class<?> getNMSClass(String string) {
        return getClass("net.minecraft.server." + Js4Bukkit.getNmsVersion() + "." + string);
    }
}
