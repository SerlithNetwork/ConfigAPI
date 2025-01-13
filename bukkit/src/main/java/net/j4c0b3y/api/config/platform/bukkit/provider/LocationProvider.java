package net.j4c0b3y.api.config.platform.bukkit.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Used to load and save bukkit's Location class.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 13/01/2025
 */
public class LocationProvider implements TypeProvider<Location> {

    /*
     * Possible location string formats:
     * - world;x;y;z;yaw;pitch (length: 6)
     * - x;y;z;yaw;pitch       (length: 5)
     * - world;x;y;z           (length: 4)
     * - x;y;z                 (length: 3)
     *
     * World is omitted if the world name is "world".
     * Yaw and Pitch are omitted if they are both 0.
     * Each segment is separated by the ";" character.
     * All numeric values are rounded to 2 decimals.
     */

    @Override
    public Location load(LoadContext context) {
        // Split the location string by the ";" delimiter.
        String[] segments = String.valueOf(context.getObject()).split(";");

        // Ensure the amount of segments is between 3 and 6.
        if (segments.length < 3 || segments.length > 6) {
            throw new IllegalArgumentException("Location must consist of (world);x;y;z;(yaw);(pitch).");
        }

        // Instantiate a location with zeroed values, these will be changed.
        Location location = new Location(null, 0, 0, 0);
        int readIndex = 0;

        // The default world name if no world is specified.
        String worldName = "world";

        // If the amount of segments is 4 or 6, change the world name.
        if (segments.length % 2 == 0) {
            worldName = segments[readIndex++];
        }

        // Set the location's world using the world name.
        location.setWorld(Bukkit.getWorld(worldName));

        // Throw an error if a world with the specified name couldn't be found.
        if (location.getWorld() == null) {
            throw new IllegalArgumentException("World '" + worldName + "' doesn't exist, a valid world must be specified.");
        }

        // Load the x, y and z values, these are always present.
        location.setX(parse(segments[readIndex++], Double::parseDouble, "x"));
        location.setY(parse(segments[readIndex++], Double::parseDouble, "y"));
        location.setZ(parse(segments[readIndex++], Double::parseDouble, "z"));

        // If the amount of segments is 5 or 6, load the yaw and pitch values.
        if (segments.length >= 5) {
            location.setYaw(parse(segments[readIndex++], Float::parseFloat, "yaw"));
            location.setPitch(parse(segments[readIndex], Float::parseFloat, "pitch"));
        }

        return location;
    }

    @Override
    public Object save(SaveContext<Location> context) {
        List<String> segments = new ArrayList<>();

        Location location = context.getObject();
        String world = location.getWorld().getName();

        // Add the world name as long as it's not "world".
        if (!world.equals("world")) {
            segments.add(world);
        }

        // Format and add the x, y and z values.
        segments.add(format(location.getX()));
        segments.add(format(location.getY()));
        segments.add(format(location.getZ()));

        // Format and add the yaw and pitch values as long as they are not both 0.
        if (location.getYaw() != 0 || location.getPitch() != 0) {
            segments.add(format(location.getYaw()));
            segments.add(format(location.getPitch()));
        }

        // Join all the values with the ";" delimiter.
        return String.join(";", segments);
    }

    /**
     * Rounds a number, then convert it to a string.
     *
     * @param number The number to be rounded.
     * @return The rounded and formatted number.
     */
    private String format(double number) {
        return String.valueOf(Math.round(number * 100) / 100);
    }

    /**
     * Parses a string into a number, throws a formatted error if it fails.
     *
     * @param input The number string to be parsed.
     * @param parser The parsing function.
     * @param type The value label to use for the error message.
     * @return The parsed number value.
     */
    private <T extends Number> T parse(String input, Function<String, T> parser, String type) {
        try {
            return parser.apply(input);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("The " + type + " value is not a valid number.");
        }
    }
}
