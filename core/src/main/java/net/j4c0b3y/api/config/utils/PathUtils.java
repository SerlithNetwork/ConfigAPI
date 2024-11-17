package net.j4c0b3y.api.config.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
@UtilityClass
public class PathUtils {
    public String createBackup(Path path, String format) throws IOException {
        String date = new SimpleDateFormat(format).format(new Date());
        String name = getBaseName(path) + "-" + date + getExtension(path);
        return Files.copy(path, path.resolveSibling(name)).toFile().getName();
    }

    public String getBaseName(Path path) {
        String name = path.getFileName().toString();
        return name.contains(".") ? name.substring(0, name.lastIndexOf(".")) : name;
    }

    public String getExtension(Path path) {
        String name = path.getFileName().toString();
        return name.contains(".") ? name.substring(name.lastIndexOf(".")) : "";
    }
}
