package net.j4c0b3y.api.config.platform.bukkit.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * Used to load and save bukkit's World class.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
public class WorldProvider implements TypeProvider<World> {

    @Override
    public World load(LoadContext context) {
        // Get the world using the specified name.
        World world = Bukkit.getWorld(String.valueOf(context.getObject()));

        // Throw an error if the world doesn't exist.
        if (world == null) {
            throw new IllegalArgumentException("There is no world with a matching name.");
        }

        return world;
    }

    @Override
    public Object save(SaveContext<World> context) {
        // Save using the world name.
        return context.getObject().getName();
    }
}
