package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.PrefixedComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jetbrains.annotations.NotNull;

public class PrefixedComponentProvider implements TypeProvider<PrefixedComponent> {

    @NotNull
    @Override
    public PrefixedComponent load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return new PrefixedComponent(string);
        }
        throw new IllegalStateException("Failed to parse prefixed component");
    }

    @NotNull
    @Override
    public Object save(@NotNull SaveContext<PrefixedComponent> context) {
        return context.getObject().getRaw();
    }

}
