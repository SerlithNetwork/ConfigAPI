package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.message.Message;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used for providing the message utility class.
 *
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/03/2025
 */
public class MessageProvider implements TypeProvider<Message> {

    @Override
    public @NotNull Message load(@NotNull LoadContext context) {
        // If the object is a collection.
        if (context.getObject() instanceof Collection) {
            List<String> parsed = new ArrayList<>();

            // Parse each item and add it to the parsed list.
            for (Object object : (Collection<?>) context.getObject()) {
                parsed.add(String.valueOf(object));
            }

            // Return the message based off the parsed list.
            return Message.of(parsed);
        }

        // If the object is anything else, including a string,
        // parse it then return a 1 line message.
        return Message.of(String.valueOf(context.getObject()));
    }

    @Override
    public @NotNull Object save(@NotNull SaveContext<Message> context) {
        Message message = context.getObject();

        // If the message is one line, return the first line as a string.
        if (message.getLines().size() == 1) {
            return message.getLines().getFirst();
        }

        // If the message is multi-line, return all lines as a list.
        return message.getLines();
    }
}
