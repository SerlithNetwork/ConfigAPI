package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import net.j4c0b3y.api.config.utils.ClassUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Used for single generic collection types.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class CollectionProvider<E, T extends Collection<E>> implements TypeProvider<T> {
    /**
     * The supplier to create a new collection of the generic type.
     */
    private final Supplier<T> supplier;

    /**
     * The collections generic type (the custom class).
     */
    private final Class<E> generic;

    /**
     * The config handler
     */
    private final ConfigHandler handler;

    /**
     * Creates a new collection provider, setting the generic type.
     *
     * @param field The collection field from the static config.
     * @param supplier The supplier to create a new collection instance.
     * @param ignored The type the collection class.
     * @param handler The config handler
     */
    public CollectionProvider(Field field, Supplier<T> supplier, Class<T> ignored, ConfigHandler handler) {
        this(supplier, (Class<E>) (field != null ? ClassUtils.getGenerics(field)[0] : null), handler);

        if (field == null) {
            throw new IllegalStateException("Nested collections are not supported!");
        }
    }

    @Override
    public @NotNull T load(@NotNull LoadContext context) {
        // Get the provider associated with the generic.
        TypeProvider<E> provider = handler.provide(generic);

        // Create a new collection instance using the supplier.
        T collection = supplier.get();

        // If the context object isn't a collection, load it as one value.
        if (!Collection.class.isAssignableFrom(context.getObject().getClass())) {
            collection.add(provider.load(context));
            return collection;
        }

        // Used for the load context key.
        int index = 0;

        // For each entry in the collection, load it using the provider.
        for (Object element : (Collection<Object>) context.getObject()) {
            collection.add(provider.load(
                new LoadContext(String.valueOf(index++), element)
            ));
        }

        return collection;
    }

    @Override
    public @NotNull Object save(@NotNull SaveContext<T> context) {
        // Get the provider associated with the generic.
        TypeProvider<E> provider = handler.provide(generic);

        // Create a new list, which is saved to the document.
        List<Object> list = new ArrayList<>();

        // User for the save context key.
        int index = 0;

        // For each entry in the collection, save it using the provider.
        for (E element : context.getObject()) {
            list.add(provider.save(
                new SaveContext<>(String.valueOf(index++), element)
            ));
        }

        return list;
    }
}
