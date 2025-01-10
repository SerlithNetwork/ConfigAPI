package net.j4c0b3y.api.config;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import lombok.AccessLevel;
import lombok.Getter;
import net.j4c0b3y.api.config.exception.LoadFailedException;
import net.j4c0b3y.api.config.exception.SaveFailedException;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import net.j4c0b3y.api.config.utils.ClassUtils;

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
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */
@Getter
public abstract class StaticConfig {
    private final static int MODIFIERS = Modifier.PUBLIC | Modifier.STATIC;

    private final File file;
    private final ConfigHandler handler;
    private final ConfigDocument document;

    private final HashMap<String, List<String>> relocations = new HashMap<>();

    @Getter(AccessLevel.NONE)
    private final Map<String, Field> fields = new HashMap<>();

    @Getter(AccessLevel.NONE)
    private final HashMap<String, Boolean> routes = new HashMap<>();

    @Getter(AccessLevel.NONE)
    private boolean success = true;

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

    public StaticConfig(File file, ConfigHandler handler) {
        this(file, null, handler);
    }

    public List<String> getHeader() {
        return Collections.emptyList();
    }

    public void load() {
        try {
            document.reload();
            relocate();

            if (fields.isEmpty()) {
                step(getClass(), "", true);
            }

            for (Map.Entry<String, Field> entry : fields.entrySet()) {
                String route = entry.getKey();
                Field field = entry.getValue();

                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                try {
                    if (document.contains(route)) {
                        field.set(null, handler.provide(field).load(
                            new LoadContext(route, document.get(route))
                        ));
                    }
                } catch (Exception exception) {
                    throw new IOException("Failed to load key '" + route + "'.", exception);
                }
            }

            save();

            if (handler.isFormatValues()) {
                format();
            }
        } catch (Exception exception) {
            success = false;
            throw new LoadFailedException(file, exception);
        }

        success = true;
    }

    public void save() {
        try {
            if (!success) {
                throw new IllegalStateException("Cannot save until successful load, check above for errors.");
            }

            //noinspection ConstantValue
            if (document.getRoot() == null) {
                throw new IllegalStateException("Document must be loaded before it can be saved.");
            }

            if (handler.isFormatStructure()) {
                document.wipeComments();
            }

            // Clear header to avoid duplicate comments.
            document.wipeComments(document);

            step(getClass(), "", false);
            document.save();
        } catch (Exception exception) {
            throw new SaveFailedException(file, exception);
        }
    }

    private void step(Class<?> parent, String path, boolean initialize) throws ReflectiveOperationException {
        for (Field field : parent.getDeclaredFields()) {
            // Skip field if its modifiers are invalid, or it is marked @Ignore.
            if (!ClassUtils.hasModifiers(field, MODIFIERS) || field.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            String route = path + getRoute(field.getAnnotation(Key.class), field.getName());

            if (initialize) {
                routes.put(route, true);
                fields.put(route, field);
                continue;
            }

            boolean hidden = field.isAnnotationPresent(Hidden.class);
            boolean present = document.contains(route);

            boolean initial = (!present || Modifier.isFinal(field.getModifiers())) && !hidden;

            if (initial || (!hidden || present)) {
                set(route, field, field.get(null));
            }

            document.setComment(document.getBlock(route), field.getAnnotation(Comment.class));
        }

        Class<?>[] classes = parent.getDeclaredClasses();

        for (int i = classes.length - 1; i >= 0; i--) {
            Class<?> clazz = classes[i];

            if (!ClassUtils.hasModifiers(clazz, MODIFIERS) || clazz.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            String route = path + getRoute(clazz.getAnnotation(Key.class), clazz.getSimpleName());
            boolean manual = clazz.isAnnotationPresent(Manual.class);

            if (initialize) {
                this.routes.put(route, manual);
                step(clazz, route + ".", true);
                continue;
            }

            if (Modifier.isFinal(clazz.getModifiers())) {
                document.remove(route);
            }

            Section section = document.getSection(route);
            boolean skip = manual && section != null;

            if (section == null && !clazz.isAnnotationPresent(Hidden.class)) {
                section = document.createSection(route);
            }

            if (section != null && !skip) {
                document.setComment(section, clazz.getAnnotation(Comment.class));
                step(clazz, route + ".", false);
            }
        }
    }

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

                    handler.getLogger().warning(
                        (remove ? "Removed" : "Found") +
                        " unrecognised key '" + route + "'" +
                        " from '" + file.getName() + "'."
                    );

                    if (remove) {
                        document.remove(route);
                        removed = true;
                    }

                    continue;
                }

                if (document.isSection(route)) continue;

                // Update the field with their formatted values.
                Field field = fields.get(route);
                set(route, field, field.get(null));
            }

            // If there were any keys removed,
            // Create a backup of the file.
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

    protected void relocate(String target, String replacement) {
        relocations.computeIfAbsent(replacement, key -> new ArrayList<>()).add(target);
    }

    private void relocate() {
        relocations.forEach((replacement, targets) -> {
            for (String target : targets) {
                if (!document.contains(target)) continue;

                document.set(replacement, document.get(target));
                document.remove(target);

                handler.getLogger().warning(
                    "Value from '" + target + "' was " +
                    "relocated to '" + replacement + "'."
                );
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <T> void set(String key, Field field, T value) {
        SaveContext<T> context = new SaveContext<>(key, value);
        Object object = ((TypeProvider<T>) handler.provide(field)).save(context);
        document.set(context.getKey(), object);
    }

    private String getRoute(Key key, String name) {
        boolean valid = key != null && !key.value().trim().isEmpty();
        return valid ? key.value() : handler.getKeyFormatter().apply(name);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Key {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Comment {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Ignore {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    protected @interface Hidden {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    protected @interface Manual {
    }
}
