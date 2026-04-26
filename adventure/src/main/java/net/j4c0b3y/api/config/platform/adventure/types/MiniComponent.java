package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Getter
public class MiniComponent {

    private final String raw;
    private final Component component;

    public MiniComponent(String raw) {
        this.raw = raw;
        this.component = MiniMessage.miniMessage().deserialize(this.raw);
    }

    public Component resolveComponent(TagResolver ...tagResolvers) {
        return MiniMessage.miniMessage().deserialize(this.raw, tagResolvers);
    }

}
