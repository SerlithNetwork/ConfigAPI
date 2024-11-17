package net.j4c0b3y.api.config.provider.impl;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;

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
    public T load(Object object) {
        if (!(object instanceof Section)) {
            throw new IllegalArgumentException("Mapped route must resolve to section.");
        }

        Section section = (Section) object;
        Map<String, E> entries = new LinkedHashMap<>();
        TypeProvider<E> provider = handler.provide(generic);

        for (Map.Entry<String, Object> entry : section.getStringRouteMappedValues(false).entrySet()) {
            entries.put(entry.getKey(), provider.load(section.get(entry.getKey())));
        }

        return (T) entries;
    }

    @Override
    public Object save(T object) {
        Map<String, Object> entries = new LinkedHashMap<>();
        TypeProvider<E> provider = handler.provide(generic);

        for (Map.Entry<String, E> entry : object.entrySet()) {
            entries.put(entry.getKey(), provider.save(entry.getValue()));
        }

        return entries;
    }
}
