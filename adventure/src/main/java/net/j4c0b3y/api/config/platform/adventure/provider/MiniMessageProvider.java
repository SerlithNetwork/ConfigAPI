package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.MiniComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MiniMessageProvider implements TypeProvider<MiniComponent> {

    @NonNull
    @Override
    public MiniComponent load(@NonNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return new MiniComponent(string);
        }
        throw new IllegalStateException("Failed to parse MiniMessage: " + context.getObject().getClass().getName());
    }

    @Nullable
    @Override
    public Object save(@NonNull SaveContext<MiniComponent> context) {
        return context.getObject().getRaw();
    }

}
