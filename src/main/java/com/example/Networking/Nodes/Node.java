package com.example.Networking.Nodes;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.Networking.Client.Client;
import com.example.Networking.Client.Message;
import com.example.Networking.Client.MessageHelper;
import com.example.Networking.Server.Response;
import com.example.Networking.Server.Server;

public class Node {
    protected Server server;
    protected Client client;
    protected static List<String> peerNodes = new ArrayList<>();

    public Node(int serverPort) {
        this.server = new Server(serverPort);
        this.client = new Client();
    }

    public void startNodeServer() {
        new Thread(() -> server.startServer()).start();
    }

    public void stopNodeServer() {
        server.stopServer(); 
    }

    public void addMeToNetwork(String bootstrapNodeHost, int bootstrapNodePort) {
        try {
            String ipAdd = Inet4Address.getLocalHost().getHostAddress();
            Message msg = MessageHelper.msgPostNode(ipAdd, server.getPort());
            client.sendSerializedMessage(msg, bootstrapNodeHost, bootstrapNodePort);
        } 
        catch (UnknownHostException e) {
            System.err.println("Failed to get own node IP ADDRESS");
            e.printStackTrace();
        } 
    }

    public List<String> getNetworkNodes(String bootstrapNodeHost, int bootstrapNodePort) {
        Message msg = MessageHelper.msgGetAllNodes();
        Response response = client.sendSerializedMessage(msg, bootstrapNodeHost, bootstrapNodePort);

        if (response != null && response.getStatus() == Response.Status.OK && response.getContent() instanceof List<?>) {
            System.out.println("Response getAllNodes is not empty and response Status == OK");
            List<?> content = (List<?>) response.getContent();

            if (!content.isEmpty() && content.get(0) instanceof String) {
                return (List<String>) content;
            } else {
                System.err.println("Unexpected content type: " + content);
            }
        } else {
            System.err.println("Failed to retrieve network nodes or invalid content type.");
        }
        return Collections.emptyList(); // Return an empty list if something goes wrong
    }



    // -------------------->    TESTING FUNCTIONS    <-----------------------------------------
    // Start Node on port 2000
    public static void testStartNodeServer() {
        Node miner1 = new Node(2000);
        miner1.startNodeServer();
    }

    // Start Node on port:2005 && Send Object to node running on port:2000
    public static void testSendObject() {
        Node wallet1 = new Node(2005);
        Message tx0se = MessageHelper.msgGetBlockchain();
        wallet1.client.sendSerializedMessage(tx0se, "localhost", 2000);
    }

    public static void main(String[] args) {
        //testStartNodeServer();
        //testSendObject(); 
    }
}
