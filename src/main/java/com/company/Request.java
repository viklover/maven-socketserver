package com.company;

import java.util.ArrayList;
import java.util.List;

public class Request extends Message {

    private String[] fields_of_args = {
            "sender", "receiver", "message", "nickname", "password"
    };

    public Request() {
        super("request");

        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }

    public Request(String request) {
        super("request");

        this.name = request;

        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }
}
