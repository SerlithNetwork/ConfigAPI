package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.MiniComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MiniMessageProvider implements TypeProvider<MiniComponent> {

    @NotNull
    @Override
    public MiniComponent load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return new MiniComponent(string);
        }
        throw new IllegalStateException("Failed to parse MiniMessage");
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<MiniComponent> context) {
        return context.getObject().getRaw();
    }

}
