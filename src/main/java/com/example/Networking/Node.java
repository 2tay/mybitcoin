package com.example.Networking;

public class Node {
    private Server server;
    private Client client;

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

    public void connectToPeer(String peerHost, int peerPort) {
        // start client to connect with another Node
        new Thread(() -> client.connectToServer(peerHost, peerPort, "hello world!")).start();
    }

    public void sendObjectToPeer(Object o, String peerHost, int peerPort) {
        new Thread(() -> client.sendSerializObject(o, peerHost, peerPort)).start();
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
        Message tx0se = new Message("tx0");
        wallet1.sendObjectToPeer(tx0se, "localhost", 2000);
    }

    public static void main(String[] args) {
        //testStartNodeServer();
        testSendObject(); 
    }
}
