package net.j4c0b3y.api.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.Block;
import dev.dejvokep.boostedyaml.block.Comments;
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
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/10/2024
 */
public class ConfigDocument extends YamlDocument {
    private final StaticConfig config;
    private final ConfigHandler handler;

    private final boolean allowLoad;

    public ConfigDocument(StaticConfig config, InputStream defaults) throws IOException {
        super(config.getFile(), defaults, config.getHandler().getDocumentSettings());
        this.config = config;
        this.handler = config.getHandler();
        this.allowLoad = true;
    }

    @Override
    public String dump(@NotNull DumperSettings settings) {
        String dump = super.dump(settings);
        String content = handler.isFormatStructure() ? format(dump) : dump;

        String header = config.getHeader()
            .stream()
            .map(line -> "# " + line + "\n")
            .collect(Collectors.joining());

        return header + (!header.isEmpty() ? "\n" : "") + content;
    }

    @Override
    public boolean reload() throws IOException {
        return allowLoad && super.reload();
    }

    public void wipeComments() {
        for (Route route : getRoutes(true)) {
            wipeComments(getBlock(route));
        }

        wipeComments(this);
    }

    public void wipeComments(Block<?> block) {
        for (NodeRole role : NodeRole.values()) {
            for (Comments.Position position : Comments.Position.values()) {
                Comments.remove(block, role, position);
            }
        }
    }

    protected void setComment(Block<?> block, StaticConfig.Comment comment) {
        if (comment == null) return;

        List<String> lines = Arrays.stream(comment.value())
            .map(line -> " " + line)
            .collect(Collectors.toList());

        block.setComments(lines);
    }

    public String format(String content) {
        List<String> formatted = new ArrayList<>();
        String[] lines = content.split("\n");

        int previous = -1;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) continue;

            int current = StringUtils.getIndentation(lines[i]);

            if (current == 0 && previous > 0) {
                lines[i] = "\n" + lines[i];
            }

            previous = current;
            formatted.add(lines[i]);
        }

        return String.join("\n", formatted);
    }

    public String backup() throws IOException {
        Path path = getFile() != null ? getFile().toPath() : null;

        if (path == null) {
            throw new IllegalStateException("The document must be loaded to take a backup.");
        }

        return PathUtils.createBackup(path, handler.getBackupDateFormat());
    }
}
