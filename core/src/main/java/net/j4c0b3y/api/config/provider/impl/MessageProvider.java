package net.j4c0b3y.api.config.provider.impl;

import net.j4c0b3y.api.config.message.Message;
import net.j4c0b3y.api.config.provider.TypeProvider;
import net.j4c0b3y.api.config.provider.context.LoadContext;
import net.j4c0b3y.api.config.provider.context.SaveContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author J4C0B3Y
 * @version ConfigAPI
 * @since 9/03/2025
 */
public class MessageProvider implements TypeProvider<Message> {

    @Override
    public Message load(LoadContext context) {
        if (context.getObject() instanceof Collection) {
            List<String> parsed = new ArrayList<>();

            for (Object object : (Collection<?>) context.getObject()) {
                parsed.add(String.valueOf(object));
            }

            return Message.of(parsed);
        }

        return Message.of(String.valueOf(context.getObject()));
    }

    @Override
    public Object save(SaveContext<Message> context) {
        Message message = context.getObject();

        if (message.getLines().size() == 1) {
            return message.getLines().get(0);
        }

        return message.getLines();
    }
}
