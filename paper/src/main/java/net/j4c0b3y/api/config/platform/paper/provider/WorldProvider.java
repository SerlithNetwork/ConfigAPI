package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.platform.paper.types.WorldReference;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldProvider implements TypeProvider<WorldReference> {

    @NotNull
    @Override
    public WorldReference load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String) {
            return new WorldReference(Bukkit.getWorld((String) context.getObject()));
        }
        throw new IllegalArgumentException("Cannot serialize world entry");
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<WorldReference> context) {
        World world = context.getObject().getWorld();
        if (world == null) {
            return null;
        }
        return world.getName();
    }

}
