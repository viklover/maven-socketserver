package com.company;

import java.util.ArrayList;
import java.util.List;

public class Event extends Message {

    private String[] fields_of_args = {
            "sender", "receiver", "message"
    };

    public Event() {
        super("event");

        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }

    public Event(String event) {
        super("event");

        this.name = event;
        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }
}
