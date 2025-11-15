package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.platform.paper.types.WorldReference;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldProvider implements TypeProvider<WorldReference> {

    @NotNull
    @Override
    public WorldReference load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String) {
            return new WorldReference((String) context.getObject());
        }
        throw new IllegalArgumentException("Failed to parse world");
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<WorldReference> context) {
        return context.getObject().getName();
    }

}
