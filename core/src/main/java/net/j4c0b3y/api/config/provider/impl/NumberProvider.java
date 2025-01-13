package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.function.Function;

/**
 * Used for loading and saving of number based types.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class NumberProvider<T extends Number> implements TypeProvider<T> {
    /**
     * The parsing function.
     */
    private final Function<String, T> parser;

    @Override
    public T load(LoadContext context) {
        return parser.apply(String.valueOf(context.getObject()));
    }

    @Override
    public Object save(SaveContext<T> context) {
        return context.getObject();
    }
}
