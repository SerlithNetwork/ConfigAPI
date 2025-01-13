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
 * Used for registering providers, backups,
 * and general config management.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */
@Getter(AccessLevel.PROTECTED) @Setter
public class ConfigHandler {
    /**
     * The registered provider resolvers.
     */
    @Getter(AccessLevel.NONE)
    private final List<TypeResolver> resolvers = new ArrayList<>();

    /**
     * The resolver for registering custom type providers.
     */
    @Getter(AccessLevel.NONE)
    private final RegistryResolver registry = new RegistryResolver();

    /**
     * A list of registered static configs.
     */
    @Getter(AccessLevel.PUBLIC)
    private final List<StaticConfig> registered = new ArrayList<>();

    /**
     * The logger to warn users for destructive actions.
     */
    private final Logger logger;

    /**
     * If the structure of the document should be formatted on save.
     */
    private boolean formatStructure = true;

    /**
     * If the document values should be formatted on load.
     */
    private boolean formatValues = true;

    /**
     * If unrecognised keys should be removed on load.
     */
    private boolean removeUnrecognised = true;

    /**
     * If a backup should be created when keys are removed.
     */
    private boolean createBackupOnRemove = true;

    /**
     * The date format of the backup file name.
     */
    private String backupDateFormat = "yyyy-MM-dd-hh-mm-a";

    /**
     * Used to format field names into clean yaml keys.
     */
    private Function<String, String> keyFormatter =
        key -> key.toLowerCase().replace("_", "-");

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     *
     * @param logger The logger used to warn for destructive actions.
     */
    public ConfigHandler(Logger logger) {
        this.logger = logger;

        // Register resolvers
        register(registry);
        register(new EnumResolver());
        register(new CollectionResolver(this));
        register(new MapResolver(this));

        // Register providers
        bind(String.class, new SimpleProvider<>(String::valueOf));
        bind(UUID.class, new SimpleProvider<>(UUID::fromString));
        bind(URI.class, new SimpleProvider<>(URI::create));
        bind(Pattern.class, new SimpleProvider<>(Pattern::compile));

        bind(int.class, new NumberProvider<>(Integer::parseInt));
        bind(long.class, new NumberProvider<>(Long::parseLong));
        bind(byte.class, new NumberProvider<>(Byte::parseByte));
        bind(float.class, new NumberProvider<>(Float::parseFloat));
        bind(double.class, new NumberProvider<>(Double::parseDouble));
        bind(short.class, new NumberProvider<>(Short::parseShort));

        bind(boolean.class, new BooleanProvider());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     */
    public ConfigHandler() {
        this(Logger.getLogger("ConfigAPI"));
    }

    /**
     * Registers a new type resolver.
     *
     * @param resolver The type resolver.
     */
    public void register(TypeResolver resolver) {
        resolvers.add(resolver);
    }

    /**
     * Binds a class to the specified type provider.
     *
     * @param type The class to bind.
     * @param provider The type provider to bind to.
     */
    public <T> void bind(Class<T> type, TypeProvider<T> provider) {
        registry.register(type, provider);
    }

    /**
     * Finds an appropriate type provider for the given class and field.
     *
     * @param type The class to get the provider for.
     * @param field The field to get the provider for.
     * @return The type provider.
     */
    public <T> TypeProvider<T> provide(Class<T> type, Field field) {
        for (TypeResolver resolver : resolvers) {
            TypeProvider<T> provider = resolver.resolve(type, field);

            if (provider != null) {
                return provider;
            }
        }

        throw new MissingProviderException(type);
    }

    /**
     * Finds an appropriate type provider for the given class.
     *
     * @param type The class to get the provider for.
     * @return The type provider.
     */
    public <T> TypeProvider<T> provide(Class<T> type) {
        return provide(type, null);
    }

    /**
     * Finds an appropriate type provider for the given field.
     *
     * @param field The field to get the provider for.
     * @return The type provider.
     */
    public TypeProvider<?> provide(Field field) {
        return provide(field.getType(), field);
    }

    /**
     * Used for the internal static config documents, this should
     * not be overridden unless you know what you are doing.
     *
     * @return The document settings.
     */
    protected Settings[] getDocumentSettings() {
        GeneralSettings general = GeneralSettings.builder()
            .setUseDefaults(false) // Do not retain default values this can cause issues.
            .build();

        LoaderSettings loader = LoaderSettings.builder()
            .setAllowDuplicateKeys(false) // Don't allow duplicate keys.
            .build();

        DumperSettings dumper = DumperSettings.builder()
            .setFlowStyle(FlowStyle.BLOCK)
            .setIndicatorIndentation(2) // Ensures lists are indented correctly.
            .setScalarFormatter((tag, value, role, def) ->
                // Ensures string values are formatted with double quotes.
                role.equals(NodeRole.VALUE) && tag.equals(Tag.STR) ? ScalarStyle.DOUBLE_QUOTED : def
            )
            .build();

        return new Settings[] { general, loader, dumper };
    }
}
