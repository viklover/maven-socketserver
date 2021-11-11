package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class ListCommand implements Command {

    @Override
    public boolean execute(String sender, Request request) throws IOException {

        if (request.name.equals("getListUsers")) {

            Response response = new Response(request.name);

            ArrayList<String> nicknames = new ArrayList<String>(Main.clients.keySet());

            for (int i = 0; i < nicknames.size(); ++i) {
                nicknames.set(i, "\"" + nicknames.get(i) + "\"");
            }

            response.setArgument("list", nicknames);

            Client client = Main.clients.get(sender);
            client.sendMessage(response);

            return true;
        }

        return false;
    }
}
