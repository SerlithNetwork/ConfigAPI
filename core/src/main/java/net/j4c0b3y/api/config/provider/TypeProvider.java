package net.j4c0b3y.api.config.provider;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public interface TypeProvider<T> {
    T load(Object object);
    Object save(T object);
}
