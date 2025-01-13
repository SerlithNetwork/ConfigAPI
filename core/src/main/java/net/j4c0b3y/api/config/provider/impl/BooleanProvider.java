package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.Arrays;
import java.util.List;

/**
 * Used for providing boolean values.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/11/2024
 */
public class BooleanProvider implements TypeProvider<Boolean> {
    /**
     * The true values.
     */
    private final static List<String> TRUE_VALUES = Arrays.asList("true", "yes", "1");

    /**
     * The false values.
     */
    private final static List<String> FALSE_VALUES = Arrays.asList("false", "no", "0");

    @Override
    public Boolean load(LoadContext context) {
        String lowered = String.valueOf(context.getObject()).toLowerCase();

        // Return true if the lowered string is a true value.
        for (String value : TRUE_VALUES) {
            if (lowered.equals(value)) {
                return true;
            }
        }

        // Return false if the lowered string is a false value.
        for (String value : FALSE_VALUES) {
            if (lowered.equals(value)) {
                return false;
            }
        }

        // If the string in neither a true nor false value, throw an error.
        throw new IllegalArgumentException("A boolean value must be provided.");
    }

    @Override
    public Object save(SaveContext<Boolean> context) {
        return context.getObject();
    }
}
