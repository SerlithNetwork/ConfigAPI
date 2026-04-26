package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.j4c0b3y.api.config.platform.adventure.AdventureConfigHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@Getter
public class PrefixedComponent {

    private final Component component;
    private final Component unprefixed;
    private final String raw;

    public PrefixedComponent(final String raw) {
        this.raw = raw;
        this.unprefixed = MiniMessage.miniMessage().deserialize(this.raw);
        this.component = AdventureConfigHandler.getPrefix().appendSpace().append(this.unprefixed);
    }

    public Component resolveUnprefixed(TagResolver...tagResolvers) {
        return MiniMessage.miniMessage().deserialize(this.raw, tagResolvers);
    }

    public Component resolveComponent(TagResolver ...tagResolvers) {
        return AdventureConfigHandler.getPrefix().appendSpace().append(this.resolveUnprefixed(tagResolvers));
    }

}
