package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class PrefixedComponent {

    @Getter
    private final Component component;

    @Getter
    private final Component unprefixed;

    @Getter
    private final String raw;

    public PrefixedComponent(Component prefix, String raw) {
        this.raw = raw;
        this.unprefixed = MiniMessage.miniMessage().deserialize(this.raw);
        this.component = prefix.append(this.unprefixed);
    }

}
