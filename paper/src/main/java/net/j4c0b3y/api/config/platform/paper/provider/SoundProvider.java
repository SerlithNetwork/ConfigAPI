package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class SoundProvider implements TypeProvider<Sound> {

    @NonNull
    @Override
    public Sound load(@NonNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            NamespacedKey key = NamespacedKey.fromString(string);
            if (key == null) {
                throw new IllegalArgumentException("Sound key in invalid format: '" + string + "'");
            }
            Sound sound = Registry.SOUNDS.get(key);
            if (sound == null) {
                throw new IllegalArgumentException("Sound does not exist: '" + string + "'");
            }
            return sound;
        }
        throw new IllegalStateException("Failed to parse Sound: " + context.getObject().getClass().getName());
    }

    @Nullable
    @Override
    public Object save(@NonNull SaveContext<Sound> context) {
        NamespacedKey key = Registry.SOUNDS.getKey(context.getObject());
        if (key == null) {
            return null;
        }
        return key.asString();
    }

}
