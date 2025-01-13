package net.j4c0b3y.api.config.resolver.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.CollectionProvider;
import net.j4c0b3y.api.config.resolver.TypeResolver;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Resolves supported collection types to our inbuilt collection provider.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class CollectionResolver implements TypeResolver {
    /**
     * The config handler.
     */
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeProvider<T> resolve(Class<T> type, Field field) {
        // If the type is a List, return a new ArrayList collection provider.
        if (List.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, ArrayList::new, List.class, handler);
        }

        // If the type is a Set, return a new HashSet collection provider.
        if (Set.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, HashSet::new, Set.class, handler);
        }

        // If the type is a Queue, return a new LinkedList collection provider.
        if (Queue.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, LinkedList::new, Queue.class, handler);
        }

        // If the type is a Deque, return a new ArrayDeque collection provider.
        if (Deque.class.isAssignableFrom(type)) {
            return (TypeProvider<T>) new CollectionProvider<>(field, ArrayDeque::new, Deque.class, handler);
        }

        return null;
    }
}
