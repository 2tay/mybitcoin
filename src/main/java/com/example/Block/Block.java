package com.example.Block;

import com.example.Transaction.Transaction;
import com.example.miner.BlockMiner;
import com.example.utils.KeyUtils;

import java.time.Instant;
import java.util.List;

public class Block {
    private final String blockId;
    private final String previousBlockId;
    private final long timestamp;
    private final List<Transaction> transactions;
    private final BlockMiner miner;
    private int nonce;

    public Block(List<Transaction> transactions, String previousBlockId, BlockMiner miner) {
        if(transactions == null || transactions.isEmpty() || miner == null) {
            throw new IllegalArgumentException("Block cannot have null transactions or miner!");
        }
        this.transactions = transactions;
        this.previousBlockId = previousBlockId;
        this.miner = miner;
        this.timestamp = Instant.now().getEpochSecond();
        this.nonce = 0;
        this.blockId = calculateBlockId();
    }

    public String getBlockId() {
        return blockId;
    }

    public String getPreviousBlockId() {
        return previousBlockId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public BlockMiner getMiner() {
        return miner;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    public void incrementNonce() {
        this.nonce++;
    }

    private String calculateBlockId() {
        String dataToHash = previousBlockId + timestamp + transactions.toString() + miner.toString() + nonce;
        return KeyUtils.hashData(dataToHash);
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockId='" + blockId + '\'' +
                ", previousBlockId='" + previousBlockId + '\'' +
                ", timestamp=" + timestamp +
                ", transactions=" + transactions +
                ", miner=" + miner.minerName +
                ", nonce=" + nonce +
                '}';
    }

    public static void testNullBlockId() {
        Block block = new Block(null, null, null);
        System.out.println(block);
    }

    public static void main(String[] args) {
        
    }
}