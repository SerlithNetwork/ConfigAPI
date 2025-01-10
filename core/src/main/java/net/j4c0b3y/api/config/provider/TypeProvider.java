package net.j4c0b3y.api.config.provider;

import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public interface TypeProvider<T> {
    T load(LoadContext context);
    Object save(SaveContext<T> context);
}
