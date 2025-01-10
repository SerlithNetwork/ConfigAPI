package net.j4c0b3y.api.config.platform.bukkit.provider;

import lombok.RequiredArgsConstructor;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 10/11/2024
 */
@RequiredArgsConstructor
public class LocationProvider implements TypeProvider<Location> {
    private final ConfigHandler handler;

    @Override
    public Location load(LoadContext context) {
        String[] parts = String.valueOf(context.getObject()).split(",");

        if (parts.length != 6) {
            throw new IllegalArgumentException("Location must have world, xyz, yaw and pitch.");
        }

        TypeProvider<World> worldProvider = handler.provide(World.class);
        TypeProvider<Double> doubleProvider = handler.provide(double.class);
        TypeProvider<Float> floatProvider = handler.provide(float.class);

        World world = worldProvider.load(new LoadContext(null, parts[0]));
        double x = doubleProvider.load(new LoadContext(null, parts[1]));
        double y = doubleProvider.load(new LoadContext(null, parts[2]));
        double z = doubleProvider.load(new LoadContext(null, parts[3]));
        float yaw = floatProvider.load(new LoadContext(null, parts[4]));
        float pitch = floatProvider.load(new LoadContext(null, parts[5]));

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public Object save(SaveContext<Location> context) {
        Location location = context.getObject();

        String world = location.getWorld().getName();
        String x = String.valueOf(location.getX());
        String y = String.valueOf(location.getY());
        String z = String.valueOf(location.getZ());
        String yaw = String.valueOf(location.getYaw());
        String pitch = String.valueOf(location.getPitch());

        return world + x + y + z + yaw + pitch;
    }
}
