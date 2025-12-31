package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class VectorProvider implements TypeProvider<Vector> {
    @NotNull
    @Override
    public Vector load(@NotNull LoadContext context) {
        if (context.getObject() instanceof Map<?, ?> map) {
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;

            if (map.containsKey("x")) {
                x = (double) map.get("x");
            }
            if (map.containsKey("y")) {
                y = (double) map.get("y");
            }
            if (map.containsKey("z")) {
                z = (double) map.get("z");
            }

            return new Vector(x, y, z);
        }
        throw new IllegalStateException("Failed to parse Vector: " + context.getObject().getClass().getName());
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<Vector> context) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("x", context.getObject().getX());
        map.put("y", context.getObject().getY());
        map.put("z", context.getObject().getZ());

        return map;
    }
}
