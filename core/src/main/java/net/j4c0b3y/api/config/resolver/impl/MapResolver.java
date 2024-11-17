package net.j4c0b3y.api.config.resolver.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.MapProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;
import net.j4c0b3y.api.config.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 17/11/2024
 */
@RequiredArgsConstructor
public class MapResolver implements TypeResolver {
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        if (!Map.class.isAssignableFrom(type)) return null;

        Type[] generics = ClassUtils.getGenerics(field);
        if (!generics[0].equals(String.class)) return null;

        return (TypeProvider<T>) resolve((Class<?>) generics[1]);
    }

    private <E, T extends Map<String, E>> MapProvider<E, T> resolve(Class<E> type) {
        return new MapProvider<>(type, handler);
    }
}
