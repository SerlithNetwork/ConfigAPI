package net.j4c0b3y.api.config.message;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/03/2025
 */
@Getter
public class Message {
    private final List<String> lines;
    private final boolean initial;

    private Message(Collection<String> lines, boolean initial) {
        this.lines = new ArrayList<>(lines);
        this.initial = initial;
    }

    public static Message of(Collection<String> lines) {
        return new Message(lines, true);
    }

    public static Message of(String... lines) {
        return new Message(Arrays.asList(lines), true);
    }

    private Message supply(Consumer<List<String>> consumer) {
        Message message = initial ? new Message(lines, false) : this;
        consumer.accept(message.lines);
        return message;
    }

    public Message map(Function<String, String> mapper) {
        return supply(lines -> lines.replaceAll(mapper::apply));
    }

    public Message filter(Predicate<String> filter) {
        return supply(lines -> lines.removeIf(filter));
    }

    public Message replace(String target, String replacement) {
        return map(line -> line.replaceAll(target, replacement));
    }

    public void send(Consumer<String> consumer) {
        this.lines.forEach(consumer);
    }
}
