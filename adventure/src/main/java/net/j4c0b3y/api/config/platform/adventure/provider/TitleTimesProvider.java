package net.j4c0b3y.api.config.platform.adventure.provider;

import net.j4c0b3y.api.config.platform.adventure.types.TitleTimes;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class TitleTimesProvider implements TypeProvider<TitleTimes> {

    @Override
    public @NonNull TitleTimes load(@NonNull LoadContext context) {
        if (context instanceof Map<?,?> map) {
            return new TitleTimes(
                    (long) map.get("fade-in"),
                    (long) map.get("stay"),
                    (long) map.get("fade-out")
            );
        }
        throw new IllegalStateException("Failed to parse Title times");
    }

    @Override
    public @Nullable Object save(@NonNull SaveContext<TitleTimes> context) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("fade-in", context.getObject().getFadeIn());
        map.put("stay", context.getObject().getStay());
        map.put("fade-out", context.getObject().getFadeOut());
        return map;
    }

}
