package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.util.regex.Pattern;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/01/2025
 */
public class PatternProvider implements TypeProvider<Pattern> {

    @Override
    public Pattern load(Object object) {
        return Pattern.compile(String.valueOf(object));
    }

    @Override
    public Object save(Pattern object) {
        return object.pattern();
    }
}
