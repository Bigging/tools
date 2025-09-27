package org.example.utils;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtil {

    private static final Map<Class<?>, Map<String, Method>> methodCache = new ConcurrentHashMap<>();

    public static <T> Object getField(T obj, String methodName) {
        if (obj == null || StringUtils.isBlank(methodName)) {
            throw new RuntimeException("传入空参数");
        }
        try {
            Class<?> clazz = obj.getClass();
            Map<String, Method> classMethods = methodCache.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
            Method method = classMethods.computeIfAbsent(methodName, n -> {
                try {
                    return clazz.getMethod(n);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            });
            return method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("执行异常，字段解析失败", e);
        }
    }
}
