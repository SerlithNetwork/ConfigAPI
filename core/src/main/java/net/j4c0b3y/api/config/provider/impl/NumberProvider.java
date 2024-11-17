package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.provider.TypeProvider;

import java.util.function.Function;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class NumberProvider<T extends Number> implements TypeProvider<T> {
    private final Function<String, T> parser;

    @Override
    public T load(Object object) {
        return parser.apply(String.valueOf(object));
    }

    @Override
    public Object save(T object) {
        return object;
    }
}
