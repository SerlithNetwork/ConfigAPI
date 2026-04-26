package net.j4c0b3y.api.config.platform.adventure.types;

import lombok.Getter;
import net.kyori.adventure.title.Title;

import java.time.Duration;

@Getter
public class TitleTimes {

    private final Title.Times times;
    private final long fadeIn;
    private final long stay;
    private final long fadeOut;

    public TitleTimes(final long fadeIn, final long stay, final long fadeOut) {
        this.times = Title.Times.times(
                Duration.ofMillis(fadeIn),
                Duration.ofMillis(stay),
                Duration.ofMillis(fadeOut)
        );
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

}
