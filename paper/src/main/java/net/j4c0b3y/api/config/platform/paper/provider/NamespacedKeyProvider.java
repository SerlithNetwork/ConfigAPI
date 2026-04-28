package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class NamespacedKeyProvider implements TypeProvider<NamespacedKey> {

    @Override
    public @NonNull NamespacedKey load(@NonNull LoadContext context) {
        if (context.getObject() instanceof String fullKey) {
            String[] splits = fullKey.split(":");
            if (splits.length != 2) {
                throw new IllegalStateException(String.format("The key is invalid, it does not follow the 'namespace:key' structure: Key(%s)", fullKey));
            }

            String namespace = splits[0];
            String key = splits[1];
            if (namespace.isBlank() || key.isBlank()) {
                throw new IllegalStateException(String.format("The key is invalid, it contains a blank namespace or key: Key(%s)", fullKey));
            }

            return new NamespacedKey(namespace, key);
        }
        throw new IllegalStateException("Failed to parse NamespacedKey");
    }

    @Override
    public @Nullable Object save(@NonNull SaveContext<NamespacedKey> context) {
        return context.getObject().asString();
    }

}
