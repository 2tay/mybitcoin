package com.example.Networking;

public class Node {
    private Server server;
    private Client client;

    public Node(int serverPort) {
        this.server = new Server(serverPort);
        this.client = new Client();
    }

    public void startNode() {
        // start server in a new Thread
        new Thread(() -> server.startServer()).start();
    }

    public void connectToPeer(String peerHost, int peerPort) {
        // start client to connect with another Node
        new Thread(() -> client.connectToServer(peerHost, peerPort)).start();
    }


    // TESTING FUNCTIONS
    public static void test1() {
        Node miner1 = new Node(2000);
        miner1.startNode();
    }

    public static void test2() {
        Node wallet1 = new Node(2005);
        wallet1.connectToPeer("localhost", 2000);
    }

    public static void main(String[] args) {
        test1();
        // test2(); // in another terminal 
    }
}
