package net.j4c0b3y.api.config.provider.impl;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Used for string to custom class mapped values.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 17/11/2024
 */
@RequiredArgsConstructor
public class MapProvider<E, T extends Map<String, E>> implements TypeProvider<T> {
    /**
     * The custom type generic.
     */
    private final Class<E> generic;

    /**
     * The config handler.
     */
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public T load(LoadContext context) {
        // Throw if the map is not a section instance.
        if (!(context.getObject() instanceof Section)) {
            throw new IllegalArgumentException("Mapped route must resolve to section.");
        }

        Section section = (Section) context.getObject();

        // An ordered map for our deserialized generic entries.
        Map<String, E> entries = new LinkedHashMap<>();

        // The provider associated with the generic.
        TypeProvider<E> provider = handler.provide(generic);

        // For each of the values in the section, load using the provider.
        for (String key : section.getStringRouteMappedValues(false).keySet()) {
            entries.put(key, provider.load(new LoadContext(key, section.get(key))));
        }

        return (T) entries;
    }

    @Override
    public Object save(SaveContext<T> saveContext) {
        // An ordered map for our serialized yaml object entries.
        Map<String, Object> entries = new LinkedHashMap<>();
        TypeProvider<E> provider = handler.provide(generic);

        for (Map.Entry<String, E> entry : saveContext.getObject().entrySet()) {
            SaveContext<E> context = new SaveContext<>(entry.getKey(), entry.getValue());

            // Serialize the entries into yaml objects.
            Object object = provider.save(context);
            entries.put(context.getKey(), object);
        }

        return entries;
    }
}
