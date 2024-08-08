package com.example.Block;

import com.example.Transaction.Transaction;
import com.example.miner.BlockMiner;
import com.example.utils.KeyUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class Block {
    private final String blockId;
    private final String previousBlockId;
    private final long timestamp;
    private final Transaction transaction;
    private final BlockMiner miner;
    private int nonce;

    public Block(Transaction transaction, String previousBlockId, BlockMiner miner) {
        this.transaction = transaction;
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

    public Transaction getTransaction() {
        return transaction;
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
        String dataToHash = previousBlockId + timestamp + transaction.toString() + miner.toString() + nonce;
        return KeyUtils.hashData(dataToHash);
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockId='" + blockId + '\'' +
                ", previousBlockId='" + previousBlockId + '\'' +
                ", timestamp=" + timestamp +
                ", transaction=" + transaction +
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