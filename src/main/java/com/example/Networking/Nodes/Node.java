package com.example.Networking.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.example.Networking.Client.Client;
import com.example.Networking.Client.Message;
import com.example.Networking.Client.MessageHelper;
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
        // start server in a new Thread
        new Thread(() -> server.startServer()).start();
    }

    public void stopNodeServer() {
        server.stopServer(); 
    }

    public void sendMessageToPeer(Message message, String peerHost, int peerPort) {
        new Thread(() -> client.sendSerializedMessage(message, peerHost, peerPort)).start();
    }

    public static List<String> getAllNodes() {
        return new ArrayList<>(peerNodes);
    }

    public static void addNode(String nodeInfos) {
        peerNodes.add(nodeInfos);
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
        wallet1.sendMessageToPeer(tx0se, "localhost", 2000);
    }

    public static void main(String[] args) {
        //testStartNodeServer();
        testSendObject(); 
    }
}
