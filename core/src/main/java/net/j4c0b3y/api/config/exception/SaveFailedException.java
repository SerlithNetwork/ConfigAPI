package net.j4c0b3y.api.config.exception;

import java.io.File;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 13/11/2024
 */
public class SaveFailedException extends RuntimeException {
    public SaveFailedException(File file, Throwable cause) {
        super("Save failed for file '" + file.getName() + "'.", cause);
    }
}
