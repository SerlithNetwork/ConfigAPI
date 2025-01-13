package net.j4c0b3y.api.config.utils;

import lombok.experimental.UtilityClass;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 13/11/2024
 */
@UtilityClass
public class StringUtils {
    /**
     * Gets the amount of indentation at the start of a line.
     *
     * @param line The line.
     * @return The indentation.
     */
    public int getIndentation(String line) {
        return line.length() - line.replaceFirst("^\\s+","").length();
    }
}
