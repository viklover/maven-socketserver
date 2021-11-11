package com.company;

public class DefaultCommand implements Command {

    @Override
    public boolean execute(String sender, Request request) {
        Main.sendEventToAll(request);
        Main.log(String.format("New event from \"%s\": \"%s\"", request.getArgument("sender"), request));
        return true;
    }
}
