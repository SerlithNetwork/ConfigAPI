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
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 17/11/2024
 */
@RequiredArgsConstructor
public class MapProvider<E, T extends Map<String, E>> implements TypeProvider<T> {
    private final Class<E> generic;
    private final ConfigHandler handler;

    @Override
    @SuppressWarnings("unchecked")
    public T load(LoadContext context) {
        if (!(context.getObject() instanceof Section)) {
            throw new IllegalArgumentException("Mapped route must resolve to section.");
        }

        Section section = (Section) context.getObject();
        Map<String, E> entries = new LinkedHashMap<>();
        TypeProvider<E> provider = handler.provide(generic);

        for (String key : section.getStringRouteMappedValues(false).keySet()) {
            entries.put(key, provider.load(new LoadContext(key, section.get(key))));
        }

        return (T) entries;
    }

    @Override
    public Object save(SaveContext<T> saveContext) {
        Map<String, Object> entries = new LinkedHashMap<>();
        TypeProvider<E> provider = handler.provide(generic);

        for (Map.Entry<String, E> entry : saveContext.getObject().entrySet()) {
            SaveContext<E> context = new SaveContext<>(entry.getKey(), entry.getValue());

            Object object = provider.save(context);
            entries.put(context.getKey(), object);
        }

        return entries;
    }
}
