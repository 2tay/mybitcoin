package com.example.Networking.Client;

import java.security.PublicKey;

import com.example.Block.Block;
import com.example.Transaction.Transaction;

public class MessageHelper {

    // BLOCK Messages
    public static Message msgGetLastBlock(Block myLastBlock) {
        Message message = new Message("get", "block", myLastBlock);
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
    public static Message msgGetTransaction(PublicKey pubkey) {
        Message message = new Message("get", "transaction", pubkey); // Msg to Get transaction by pubkey
        return message;
    }

    public static Message msgPostTransaction(Transaction tx) {
        Message message = new Message("post", "transaction", tx);
        return message;
    }
    
}
