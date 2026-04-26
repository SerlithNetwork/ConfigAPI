package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.TitleComponent;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class TitleComponentProvider implements TypeProvider<TitleComponent> {

    @Override
    public @NonNull TitleComponent load(@NonNull LoadContext context) {
        if (context.getObject() instanceof Map<?,?> map) {
            Map<?, ?> duration = (Map<?, ?>) map.get("duration");
            return new TitleComponent(
                    (String) map.get("title"),
                    (String) map.get("subtitle"),
                    (long) duration.get("fade-in"),
                    (long) duration.get("stay"),
                    (long) duration.get("fade-out")
            );
        }
        throw new IllegalStateException("Failed to parse Title component");
    }

    @Override
    public @Nullable Object save(@NonNull SaveContext<TitleComponent> context) {
        Map<String, Object> duration = new LinkedHashMap<>();
        duration.put("fade-in", context.getObject().getFadeIn());
        duration.put("stay", context.getObject().getStay());
        duration.put("fade-out", context.getObject().getFadeOut());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("title", context.getObject().getRawTitle());
        map.put("subtitle", context.getObject().getRawSubtitle());
        map.put("duration", duration);
        return map;
    }

}
