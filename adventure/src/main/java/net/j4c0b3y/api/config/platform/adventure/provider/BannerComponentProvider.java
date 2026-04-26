package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.BannerComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class BannerComponentProvider implements TypeProvider<BannerComponent> {

    @Override
    public @NonNull BannerComponent load(@NonNull LoadContext context) {
        if (context.getObject() instanceof Collection<?> collection) {
            return new BannerComponent(collection.stream().map(Object::toString).toArray(String[]::new));
        }
        throw new IllegalStateException("Failed to parse MiniMessage");
    }

    @Override
    public @Nullable Object save(@NonNull SaveContext<BannerComponent> context) {
        return context.getObject().getRaws();
    }

}
