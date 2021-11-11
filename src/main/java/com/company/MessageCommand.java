package com.company;

import java.io.IOException;

public class MessageCommand implements Command {

    @Override
    public boolean execute(String sender, Request request) throws IOException {

        if (request.name.equals("sendMessage")) {

            Event event = new Event("message");
            event.setArgsFromMessage(request);

            if (((String) event.getArgument("receiver")).equals("all")) {
                Main.sendEventToAll(event);
                Main.log(String.format("New message from \"%s\": \"%s\"", event.getArgument("sender"), event.getArgument("message")));
            } else {
                Client client = Main.clients.get((String) event.getArgument("receiver"));
                client.sendMessage(event);

                Main.log(String.format("New message to \"%s\" from \"%s\": \"%s\"",
                        event.getArgument("receiver"), event.getArgument("sender"), event.getArgument("message"))
                );
            }

            return true;
        }

        return false;
    }
}
