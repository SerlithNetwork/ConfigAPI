package net.j4c0b3y.api.config.provider;

import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used for (de)serialization between the document and static fields.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public interface TypeProvider<T> {
    /**
     * Deserializes a yaml object to the type provider's generic type.
     *
     * @param context The load context, containing the object.
     * @return The deserialized type provider generic instance.
     */
    @NotNull T load(@NotNull LoadContext context);

    /**
     * Serializes the type provider's generic class instance to a yaml object.
     *
     * @param context The generic save context, containing the instance.
     * @return The serialized yaml object.
     */
    @Nullable Object save(@NotNull SaveContext<T> context);
}
