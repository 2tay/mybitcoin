package com.example.Blockchain;

import java.util.ArrayList;
import java.util.List;

import com.example.Block.Block;

public class Blockchain {
    private static List<Block> blockchain = new ArrayList<>();

    public static List<Block> getBlockchain() {
        return blockchain;
    }

    public static Block getLastBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    public static void addBlock(Block block) {
        blockchain.add(block);
    }
}
