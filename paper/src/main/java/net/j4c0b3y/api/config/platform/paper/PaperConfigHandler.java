package net.j4c0b3y.api.config.platform.paper;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.platform.paper.provider.*;
import net.j4c0b3y.api.config.platform.paper.types.PrefixedComponent;
import net.j4c0b3y.api.config.platform.paper.types.WorldReference;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
public class PaperConfigHandler extends ConfigHandler {

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     *
     * @param logger The logger used to warn for destructive actions.
     */
    public PaperConfigHandler(Logger logger, Component prefix) {
        super(logger);

        this.bind(Component.class, new ComponentProvider());
        this.bind(ItemStack.class, new ItemStackProvider());
        this.bind(Location.class, new LocationProvider());
        this.bind(PrefixedComponent.class, new PrefixedComponentProvider(prefix));
        this.bind(Vector.class, new VectorProvider());
        this.bind(WorldReference.class, new WorldProvider());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     */
    public PaperConfigHandler() {
        this(Logger.getLogger("ConfigAPI"), Component.empty());
    }

    public PaperConfigHandler(Component prefix) {
        this(Logger.getLogger("ConfigAPI"), prefix);
    }

    public PaperConfigHandler(Logger logger) {
        this(logger, Component.empty());
    }

}
