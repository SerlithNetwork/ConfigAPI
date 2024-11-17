package net.j4c0b3y.api.config.utils;

import lombok.experimental.UtilityClass;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@UtilityClass
public class ClassUtils {

    public Type[] getGenerics(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return type.getActualTypeArguments();
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> wrap(Class<T> type) {
        return (Class<T>) MethodType.methodType(type).wrap().returnType();
    }

    public boolean hasModifiers(Field field, int contains) {
        return (field.getModifiers() & contains) == contains;
    }

    public boolean hasModifiers(Class<?> clazz, int contains) {
        return (clazz.getModifiers() & contains) == contains;
    }
}
