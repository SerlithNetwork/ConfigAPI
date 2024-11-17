package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;

import java.util.Arrays;
import java.util.List;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class BooleanProvider implements TypeProvider<Boolean> {
    private final static List<String> TRUE_VALUES = Arrays.asList("true", "yes", "1");
    private final static List<String> FALSE_VALUES = Arrays.asList("false", "no", "0");

    @Override
    public Boolean load(Object object) {
        String lowered = String.valueOf(object).toLowerCase();

        for (String value : TRUE_VALUES) {
            if (lowered.equals(value)) {
                return true;
            }
        }

        for (String value : FALSE_VALUES) {
            if (lowered.equals(value)) {
                return false;
            }
        }

        throw new IllegalArgumentException("A boolean value must be provided.");
    }

    @Override
    public Object save(Boolean object) {
        return object;
    }
}
