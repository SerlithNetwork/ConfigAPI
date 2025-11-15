package net.j4c0b3y.api.config.platform.paper;

import net.j4c0b3y.api.config.platform.adventure.AdventureConfigHandler;
import net.j4c0b3y.api.config.platform.paper.provider.*;
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
public class PaperConfigHandler extends AdventureConfigHandler {

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     *
     * @param logger The logger used to warn for destructive actions.
     */
    public PaperConfigHandler(Logger logger, Component prefix) {
        super(logger, prefix);

        this.bind(ItemStack.class, new ItemStackProvider());
        this.bind(Location.class, new LocationProvider());
        this.bind(Vector.class, new VectorProvider());
        this.bind(WorldReference.class, new WorldProvider());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     * Does not contain a prefix.
     */
    public PaperConfigHandler() {
        super(Logger.getLogger("ConfigAPI"), Component.empty());
    }

    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Uses a logger with name "ConfigAPI".
     */
    public PaperConfigHandler(Component prefix) {
        super(Logger.getLogger("ConfigAPI"), prefix);
    }
    /**
     * Creates a new config handler instance,
     * registers default resolvers and providers.
     * Does not contain a prefix.
     */

    public PaperConfigHandler(Logger logger) {
        super(logger, Component.empty());
    }

}
