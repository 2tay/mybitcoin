package com.example.Networking.Server;

import com.example.Networking.Client.Message;


public class HandleRequest {
    private ResponseHelper helper;
    private Response response;

    public HandleRequest(Message message) {
        helper = new ResponseHelper(message);
        response = handleRequestMessage();
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

        return new Response(Response.Status.BAD_REQUEST, null);
    }

}
