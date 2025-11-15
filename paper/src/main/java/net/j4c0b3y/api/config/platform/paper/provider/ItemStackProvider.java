package net.j4c0b3y.api.config.platform.paper.provider;

import io.papermc.paper.registry.RegistryKey;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

/**
 * Referenced from Paper 1.21.10 internals
 */
@SuppressWarnings({"deprecation", "removal"})
public class ItemStackProvider implements TypeProvider<ItemStack> {

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public ItemStack load(@NotNull LoadContext context) {
        if (context.getObject() instanceof Map<?,?> configMap) {
            if (configMap.containsKey("schema_version")) {
                return Bukkit.getUnsafe().deserializeStack((Map<String, Object>) configMap);
            }

            int version = (configMap.containsKey("v")) ? ((Number) configMap.get("v")).intValue() : -1;
            short damage = 0;
            int amount = 1;

            if (configMap.containsKey("damage")) {
                damage = ((Number) configMap.get("damage")).shortValue();
            }

            Material type;
            if (version < 0) {
                type = Material.getMaterial(Material.LEGACY_PREFIX + (String) configMap.get("type"));

                byte dataVal = (type != null && type.getMaxDurability() == 0) ? (byte) damage : 0; // Actually durable items get a 0 passed into conversion
                type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, dataVal), true);

                // We've converted now so the data val isn't a thing and can be reset
                if (dataVal != 0) {
                    damage = 0;
                }
            } else {
                type = Bukkit.getUnsafe().getMaterial((String) configMap.get("type"), version);
            }

            if (configMap.containsKey("amount")) {
                amount = ((Number) configMap.get("amount")).intValue();
            }

            ItemStack result = new ItemStack(type, amount, damage);

            if (configMap.containsKey("enchantments")) { // Backward compatibility, @deprecated
                Object raw = configMap.get("enchantments");

                if (raw instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) raw;

                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        String stringKey = entry.getKey().toString();
                        stringKey = Bukkit.getUnsafe().get(Enchantment.class, stringKey);
                        NamespacedKey key = NamespacedKey.fromString(stringKey.toLowerCase(Locale.ROOT));

                        Enchantment enchantment = Bukkit.getUnsafe().get(RegistryKey.ENCHANTMENT, key);

                        if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                            result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                        }
                    }
                }
            } else if (configMap.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
                Object raw = configMap.get("meta");
                if (raw instanceof ItemMeta) {
                    ((ItemMeta) raw).setVersion(version);
                    // Paper start - for pre 1.20.5 itemstacks, add HIDE_STORED_ENCHANTS flag if HIDE_ADDITIONAL_TOOLTIP is set
                    if (version < 3837) { // 1.20.5
                        if (((ItemMeta) raw).hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)) {
                            ((ItemMeta) raw).addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);
                        }
                    }
                    // Paper end
                    result.setItemMeta((ItemMeta) raw);
                }
            }

            if (version < 0) {
                // Set damage again incase meta overwrote it
                if (configMap.containsKey("damage")) {
                    result.setDurability(damage);
                }
            }

            return result.ensureServerConversions(); // Paper
        }
        throw new IllegalStateException("Failed to parse ItemStack");
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<ItemStack> context) {
        return Bukkit.getUnsafe().serializeStack(context.getObject());
    }

}
