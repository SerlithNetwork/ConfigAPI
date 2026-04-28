package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.PrefixedComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;

public class PrefixedComponentProvider implements TypeProvider<PrefixedComponent> {

    @NonNull
    @Override
    public PrefixedComponent load(@NonNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return new PrefixedComponent(string);
        }
        throw new IllegalStateException("Failed to parse prefixed component: " + context.getObject().getClass().getName());
    }

    @NonNull
    @Override
    public Object save(@NonNull SaveContext<PrefixedComponent> context) {
        return context.getObject().getRaw();
    }

}
