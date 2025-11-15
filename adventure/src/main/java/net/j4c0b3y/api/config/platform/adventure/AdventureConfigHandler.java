package net.j4c0b3y.api.config.platform.adventure;

import lombok.Getter;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.platform.adventure.provider.MiniMessageProvider;
import net.j4c0b3y.api.config.platform.adventure.provider.PrefixedComponentProvider;
import net.j4c0b3y.api.config.platform.adventure.types.MiniComponent;
import net.j4c0b3y.api.config.platform.adventure.types.PrefixedComponent;
import net.kyori.adventure.text.Component;

import java.util.logging.Logger;

/**
 * Used for registering providers, backups,
 * and general config management.
 * Also binds bukkit specific providers.
 *
 * @author Biquaternions
 * @version ConfigAPI
 * @since 11/14/2025
 */
public class AdventureConfigHandler extends ConfigHandler {

    @Getter
    private static Component prefix;

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     *
     * @param logger The logger used to warn for destructive actions.
     */
    public AdventureConfigHandler(Logger logger, Component prefix) {
        super(logger);

        AdventureConfigHandler.prefix = prefix;
        this.bind(MiniComponent.class, new MiniMessageProvider());
        this.bind(PrefixedComponent.class, new PrefixedComponentProvider());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     * Does not contain a prefix.
     */
    public AdventureConfigHandler() {
        this(Logger.getLogger("ConfigAPI"), Component.empty());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     */
    public AdventureConfigHandler(Component prefix) {
        this(Logger.getLogger("ConfigAPI"), prefix);
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Does not contain a prefix.
     */
    public AdventureConfigHandler(Logger logger) {
        this(logger, Component.empty());
    }

}
