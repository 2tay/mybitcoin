package com.Block;

import java.util.UUID;

import com.example.Transaction.Transaction;
import com.example.miner.Miner;

public class Block {
    private String blockId;
    private Transaction transaction;
    private Miner blockMiner;

    public Block(Transaction transaction, Miner blockMiner) {
        this.blockId = UUID.randomUUID().toString();
        this.transaction = transaction;
        this.blockMiner = blockMiner;
    }

    public String getBlockId() {
        return blockId;
    }

    public Transaction geTransaction() {
        return transaction;
    }

    public Miner getBlockMiner() {
        return blockMiner;
    }

    @Override
    public String toString() {
        return "BlockID: " + blockId + ", Transaction: " + transaction + "BlockMiner: " + blockMiner.minerName;
    }
}
