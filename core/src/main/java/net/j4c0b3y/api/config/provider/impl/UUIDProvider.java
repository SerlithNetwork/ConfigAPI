package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.util.UUID;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class UUIDProvider implements TypeProvider<UUID> {

    @Override
    public UUID load(Object object) {
        return UUID.fromString(String.valueOf(object));
    }

    @Override
    public Object save(UUID object) {
        return object.toString();
    }
}
