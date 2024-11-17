package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.net.URI;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 14/11/2024
 */
public class URIProvider implements TypeProvider<URI> {

    @Override
    public URI load(Object object) {
        return URI.create(String.valueOf(object));
    }

    @Override
    public Object save(URI object) {
        return object.toString();
    }
}
