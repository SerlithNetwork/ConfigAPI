package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionEffectTypeProvider implements TypeProvider<PotionEffectType> {

    @NotNull
    @Override
    public PotionEffectType load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            NamespacedKey key = NamespacedKey.fromString(string);
            if (key == null) {
                throw new IllegalArgumentException("Potion effect key in invalid format: '" + string + "'");
            }
            PotionEffectType effect = Registry.MOB_EFFECT.get(key);
            if (effect == null) {
                throw new IllegalArgumentException("Potion effect does not exist: '" + string + "'");
            }
            return effect;
        }
        throw new IllegalStateException("Failed to parse PotionEffectType: " + context.getObject().getClass().getName());
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<PotionEffectType> context) {
        return context.getObject().getKey().asString();
    }

}
