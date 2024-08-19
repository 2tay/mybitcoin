package com.example.Networking.Nodes;

import java.util.ArrayList;
import java.util.List;

import com.example.Wallet.Wallet;

public class BootstrapNode extends Node {
    private static final int PORT = 2000;
    private static final String HOST = "localhost";
    private static Wallet satoshiWallet;
    
    public BootstrapNode() {
        super(PORT);
        satoshiWallet = new Wallet();
        BootstrapHelper.genisisBlock(satoshiWallet);
        peerNodes.add(HOST + ":" + PORT);
        startNodeServer();
    }


    public static List<String> getAllNodes() {
        return new ArrayList<>(peerNodes);
    }

    public static void addNode(String nodeInfos) {
        peerNodes.add(nodeInfos);
    }

    public static void main(String[] args) {
        BootstrapNode bnode = new BootstrapNode();
    }
}
