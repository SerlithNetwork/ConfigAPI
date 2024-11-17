package net.j4c0b3y.api.config.resolver.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.CollectionProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class CollectionResolver implements TypeResolver {
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        if (List.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, ArrayList::new, List.class, handler);
        }

        if (Set.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, HashSet::new, Set.class, handler);
        }

        if (Queue.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, LinkedList::new, Queue.class, handler);
        }

        if (Deque.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, ArrayDeque::new, Deque.class, handler);
        }

        return null;
    }
}
