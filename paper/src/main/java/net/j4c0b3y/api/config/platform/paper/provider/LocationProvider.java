package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocationProvider implements TypeProvider<Location> {

    @NotNull
    @Override
    public Location load(@NotNull LoadContext context) {
        if (context.getObject() instanceof Map<?,?> map) {
            World world = null;
            if (map.containsKey("world")) {
                world = Bukkit.getWorld((String) map.get("world"));
                if (world == null) {
                    throw new IllegalArgumentException("Unknown world");
                }
            }
            return new Location(world, (double) map.get("x"), (double) map.get("y"), (double) map.get("z"), (float) map.get("yaw"), (float) map.get("pitch"));
        }
        throw new IllegalStateException("Failed to parse Location");
    }

    @NotNull
    @Override
    public Object save(@NotNull SaveContext<Location> context) {
        Map<String, Object> map = new LinkedHashMap<>();
        Location location = context.getObject();
        World world = location.getWorld();
        if (world != null) {
            map.put("world", world.getName());
        }
        map.put("x", location.getBlockX());
        map.put("y", location.getBlockY());
        map.put("z", location.getBlockZ());

        map.put("yaw", location.getYaw());
        map.put("pitch", location.getPitch());

        return map;
    }

}
