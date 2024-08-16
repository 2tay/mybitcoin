package com.example.Blockchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.Block.Block;
import com.example.Pool.TransactionPool;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.miner.BlockMiner;

public class Blockchain {
    private static final List<Block> chain = new ArrayList<>();

    /*static {
        Block genisisBlock = genisisBlock();
        chain.add(genisisBlock);
    }*/

    public static List<Block> getBlockchain() {
        return new ArrayList<>(chain);
    }

    public static synchronized void addToBlockchain(Block block) {
        chain.add(block);
    }

    public static synchronized Block getLatestBlock() {
        return chain.isEmpty() ? null : chain.get(chain.size() - 1);
    }

    public static synchronized String getLatestBlockId() {
        Block latestBlock = getLatestBlock();
        return latestBlock != null ? latestBlock.getBlockId() : null;
    }

    /*public static Block genisisBlock() {
        Wallet satoshiWallet = new Wallet();
        BlockMiner miner = new BlockMiner("Miner0");
        UTXO.genesisUtxo(satoshiWallet, 10);
        Transaction tx = satoshiWallet.processTransaction(satoshiWallet.getPublicKey(), 10);
        Block firstBlock = new Block(new ArrayList<>(Arrays.asList(tx)), null, miner);
        TransactionPool.removeTransaction(tx);
        return firstBlock;
    }*/

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
