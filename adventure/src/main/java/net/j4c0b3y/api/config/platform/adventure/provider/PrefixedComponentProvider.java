package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.PrefixedComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class PrefixedComponentProvider implements TypeProvider<PrefixedComponent> {

    private final Component prefix;

    public PrefixedComponentProvider(Component prefix) {
        this.prefix = prefix;
    }

    @NotNull
    @Override
    public PrefixedComponent load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return new PrefixedComponent(this.prefix, string);
        }
        throw new IllegalStateException("Failed to parse prefixed component");
    }

    @NotNull
    @Override
    public Object save(@NotNull SaveContext<PrefixedComponent> context) {
        return context.getObject().getRaw();
    }

}
