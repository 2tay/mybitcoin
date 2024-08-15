package com.example.Networking_Testing;

import com.example.Networking.Node;

public class testMinerNode {
    public static void main(String[] args) {
        Node minerNode = new Node(2005);
        minerNode.startNodeServer();
    }
}
