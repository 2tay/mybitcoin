package com.example.Networking;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private String id;
    private String body;

    public Message(String body) {
        this.id = UUID.randomUUID().toString();
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public String getBody() {
        return body;
    }
}
