package me.naerqaq.utils.common.nms;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSUtil {

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    @SneakyThrows
    public static Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        return field.get(object);
    }

    @SneakyThrows
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    @SneakyThrows
    public static Object getMethodValue(Method method, Object object, Object... args) {
        return method.invoke(object, args);
    }

    @SneakyThrows
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) {
        Method method = clazz.getMethod(methodName, args);
        method.setAccessible(true);
        return method;
    }

//    lol for anticheat purpose
    @SneakyThrows
    public static Object newAxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return getNMSClass("AxisAlignedBB")
                .getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE)
                .newInstance(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static Class<?> getEntity() {
        return getClass("Entity");
    }

    public static Class<?> getNMSClass(String string) {
        return getClass("net.minecraft.server." + version + "." + string);
    }

    @SneakyThrows
    public static Class<?> getClass(String string) {
        return Class.forName(string);
    }
}
