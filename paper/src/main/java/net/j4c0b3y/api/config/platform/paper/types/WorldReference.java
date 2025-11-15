package net.j4c0b3y.api.config.platform.paper.types;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Holds a reference to a Bukkit World
 * If the world was deleted, this will hold a null reference
 */
public class WorldReference {

    private final WeakReference<World> world;

    public WorldReference(World world) {
        this.world = new WeakReference<>(world);
    }

    @Nullable
    public World getWorld() {
        return this.world.get();
    }

}
