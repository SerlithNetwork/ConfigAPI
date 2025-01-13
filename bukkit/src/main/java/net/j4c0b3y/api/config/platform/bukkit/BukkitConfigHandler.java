package net.j4c0b3y.api.config.platform.bukkit;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.platform.bukkit.provider.LocationProvider;
import net.j4c0b3y.api.config.platform.bukkit.provider.WorldProvider;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.logging.Logger;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
public class BukkitConfigHandler extends ConfigHandler {

    public BukkitConfigHandler(Logger logger) {
        super(logger);

        bind(World.class, new WorldProvider());
        bind(Location.class, new LocationProvider());
    }

    public BukkitConfigHandler() {
        this(Logger.getLogger("ConfigAPI"));
    }
}
