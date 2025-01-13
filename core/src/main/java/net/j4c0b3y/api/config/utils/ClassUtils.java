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
    /**
     * Gets the generic types for a field.
     *
     * @param field The field.
     * @return The generic types.
     */
    public Type[] getGenerics(Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return type.getActualTypeArguments();
    }

    /**
     * Gets the wrapped type of the primitive type.
     *
     * @param type The primitive type.
     * @return The wrapper class.
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> wrap(Class<T> type) {
        return (Class<T>) MethodType.methodType(type).wrap().returnType();
    }

    /**
     * Checks if a field contains certain modifiers.
     *
     * @param field The field to check.
     * @param contains The modifiers.
     * @return If the field contains the modifiers.
     */
    public boolean hasModifiers(Field field, int contains) {
        return (field.getModifiers() & contains) == contains;
    }

    /**
     * Checks if a class contains certain modifiers.
     *
     * @param clazz The class to check.
     * @param contains The modifiers.
     * @return If the class contains the modifiers.
     */
    public boolean hasModifiers(Class<?> clazz, int contains) {
        return (clazz.getModifiers() & contains) == contains;
    }
}
