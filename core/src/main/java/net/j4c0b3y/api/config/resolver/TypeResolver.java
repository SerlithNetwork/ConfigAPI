package net.j4c0b3y.api.config.resolver;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.lang.reflect.Field;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public interface TypeResolver {
    <T> TypeProvider<T> resolve(Class<T> type, Field field);
}
