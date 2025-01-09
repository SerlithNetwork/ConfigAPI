package net.j4c0b3y.api.config;

import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.FlowStyle;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.common.ScalarStyle;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.nodes.Tag;
import dev.dejvokep.boostedyaml.settings.Settings;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.utils.format.NodeRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.j4c0b3y.api.config.exception.MissingProviderException;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.impl.*;
import net.j4c0b3y.api.config.resolver.TypeResolver;
import net.j4c0b3y.api.config.resolver.impl.CollectionResolver;
import net.j4c0b3y.api.config.resolver.impl.EnumResolver;
import net.j4c0b3y.api.config.resolver.impl.MapResolver;
import net.j4c0b3y.api.config.resolver.impl.RegistryResolver;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */

@Getter(AccessLevel.PROTECTED) @Setter
public class ConfigHandler {

    @Getter(AccessLevel.NONE)
    private final List<TypeResolver> resolvers = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    private final RegistryResolver registry = new RegistryResolver();

    @Getter(AccessLevel.PUBLIC)
    private final List<StaticConfig> registered = new ArrayList<>();
    private final Logger logger;

    private boolean formatStructure = true;
    private boolean spaceBeforeComment = true;

    private boolean formatValues = true;
    private boolean removeUnrecognised = true;
    private boolean createBackupOnRemove = true;

    private String backupDateFormat = "yyyy-MM-dd-hh-mm-a";

    private Function<String, String> keyFormatter =
        key -> key.toLowerCase().replace("_", "-");

    public ConfigHandler(Logger logger) {
        this.logger = logger;

        register(registry);
        register(new EnumResolver());
        register(new CollectionResolver(this));
        register(new MapResolver(this));

        bind(String.class, new StringProvider());
        bind(boolean.class, new BooleanProvider());
        bind(UUID.class, new UUIDProvider());
        bind(URI.class, new URIProvider());
        bind(Pattern.class, new PatternProvider());

        bind(int.class, new NumberProvider<>(Integer::parseInt));
        bind(long.class, new NumberProvider<>(Long::parseLong));
        bind(byte.class, new NumberProvider<>(Byte::parseByte));
        bind(float.class, new NumberProvider<>(Float::parseFloat));
        bind(double.class, new NumberProvider<>(Double::parseDouble));
        bind(short.class, new NumberProvider<>(Short::parseShort));
    }

    public ConfigHandler() {
        this(Logger.getLogger("ConfigAPI"));
    }

    public void register(TypeResolver resolver) {
        resolvers.add(resolver);
    }

    public <T> void bind(Class<T> type, TypeProvider<T> provider) {
        registry.register(type, provider);
    }

    public <T> TypeProvider<T> provide(Class<T> type, Field field) {
        for (TypeResolver resolver : resolvers) {
            TypeProvider<T> provider = resolver.resolve(type, field);

            if (provider != null) {
                return provider;
            }
        }

        throw new MissingProviderException(type);
    }

    public <T> TypeProvider<T> provide(Class<T> type) {
        return provide(type, null);
    }

    public TypeProvider<?> provide(Field field) {
        return provide(field.getType(), field);
    }

    protected Settings[] getDocumentSettings() {
        GeneralSettings general = GeneralSettings.builder()
            .setUseDefaults(false)
            .build();

        LoaderSettings loader = LoaderSettings.builder()
            .setAllowDuplicateKeys(false)
            .build();

        DumperSettings dumper = DumperSettings.builder()
            .setFlowStyle(FlowStyle.BLOCK)
            .setIndicatorIndentation(2)
            .setScalarFormatter((tag, value, role, def) ->
                role.equals(NodeRole.VALUE) && tag.equals(Tag.STR) ? ScalarStyle.DOUBLE_QUOTED : def
            )
            .build();

        return new Settings[] { general, loader, dumper };
    }
}
