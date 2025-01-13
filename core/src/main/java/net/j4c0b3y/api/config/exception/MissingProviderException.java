package net.j4c0b3y.api.config.exception;

/**
 * A formatted exception for when a provider was not found.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class MissingProviderException extends IllegalStateException {
    /**
     * @param type The type.
     */
    public MissingProviderException(Class<?> type) {
        super("No providers are bound for type '" + type.getSimpleName() + "'.");
    }
}
