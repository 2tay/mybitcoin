package com.example.Networking.Client;

import com.example.Block.Block;
import com.example.Transaction.Transaction;

public class RequestHelper {

    public static Request postTx(Transaction tx) {
        return new Request("postTx", tx);
    }

    public static Request postBlock(Block block) {
        return new Request("postBlock", block);
    }

    public static Request getBlocks(Block block) {
        return new Request("getBlocks", block);
    }

    public static Request getBlockchain() {
        return new Request("getBlockchain");
    }

    // Main Server send new Node to Network Nodes
    public static Request postNode(String peerNodeInfos) {
        return new Request("postNode", peerNodeInfos);
    }

    // Post my nodeInfos to Main Server
    public static Request postMyNode(String myNodeInfos) {
        return new Request("postMyNode", myNodeInfos);
    }

    public static Request getNetwork() {
        return new Request("getNetwork");
    }
}
