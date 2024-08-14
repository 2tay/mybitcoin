package com.example.Networking.Client;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private String id;
    private String method;
    private String path;
    private Object argument;

    public Message(String method, String path, Object argument) {
        this.id = UUID.randomUUID().toString();
        this.method = method;
        this.path = path;
        this.argument = argument;
    }

    public String getId() {
        return id;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Object getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return method + " /" + path + ":" + argument;
    }

}
