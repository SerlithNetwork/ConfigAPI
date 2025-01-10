package net.j4c0b3y.api.config.provider.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/01/2025
 */
@Getter @Setter
@AllArgsConstructor
public class SaveContext<T> {
    private String key;
    private final T object;
}
