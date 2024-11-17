package net.j4c0b3y.api.config.resolver.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.EnumProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;

import java.lang.reflect.Field;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class EnumResolver implements TypeResolver {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        if (!type.isEnum()) {
            return null;
        }

        return (TypeProvider<T>) new EnumProvider<>((Class<Enum>) type);
    }
}
