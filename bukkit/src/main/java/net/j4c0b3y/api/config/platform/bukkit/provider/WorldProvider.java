package net.j4c0b3y.api.config.platform.bukkit.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
public class WorldProvider implements TypeProvider<World> {

    @Override
    public World load(LoadContext context) {
        World world = Bukkit.getWorld(String.valueOf(context.getObject()));

        if (world == null) {
            throw new IllegalArgumentException("There is no world with a matching name.");
        }

        return world;
    }

    @Override
    public Object save(SaveContext<World> context) {
        return context.getObject().getName();
    }
}
