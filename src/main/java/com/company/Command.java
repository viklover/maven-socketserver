package com.company;

import java.io.IOException;

public interface Command {
    public boolean execute(String name, Request request) throws IOException;
}
