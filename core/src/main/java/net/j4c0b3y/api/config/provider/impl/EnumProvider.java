package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.provider.TypeProvider;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class EnumProvider<T extends Enum<T>> implements TypeProvider<T> {
    private final Class<T> type;

    @Override
    public T load(Object object) {
        return Enum.valueOf(type, String.valueOf(object).toUpperCase());
    }

    @Override
    public Object save(T object) {
        return object.name();
    }
}
