package net.j4c0b3y.api.config.resolver.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;
import net.j4c0b3y.api.config.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class RegistryResolver implements TypeResolver {
    private final Map<Class<?>, TypeProvider<?>> providers = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        return (TypeProvider<T>) providers.get(type);
    }

    public <T> void register(Class<T> type, TypeProvider<T> provider) {
        if (type.isPrimitive()) {
            register(ClassUtils.wrap(type), provider);
        }

        providers.put(type, provider);
    }
}
