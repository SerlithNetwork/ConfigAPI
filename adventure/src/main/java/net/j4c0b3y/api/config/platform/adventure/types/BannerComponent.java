package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Arrays;
import java.util.List;

@Getter
public class BannerComponent {

    private final List<String> raws;
    private final List<Component> components;
    private final Component joinedComponent;

    public BannerComponent(String ...raws) {
        this.raws = Arrays.stream(raws).toList();
        this.components = this.raws.stream().map(MiniMessage.miniMessage()::deserialize).toList();
        this.joinedComponent = Component.join(JoinConfiguration.newlines(), this.components);
    }

    public List<Component> resolveComponents(TagResolver ...tagResolvers) {
        return this.raws.stream().map(raw -> MiniMessage.miniMessage().deserialize(raw, tagResolvers)).toList();
    }

    public Component resolveJoinedComponent(TagResolver ...tagResolvers) {
        return Component.join(JoinConfiguration.newlines(), this.resolveComponents(tagResolvers));
    }

}
