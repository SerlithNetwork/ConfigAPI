package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
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
    public T load(LoadContext context) {
        TypeProvider<E> provider = handler.provide(generic);
        T collection = supplier.get();

        // If the context object isn't a collection, load it as one value.
        if (!Collection.class.isAssignableFrom(context.getObject().getClass())) {
            collection.add(provider.load(context));
            return collection;
        }

        int index = 0;

        for (Object element : (Collection<Object>) context.getObject()) {
            collection.add(provider.load(
                new LoadContext(String.valueOf(index++), element)
            ));
        }

        return collection;
    }

    @Override
    public Object save(SaveContext<T> context) {
        TypeProvider<E> provider = handler.provide(generic);
        List<Object> list = new ArrayList<>();

        int index = 0;

        for (E element : context.getObject()) {
            list.add(provider.save(
                new SaveContext<>(String.valueOf(index++), element)
            ));
        }

        return list;
    }
}
