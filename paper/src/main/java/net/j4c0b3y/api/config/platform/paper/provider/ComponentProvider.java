package net.j4c0b3y.api.config.platform.paper.provider;

import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentProvider implements TypeProvider<Component> {

    @NotNull
    @Override
    public Component load(@NotNull LoadContext context) {
        if (context.getObject() instanceof String string) {
            return MiniMessage.miniMessage().deserialize(string);
        }
        throw new IllegalStateException();
    }

    @Nullable
    @Override
    public Object save(@NotNull SaveContext<Component> context) {
        return MiniMessage.miniMessage().serialize(context.getObject());
    }

}
