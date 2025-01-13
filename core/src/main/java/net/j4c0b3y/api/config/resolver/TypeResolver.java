package net.j4c0b3y.api.config.resolver;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.lang.reflect.Field;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public interface TypeResolver {
    /**
     * Resolves a type provider for the given class and field.
     *
     * @param type The class.
     * @param field The field.
     * @return The type provider.
     */
    <T> TypeProvider<T> resolve(Class<T> type, Field field);
}
