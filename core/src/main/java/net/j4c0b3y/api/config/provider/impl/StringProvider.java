package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class StringProvider implements TypeProvider<String> {

    @Override
    public String load(Object object) {
        return String.valueOf(object);
    }

    @Override
    public Object save(String object) {
        return object;
    }
}
