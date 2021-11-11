package com.company;

import java.util.ArrayList;
import java.util.List;

public class Response extends Message {

    private String[] fields_of_args = {
            "list", "status", "password", "success"
    };

    public Response() {
        super("response");

        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }

    public Response(String event) {
        super("response");

        this.name = event;
        this.fields = new ArrayList<String>(List.of(fields_of_args));
    }
}
