package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;

import java.time.Duration;

@Getter
public class TitleComponent {

    private final String rawTitle;
    private final String rawSubtitle;
    private final long fadeIn;
    private final long stay;
    private final long fadeOut;

    private final Title title;
    private final Title.Times times;

    public TitleComponent(final String rawTitle, final String rawSubtitle, final long fadeIn, final long stay, final long fadeOut) {
        this.rawTitle = rawTitle;
        this.rawSubtitle = rawSubtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;

        this.times = Title.Times.times(
                Duration.ofMillis(this.fadeIn),
                Duration.ofMillis(this.stay),
                Duration.ofMillis(this.fadeOut)
        );
        this.title = Title.title(
                MiniMessage.miniMessage().deserialize(this.rawTitle),
                MiniMessage.miniMessage().deserialize(this.rawSubtitle),
                this.times
        );
    }

    public Title resolveComponent(TagResolver ...tagResolvers) {
        return Title.title(
                MiniMessage.miniMessage().deserialize(this.rawTitle, tagResolvers),
                MiniMessage.miniMessage().deserialize(this.rawSubtitle, tagResolvers),
                this.times
        );
    }

}
