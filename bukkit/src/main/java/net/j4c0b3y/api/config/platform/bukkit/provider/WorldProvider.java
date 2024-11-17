package net.j4c0b3y.api.config.platform.bukkit.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
public class WorldProvider implements TypeProvider<World> {

    @Override
    public World load(Object object) {
        World world = Bukkit.getWorld(String.valueOf(object));

        if (world == null) {
            throw new IllegalArgumentException("There is no world with a matching name.");
        }

        return world;
    }

    @Override
    public Object save(World world) {
        return world.getName();
    }
}
