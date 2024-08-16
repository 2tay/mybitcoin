package com.example.Networking_Testing;

import com.example.Blockchain.Blockchain;
import com.example.Networking.Nodes.Node;

public class testMinerNode {
    public static void main(String[] args) {
        Node minerNode = new Node(2000);

        minerNode.startNodeServer();
        
        System.out.println("Blockchain size before: " + Blockchain.getBlockchain().size());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Blockchain size after: " + Blockchain.getBlockchain().size());


    }
}
