package com.example.Networking.Server;

import com.example.Networking.Client.Message;


public class HandleRequest {
    private HandleRequestHelper helper;
    private Response response;

    public HandleRequest(Message message) {
        helper = new HandleRequestHelper(message);
        response = handleRequestMessage();
    }

    public Response getResponse() {
        return response;
    }

    public Response handleRequestMessage() {
        if(helper.requestChoice("get", "block")) 
        {
            return helper.handleGetLatestBlock();
        }
        else if(helper.requestChoice("get", "blocks")) 
        {
            return helper.handleGetBlocksFrom();
        }
        else if(helper.requestChoice("post", "block"))
        {
            return helper.handlePostBlock();
        }
        else if(helper.requestChoice("get", "blockchain"))
        {
            return helper.handleGetBlockchain();
        }
        else if(helper.requestChoice("post", "transaction"))
        {
            return helper.handlePostTransaction();
        }
        else if(helper.requestChoice("get", "nodes"))
        {
            return helper.handleGetAllNodes();
        }
        else if(helper.requestChoice("post", "node"))
        {
            return helper.handlePostNode();
        }

        return new Response(Response.Status.BAD_REQUEST, "error: invalid syntax request");
    }

}
