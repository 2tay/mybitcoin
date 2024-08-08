package com.example.Blockchain;

import java.util.ArrayList;
import java.util.List;

import com.example.Block.Block;

public class Blockchain {
    private static List<Block> blockchain = new ArrayList<>();
    
    public static List<Block> getBlockchain() {
        return blockchain;
    }

    public static void addToBlockchain(Block block) {
        blockchain.add(block);
    }
}
