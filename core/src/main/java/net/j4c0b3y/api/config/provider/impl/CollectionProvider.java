package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class CollectionProvider<E, T extends Collection<E>> implements TypeProvider<T> {
    private final Supplier<T> supplier;
    private final Class<T> type;
    private final Class<E> generic;
    private final ConfigHandler handler;

    public CollectionProvider(Field field, Supplier<T> supplier, Class<T> type, ConfigHandler handler) {
        this(supplier, type, (Class<E>) ClassUtils.getGenerics(field)[0], handler);
    }

    @Override
    public T load(Object object) {
        TypeProvider<E> provider = handler.provide(generic);
        T collection = supplier.get();

        if (!Collection.class.isAssignableFrom(type)) {
            collection.add(provider.load(object));
            return collection;
        }

        for (Object element : (Collection<Object>) object) {
            collection.add(provider.load(element));
        }

        return collection;
    }

    @Override
    public Object save(T object) {
        TypeProvider<E> provider = handler.provide(generic);
        List<Object> list = new ArrayList<>();

        for (E element : object) {
            list.add(provider.save(element));
        }

        return list;
    }
}
