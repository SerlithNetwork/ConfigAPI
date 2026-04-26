package net.j4c0b3y.api.config.provider.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

/**
 * A simple provider, requiring a parser and an optional saver.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@AllArgsConstructor
@RequiredArgsConstructor
public class SimpleProvider<T> implements TypeProvider<T> {
    /**
     * The parsing function.
     */
    private final Function<String, T> parser;

    /**
     * The saving function.
     */
    private Function<T, String> saver = Object::toString;

    @Override
    public @NonNull T load(@NonNull LoadContext context) {
        return parser.apply(String.valueOf(context.getObject()));
    }

    @Override
    public @NonNull Object save(@NonNull SaveContext<T> context) {
        return saver.apply(context.getObject());
    }
}
