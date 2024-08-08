package com.example.Block;

import java.util.UUID;

import com.example.Transaction.Transaction;
import com.example.miner.BlockMiner;

public class Block {
    private String blockId;
    private Transaction transaction;
    private BlockMiner blockMiner;

    public Block(Transaction transaction, BlockMiner blockMiner) {
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

    public void deleteTransaction() {
        transaction = null;
    }

    public BlockMiner getBlockMiner() {
        return blockMiner;
    }

    @Override
    public String toString() {
        return "BlockID: " + blockId + ", Transaction: " + transaction + "BlockMiner: " + blockMiner.minerName;
    }
}
