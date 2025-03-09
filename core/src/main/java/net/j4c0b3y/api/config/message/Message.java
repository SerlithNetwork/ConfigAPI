package net.j4c0b3y.api.config.message;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A utility class to easily parse and send translated messages to users.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/03/2025
 */
@Getter
public class Message {
    /**
     * The messages current lines.
     */
    private final List<String> lines;

    /**
     * If the message is the original.
     */
    private final boolean initial;

    /**
     * Creates a new message with the specified lines.
     *
     * @param lines The messages current lines.
     * @param initial If the message is the original.
     */
    private Message(Collection<String> lines, boolean initial) {
        this.lines = new ArrayList<>(lines);
        this.initial = initial;
    }

    /**
     * Creates a new message from a collection.
     *
     * @param lines The collection containing the lines.
     * @return The message.
     */
    public static Message of(Collection<String> lines) {
        return new Message(lines, true);
    }

    /**
     * Creates a new message from a string or multiple strings.
     *
     * @param lines The string or multiple strings.
     * @return The message.
     */
    public static Message of(String... lines) {
        return new Message(Arrays.asList(lines), true);
    }

    /**
     * Supplies the consumer with the lines of the current message if it isn't initial
     * or the lines of a new message with lines copied over, if it is the initial message.
     *
     * @param consumer The consumer to supply.
     * @return The non-initial message.
     */
    private Message supply(Consumer<List<String>> consumer) {
        Message message = initial ? new Message(lines, false) : this;
        consumer.accept(message.lines);
        return message;
    }

    /**
     * Replaces all the lines in the message using the mapping function.
     *
     * @param mapper The mapping function.
     * @return The mapped message.
     */
    public Message map(Function<String, String> mapper) {
        return supply(lines -> lines.replaceAll(mapper::apply));
    }

    /**
     * Removes lines that match the filter predicate from a message.
     *
     * @param filter The filter predicate.
     * @return The filtered message.
     */
    public Message filter(Predicate<String> filter) {
        return supply(lines -> lines.removeIf(filter));
    }

    /**
     * Replaces all instances of a string in the message.
     *
     * @param target The string to replace.
     * @param replacement The replacement value.
     * @return The replaced message.
     */
    public Message replace(String target, String replacement) {
        return map(line -> line.replaceAll(target, replacement));
    }

    /**
     * Sends all lines to a user using the provided consumer,
     *
     * @param consumer The send message consumer.
     */
    public void send(Consumer<String> consumer) {
        this.lines.forEach(consumer);
    }
}
