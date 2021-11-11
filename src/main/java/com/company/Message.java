package com.company;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Message {

    protected String type;
    public String name;

    protected ArrayList<String> fields = new ArrayList<String>();

    public Hashtable<String, Object> args = new Hashtable<String, Object>();;

    public Message() {

    }

    public Message(String type) {
        this.type = type;
    }

    public void setJSON(String json) {
        parse(json);
    }

    public boolean setArgument(String key, Object value) {
        return args.put(key, value) != null;
    }

    public void setArgsFromMessage(Message message) {
        this.args = message.args;
    }

    public Object getArgument(String key) {
        if (args.contains(key)) {
            return args.get(key);
        }

        return args.get(key);
    }

    public String getJSON() {
        String out = "{\""+this.type+"\" : \""+this.name+"\", \"args\" : {";

        ArrayList<String> keys = new ArrayList<String>(this.args.keySet());
        String key;

        for (int i = 0; i < keys.size(); ++i) {
            key = keys.get(i);

            if (i != 0) {
                out += ", ";
            }

            if (args.get(key) instanceof ArrayList || args.get(key) instanceof Boolean) {
                out += "\""+key+"\"" + " : " + String.valueOf(args.get(key)) + "";
            } else {
                out += "\""+key+"\"" + " : \"" + String.valueOf(args.get(key)) + "\"";
            }
        }

        out += "}}";

        return out;
    }

    @Override
    public String toString() {
        return getJSON();
    }

    void parse(String data) {

        if (!data.contains(this.type)) {
            return;
        }

        // PARSE TYPE NAME
        int start_index = data.indexOf("\"", data.indexOf(":")) + 1;
        int end_index = data.indexOf(",", data.indexOf(this.type)) - 1;

        if (end_index == -2) {
            end_index = data.indexOf("}", start_index) - 1;
        }

        this.name = data.substring(start_index, end_index);

        if (data.contains("args"))
        {
            start_index = data.indexOf("{", data.indexOf("args")) + 1;

            if (data.indexOf("{", start_index) == -1) {

                end_index = data.indexOf("}", start_index);

                if (data.substring(start_index, end_index).length() > 0) {
                    String line = data.substring(start_index, end_index);
                    parseArguments(line);
                }

            } else {
                // парсинг ключей со словарями
            }
        }
    }

    private void parseArguments(String line)
    {
        int start_index;
        int end_index = 0;

        for (String field : this.fields) {

            if (line.contains(field) && field != null)
            {
                boolean is_array = false;
                boolean is_boolean = false;

                start_index = line.indexOf(":", line.indexOf(field)) + 1;
                end_index = line.indexOf(",", start_index);

                if (line.indexOf(",", start_index) != -1) {
                    end_index = line.indexOf(",", start_index);
                } else {
                    end_index = line.length();
                }

                if (!line.substring(start_index, end_index).contains("\"")
                        && (line.substring(start_index, end_index).contains("false")) ||
                            line.substring(start_index, end_index).contains("true"))
                {
                    is_boolean = true;
                }

                // ARRAY
                if (line.substring(start_index, end_index).contains("[")) {
                    end_index = line.indexOf("]", start_index) + 1;
                    is_array = true;
                }

                //STRING
                if (count(line.substring(start_index, end_index), "\"") < 2 && !is_boolean) {

                    start_index = line.indexOf("\"", line.indexOf("\"", line.indexOf("\""+field+"\"")+field.length()+2)) + 1;
                    end_index = line.indexOf("\"", start_index) + 1;

                    start_index--;

                } else {
                    if (!is_boolean) {
                        start_index++;
                    }
                }

                // IF IS STRING
                if (!is_array && !is_boolean) {
                    start_index++;
                    end_index--;
                }

                String value = line.substring(start_index, end_index).trim();

                if (is_array) {

                    value = value.substring(1, value.length()-1);

                    ArrayList<String> array = new ArrayList<String>(
                            List.of(value.split(", "))
                    );

                    this.args.put(field, array);
                }
                else if (is_boolean) {
                    this.args.put(field, Boolean.valueOf(value));
                }
                else {
                    this.args.put(field, value);
                }
            }
        }
    }

    int count(String text, String symbol) {
        return (text.length() - text.replace(symbol, "").length());
    }
}
