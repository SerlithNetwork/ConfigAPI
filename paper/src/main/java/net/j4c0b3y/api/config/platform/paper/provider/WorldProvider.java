package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.platform.paper.types.WorldReference;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class WorldProvider implements TypeProvider<WorldReference> {

    @NonNull
    @Override
    public WorldReference load(@NonNull LoadContext context) {
        if (context.getObject() instanceof String) {
            return new WorldReference((String) context.getObject());
        }
        throw new IllegalArgumentException("Failed to parse world: " + context.getObject().getClass().getName());
    }

    @Nullable
    @Override
    public Object save(@NonNull SaveContext<WorldReference> context) {
        return context.getObject().getName();
    }

}
