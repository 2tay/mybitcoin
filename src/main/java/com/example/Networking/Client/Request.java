package com.example.Networking.Client;

import java.io.Serializable;

public class Request implements Serializable {
    private String method;
    private Object argument;

    public Request(String method, Object argument) {
        this.method = method;
        this.argument = argument;
    }

    public Request(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public Object getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        return "method: " + method + "/ argument: " + argument;
    }
}
