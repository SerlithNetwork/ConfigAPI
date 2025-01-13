package net.j4c0b3y.api.config.platform.bukkit;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.platform.bukkit.provider.LocationProvider;
import net.j4c0b3y.api.config.platform.bukkit.provider.WorldProvider;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.logging.Logger;

/**
 * Used for registering providers, backups,
 * and general config management.
 * Also binds bukkit specific providers.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
public class BukkitConfigHandler extends ConfigHandler {

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     *
     * @param logger The logger used to warn for destructive actions.
     */
    public BukkitConfigHandler(Logger logger) {
        super(logger);

        bind(World.class, new WorldProvider());
        bind(Location.class, new LocationProvider());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     */
    public BukkitConfigHandler() {
        this(Logger.getLogger("ConfigAPI"));
    }
}
