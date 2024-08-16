package com.example.Networking.Client;

import com.example.Block.Block;
import com.example.Transaction.Transaction;

public class MessageHelper {

    // Block 
    public static Message msgGetLastBlock() {
        Message message = new Message("get", "block", null);
        return message;
    }

    public static Message msgGetBlocksFrom(Block fromBlock) {
        Message message = new Message("get", "blocks", fromBlock);
        return message;
    }

    public static Message msgPostBlock(Block minedBlock) {
        Message message = new Message("post", "block", minedBlock);
        return message;
    }

    // Blockchain 
    public static Message msgGetBlockchain() {
        Message message = new Message("get", "blockchain", null);
        return message;
    }

    // Transaction
    public static Message msgPostTransaction(Transaction tx) {
        Message message = new Message("post", "transaction", tx);
        return message;
    }

    // Nodes : (Nodes in the network)
    public static Message msgGetAllNodes() {
        Message message = new Message("get", "nodes", null);
        return message;
    }

    public static Message msgPostNode(String nodeHost, String nodePort) {
        String nodeInfos = nodeHost + ":" + nodePort;
        Message message = new Message("post", "node", nodeInfos);
        return message;
    }

    
}
