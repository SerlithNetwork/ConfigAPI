package net.j4c0b3y.api.config.exception;

import java.io.File;

/**
 * A formatted exception for when the config fails to save.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 13/11/2024
 */
public class SaveFailedException extends RuntimeException {
    /**
     * @param file The config file.
     * @param cause The cause.
     */
    public SaveFailedException(File file, Throwable cause) {
        super("Save failed for file '" + file.getName() + "'.", cause);
    }
}
