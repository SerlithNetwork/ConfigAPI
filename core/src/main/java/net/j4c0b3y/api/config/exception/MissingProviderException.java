package net.j4c0b3y.api.config.exception;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class MissingProviderException extends IllegalStateException {
    public MissingProviderException(Class<?> type) {
        super("No providers are bound for type '" + type.getSimpleName() + "'.");
    }
}
