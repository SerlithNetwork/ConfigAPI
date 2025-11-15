package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class PotionEffectProvider implements TypeProvider<PotionEffect> {

    @NotNull
    @Override
    public PotionEffect load(@NotNull LoadContext context) {
        if (context.getObject() instanceof Map<?,?> map) {
            String key = (String) map.get("effect");
            PotionEffectType effect = Registry.MOB_EFFECT.get(Objects.requireNonNull(NamespacedKey.fromString(key)));
            if (effect == null) {
                throw new IllegalStateException("Potion effect does not exist: '" + key + "'");
            }
            return new PotionEffect(
                    effect,
                    (int) map.get("duration"),
                    (int) map.get("amplifier"),
                    (boolean) map.get("ambient"),
                    (boolean) map.get("has-particles"),
                    (boolean) map.get("has-icon")
            );
        }
        throw new IllegalStateException("Failed to parse PotionEffect");
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<PotionEffect> context) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("effect", context.getObject().getType());
        map.put("duration", context.getObject().getDuration());
        map.put("amplifier", context.getObject().getAmplifier());
        map.put("ambient", context.getObject().isAmbient());
        map.put("has-particles", context.getObject().hasParticles());
        map.put("has-icon", context.getObject().hasIcon());
        return map;
    }

}
