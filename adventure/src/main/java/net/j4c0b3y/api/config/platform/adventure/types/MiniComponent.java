package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniComponent {

    @Getter
    private final String raw;

    @Getter
    private final Component component;

    public MiniComponent(String raw) {
        this.raw = raw;
        this.component = MiniMessage.miniMessage().deserialize(this.raw);
    }

}
