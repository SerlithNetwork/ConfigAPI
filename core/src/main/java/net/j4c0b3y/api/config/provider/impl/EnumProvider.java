package net.j4c0b3y.api.config.provider.impl;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;

/**
 * Used for providing enum values.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
@RequiredArgsConstructor
public class EnumProvider<T extends Enum<T>> implements TypeProvider<T> {
    /**
     * The enum type.
     */
    private final Class<T> type;

    @Override
    public @NonNull T load(@NonNull LoadContext context) {
        // Load using the context string, transformed to upper case.
        return Enum.valueOf(type, String.valueOf(context.getObject()).toUpperCase());
    }

    @Override
    public @NonNull Object save(@NonNull SaveContext<T> context) {
        // Save using the enum name.
        return context.getObject().name();
    }
}
