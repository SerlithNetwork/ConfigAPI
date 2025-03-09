package net.j4c0b3y.api.config;

import dev.dejvokep.boostedyaml.block.Block;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.AccessLevel;
import lombok.Getter;
import net.j4c0b3y.api.config.exception.LoadFailedException;
import net.j4c0b3y.api.config.exception.SaveFailedException;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import net.j4c0b3y.api.config.utils.ClassUtils;
import net.j4c0b3y.api.config.utils.Pair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A static access configuration file.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */
@Getter
public abstract class StaticConfig {
    /**
     * The modifiers for static config fields, this should never be changed.
     */
    private final static int MODIFIERS = Modifier.PUBLIC | Modifier.STATIC;

    /**
     * The file the config document is associated with.
     */
    private final File file;

    /**
     * The config handler which is used for settings.
     */
    private final ConfigHandler handler;

    /**
     * The document we load and save values from.
     */
    private final ConfigDocument document;

    /**
     * Maps all config routes to their associated fields.
     */
    @Getter(AccessLevel.NONE)
    private final Map<String, Field> fields = new HashMap<>();

    /**
     * A list of all routes mapped to if they can't be removed.
     */
    @Getter(AccessLevel.NONE)
    private final Map<String, Boolean> routes = new HashMap<>();

    /**
     * A map of relocation replacements to their target paths.
     */
    private final Map<String, List<String>> relocations = new HashMap<>();

    /**
     * A map of custom comments to add to fields.
     */
    private final Map<String, Pair<List<String>, Boolean>> comments = new HashMap<>();

    /**
     * If the last load operation was successful.
     */
    @Getter(AccessLevel.NONE)
    private boolean success = true;

    /**
     * Creates a new static config, registers it to the config handler.
     *
     * @param file The file to be used for the configuration document.
     * @param defaults The optional default resource to be loaded from.
     * @param handler The config handler to use for config settings.
     */
    public StaticConfig(File file, InputStream defaults, ConfigHandler handler) {
        this.file = file;
        this.handler = handler;

        try {
            this.document = new ConfigDocument(this, defaults);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to instantiate document for '" + file.getName() + "'.");
        }

        handler.getRegistered().add(this);
    }

    /**
     * Creates a new static config, registers it to the config handler.
     *
     * @param file The file to be used for the configuration document.
     * @param handler The config handler to use for config settings.
     */
    public StaticConfig(File file, ConfigHandler handler) {
        this(file, null, handler);
    }

    /**
     * @return The header to be shown at the top of the document file.
     */
    public List<String> getHeader() {
        Header header = getClass().getAnnotation(Header.class);
        if (header == null) return Collections.emptyList();

        return Arrays.asList(header.value());
    }

    /**
     * Creates, relocates and loads the document keys into the static fields.
     */
    public void load() {
        try {
            // Create and loads the values into the config document.
            document.reload();

            // Perform all registered value relocations.
            relocate();

            // If fields are empty, perform the initialization.
            if (fields.isEmpty()) {
                step(getClass(), "", true);
            }

            // Load and set each field value from the document.
            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                String route = entry.getKey();
                Field field = entry.getValue();

                // If the field is final, the value is always the same.
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                try {
                    // If the document contains the route, set the
                    // static field using the current type provider.
                    if (document.contains(route)) {
                        field.set(null, handler.provide(field).load(
                            new LoadContext(route, document.get(route))
                        ));
                    }
                } catch (Exception exception) {
                    throw new IOException("Failed to load key '" + route + "'.", exception);
                }
            }

            // Save the field values to the config document.
            save();

            // If configured, we should format values.
            if (handler.isFormatValues()) {
                format();
            }
        } catch (Exception exception) {
            // Load was unsuccessful, prevent saving.
            success = false;
            throw new LoadFailedException(file, exception);
        }

        // Load was successful, allow saving.
        success = true;
    }

    /**
     * Saves the current field values to the configuration document.
     */
    public void save() {
        try {
            // Don't allow saving if the values weren't loaded correctly.
            if (!success) {
                throw new IllegalStateException("Cannot save until successful load, check above for errors.");
            }

            // Make sure the document has been loaded and created before values can be saved.
            // noinspection ConstantValue
            if (document.getRoot() == null) {
                throw new IllegalStateException("Document must be loaded before it can be saved.");
            }

            // If the structure gets formatted, wipe all comments, as they are set again.
            if (handler.isFormatStructure()) {
                document.wipeComments();
            }

            // Clear document header to avoid it being duplicated.
            document.wipeComments(document);

            // Recurse through the static fields, setting the values in the document.
            step(getClass(), "", false);

            // Set the additional custom comments specified by the user.
            setComments();

            // Save the document values and comments to file.
            document.save();
        } catch (Exception exception) {
            throw new SaveFailedException(file, exception);
        }
    }

    /**
     * Recursively steps through each configuration section.
     *
     * @param parent The parent class of the section.
     * @param path The current path.
     * @param initialize If this is the initialization phase.
     */
    private void step(Class<?> parent, String path, boolean initialize) throws ReflectiveOperationException {
        for (Field field : parent.getDeclaredFields()) {
            // Skip field if its modifiers are invalid, or it is marked @Ignore.
            if (!ClassUtils.hasModifiers(field, MODIFIERS) || field.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            // Get the route by combining the current path and the formatted key.
            String route = path + getRoute(field.getAnnotation(Key.class), field.getName());

            // Add the fields and routes if we are in the initialization phase.
            if (initialize) {
                routes.put(route, true);
                fields.put(route, field);
                continue;
            }

            boolean hidden = field.isAnnotationPresent(Hidden.class);
            boolean present = document.contains(route);

            // If this is the initial saving of the value in the document,
            // or the field is final, so we have to override the value anyway.
            boolean initial = (!present || Modifier.isFinal(field.getModifiers())) && !hidden;

            // Set the field value in the configuration document.
            if (initial || (!hidden || present)) {
                set(route, field, field.get(null));
            }

            // Set the associated comment for the route's block if present.
            document.setComment(document.getBlock(route), field.getAnnotation(Comment.class));
        }

        Class<?>[] classes = parent.getDeclaredClasses();

        // The JVM usually returns classes in reverse order for some reason.
        // According to the spec, the order of getDeclaredClasses is up to
        // the JVM implementation, so this could differ depending on what is used.
        for (int i = classes.length - 1; i >= 0; i--) {
            Class<?> clazz = classes[i];

            // Skip field if its modifiers are invalid, or it is marked @Ignore.
            if (!ClassUtils.hasModifiers(clazz, MODIFIERS) || clazz.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            // Get the route by combining the current path and the formatted key.
            String route = path + getRoute(clazz.getAnnotation(Key.class), clazz.getSimpleName());

            // Add the routes and recurse if we are in the initialization phase.
            if (initialize) {
                routes.put(route, false);
                step(clazz, route + ".", true);
                continue;
            }

            // If the class is final, remove it so it is regenerated with default values.
            if (Modifier.isFinal(clazz.getModifiers())) {
                document.remove(route);
            }

            Section section = document.getSection(route);

            // If the section doesn't exist and is not hidden, create it.
            if (section == null && !clazz.isAnnotationPresent(Hidden.class)) {
                section = document.createSection(route);
            }

            // If the section exists, and we shouldn't skip, set the comment and recurse.
            if (section != null) {
                document.setComment(section, clazz.getAnnotation(Comment.class));
                step(clazz, route + ".", false);
            }
        }
    }

    /**
     * Formats the values in the config file.
     */
    public void format() throws IOException {
        try {
            boolean removed = false;

            for (String route : document.getRoutesAsStrings(true)) {

                // If there is no field for the route,
                // but the route is present in the file.
                if (!fields.containsKey(route) && !routes.containsKey(route)) {

                    // If the route starts with any manual paths,
                    // prevent it from being flagged for removal.
                    if (routes.entrySet().stream().anyMatch(entry -> entry.getValue() && route.startsWith(entry.getKey()))) {
                        continue;
                    }

                    boolean remove = handler.isRemoveUnrecognised();

                    // Warn the user of the removal / discovery.
                    handler.getLogger().warning(
                        (remove ? "Removed" : "Found") +
                        " unrecognised key '" + route + "'" +
                        " from '" + file.getName() + "'."
                    );

                    // If configured, remove the key from the document.
                    if (remove) {
                        document.remove(route);
                        removed = true;
                    }

                    continue;
                }

                // Ignore any sections for the next bit.
                if (document.isSection(route)) continue;

                // Update the field with their formatted values.
                Field field = fields.get(route);
                set(route, field, field.get(null));
            }

            // If there were any keys removed, create a backup of the file.
            if (removed) {
                handler.getLogger().warning(
                    "Keys were removed from '" + file.getName() + "'," +
                    " a backup was saved to '" + document.backup() + "'."
                );
            }

            // Save the formatted values to file.
            document.save();
        } catch (Exception exception) {
            throw new IOException("Format failed for file '" + file.getName() + "'.", exception);
        }
    }

    /**
     * Register a new relocation.
     *
     * @param target The old location.
     * @param replacement The new location.
     */
    protected void relocate(String target, String replacement) {
        relocations.computeIfAbsent(replacement, key -> new ArrayList<>()).add(target);
    }

    /**
     * Performs all registered relocations.
     */
    private void relocate() {
        relocations.forEach((replacement, targets) -> {
            for (String target : targets) {
                // If the document doesn't contain the target, ignore it.
                if (!document.contains(target)) continue;

                // Set the value in the new location.
                document.set(replacement, document.get(target));

                // Remove the old, existing value.
                document.remove(target);

                // Warn the user of the relocation.
                handler.getLogger().warning(
                    "Value from '" + target + "' was " +
                    "relocated to '" + replacement + "'."
                );
            }
        });
    }

    /**
     * Adds a custom comment to a route in the document.
     *
     * @param route The route.
     * @param side If the comment is on the side.
     * @param comment The comment.
     */
    protected void setComment(String route, boolean side, String ...comment) {
        comments.put(route, new Pair<>(Arrays.asList(comment), side));
    }

    /**
     * Adds a custom comment to a route in the document.
     *
     * @param route The route.
     * @param comment The comment.
     */
    protected void setComment(String route, String ...comment) {
        setComment(route, false, comment);
    }

    /**
     * Sets the custom comments for routes that exist in the document.
     */
    private void setComments() {
        comments.forEach((route, comment) -> {
            Block<?> block = document.getBlock(route);
            if (block == null) return;

            List<String> comments = block.getComments();
            if (comments != null && !comments.isEmpty()) return;

            document.setComment(block, comment.getLeft(), comment.getRight());
        });
    }

    /**
     * Uses a type provider to save a field value to the config document.
     *
     * @param key The route to set in the document.
     * @param field The field that the value comes from.
     * @param value The value of the field.
     */
    @SuppressWarnings("unchecked")
    private <T> void set(String key, Field field, T value) {
        SaveContext<T> context = new SaveContext<>(key, value);
        Object object = ((TypeProvider<T>) handler.provide(field)).save(context);
        document.set(context.getKey(), object);
    }

    /**
     * If the key annotation is present, uses that value.
     * Otherwise, uses the member name with the key formatter.
     *
     * @param key The key annotation.
     * @param name The name of the member.
     * @return The formatted key.
     */
    private String getRoute(Key key, String name) {
        boolean valid = key != null && !key.value().trim().isEmpty();
        return valid ? key.value() : handler.getKeyFormatter().apply(name);
    }

    /**
     * Changes the yaml key to the specified value.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Key {
        String value();
    }

    /**
     * Adds a comment above the yaml key in the file.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Comment {
        String[] value();
        boolean side() default false;
    }

    /**
     * Completely ignores a static field from being processed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Ignore {
    }

    /**
     * Marks a value to be manually specified by the user.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Hidden {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    public @interface Header {
        String[] value();
    }
}
