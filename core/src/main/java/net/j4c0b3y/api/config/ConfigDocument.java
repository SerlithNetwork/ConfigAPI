package net.j4c0b3y.api.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.Block;
import dev.dejvokep.boostedyaml.block.Comments;
import dev.dejvokep.boostedyaml.block.Comments.Position;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.comments.CommentLine;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.utils.format.NodeRole;
import net.j4c0b3y.api.config.utils.PathUtils;
import net.j4c0b3y.api.config.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used for the internal loading, and file management.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */
public class ConfigDocument extends YamlDocument {
    /**
     * The associated static config.
     */
    private final StaticConfig config;

    /**
     * The static config's associated handler.
     */
    private final ConfigHandler handler;

    /**
     * The YamlDocument automatically loads in the constructor,
     * we intercept the load and prevent it if this value is false.
     * The value is false until this class's constructor is called,
     * then loading is allowed, which should be done manually by the user.
     */
    private final boolean allowLoad;

    /**
     * Creates a new config document to be used with our static config.
     *
     * @param config The associated static config.
     * @param defaults The default values.
     */
    public ConfigDocument(StaticConfig config, InputStream defaults) throws IOException {
        super(config.getFile(), defaults, config.getHandler().getDocumentSettings());
        this.config = config;
        this.handler = config.getHandler();
        this.allowLoad = true;
    }

    /**
     * Dumps the yaml document, formatting the structure and adding the file header.
     *
     * @param settings The dumper settings.
     * @return The dumped yaml document.
     */
    @Override
    public String dump(@NotNull DumperSettings settings) {
        String dump = super.dump(settings);
        String content = handler.isFormatStructure() ? format(dump) : dump;

        String header = config.getHeader()
            .stream()
            .map(line -> "# " + line + "\n")
            .collect(Collectors.joining());

        String footer = config.getFooter()
            .stream()
            .map(line -> "# " + line + "\n")
            .collect(Collectors.joining());

        return header + (!header.isEmpty() ? "\n" : "") + content + "\n" + footer;
    }

    /**
     * Intercept reloading to prevent the initial load.
     */
    @Override
    public boolean reload() throws IOException {
        return allowLoad && super.reload();
    }

    /**
     * Wipe all comments on the entire document.
     */
    public void wipeComments() {
        for (Route route : getRoutes(true)) {
            wipeComments(getBlock(route));
        }

        wipeComments(this);
    }

    /**
     * Wipes comments on all possible positions and roles for a block.
     *
     * @param block The block to wipe comments for.
     */
    public void wipeComments(Block<?> block) {
        for (NodeRole role : NodeRole.values()) {
            for (Comments.Position position : Comments.Position.values()) {
                Comments.remove(block, role, position);
            }
        }
    }

    /**
     * Sets a block's comment using the Comment annotation value.
     *
     * @param block The block to set the comment for.
     * @param comment The comment annotation.
     */
    protected void setComment(Block<?> block, StaticConfig.Comment comment) {
        if (block != null && comment != null) {
            setComment(block, Arrays.asList(comment.value()), comment.side());
        }
    }

    /**
     * Sets a block's comment using a string list.
     *
     * @param block The block to set the comment for.
     * @param comment The comment list.
     */
    protected void setComment(Block<?> block, List<String> comment, boolean side) {
        Position position = side ? Position.INLINE : Position.BEFORE;
        NodeRole role = side ? NodeRole.VALUE : NodeRole.KEY;

        List<CommentLine> lines = comment.stream()
            .map(line -> Comments.create(" " + line, position))
            .collect(Collectors.toList());

        Comments.set(block, role, position, lines);
    }

    /**
     * Formats the config document structure.
     *
     * @param content The file content.
     * @return The formatted content.
     */
    public String format(String content) {
        List<String> formatted = new ArrayList<>();
        String[] lines = content.split("\n");

        int previousIndentation = -1;
        boolean previousComment = false;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) continue;

            int currentIndentation = StringUtils.getIndentation(lines[i]);
            boolean currentComment = lines[i].trim().startsWith("#");

            boolean comment = currentComment && !previousComment && previousIndentation >= currentIndentation;
            boolean section = currentIndentation == 0 && previousIndentation > 0 && !previousComment;

            if (comment || section) {
                lines[i] = "\n" + lines[i];
            }

            previousIndentation = currentIndentation;
            previousComment = currentComment;

            formatted.add(lines[i]);
        }

        return String.join("\n", formatted);
    }

    /**
     * Creates a backup of the document file.
     *
     * @return The backup file name.
     */
    public String backup() throws IOException {
        Path path = getFile() != null ? getFile().toPath() : null;

        if (path == null) {
            throw new IllegalStateException("The document must be loaded to take a backup.");
        }

        return PathUtils.createBackup(path, handler.getBackupDateFormat());
    }
}
