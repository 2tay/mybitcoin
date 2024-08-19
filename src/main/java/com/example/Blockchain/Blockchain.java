package com.example.Blockchain;

import java.util.ArrayList;
import java.util.List;
import com.example.Block.Block;
import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.utils.TransactionUtils;

public class Blockchain {
    private static final List<Block> chain = new ArrayList<>();

    public static List<Block> getBlockchain() {
        return new ArrayList<>(chain);
    }

    public static boolean verifyBlock(Block newBlock) {
        for(Block block : chain) {
            if(block.getBlockId().equals(newBlock.getBlockId())) {
                return false;
            }
            if(block.getTransactions().equals(newBlock.getTransactions())) {
                return false;
            }
        }

        for(Transaction tx : newBlock.getTransactions()) {
            if(!TransactionUtils.verifyTransaction(tx)) {
                return false;
            }
        }

        return true;
    }

    public static synchronized void addToBlockchain(Block block) {
        for(Transaction tx : block.getTransactions()) {
            TransactionPool.removeTransaction(tx);
            UTXOSet.removeConsumedUtxosTransaction(tx);
            UTXOSet.addUtxosTransaction(tx);
        }
        chain.add(block);
    }

    public static synchronized Block getLatestBlock() {
        return chain.isEmpty() ? null : chain.get(chain.size() - 1);
    }

    public static synchronized String getLatestBlockId() {
        Block latestBlock = getLatestBlock();
        return latestBlock != null ? latestBlock.getBlockId() : null;
    }


    public static List<Block> getNewBlocks(Block block) {
        int index = chain.indexOf(block);
        if(index != -1) {
            return new ArrayList<>(chain.subList(index, chain.size()));
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(chain.size());
    }

}
