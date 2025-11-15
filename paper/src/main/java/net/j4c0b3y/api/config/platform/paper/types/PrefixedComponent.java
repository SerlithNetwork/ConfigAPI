package net.j4c0b3y.api.config.platform.paper.types;

import lombok.Getter;
import net.kyori.adventure.text.Component;

public class PrefixedComponent {

    @Getter
    private final Component component;

    @Getter
    private final Component raw;

    public PrefixedComponent(Component prefix, Component component) {
        this.component = prefix.append(component);
        this.raw = component;
    }

}
