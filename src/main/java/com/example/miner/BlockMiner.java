package com.example.miner;

import java.util.List;
import java.util.Random;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.utils.Logger;
import com.example.utils.TransactionUtils;

public class BlockMiner extends Thread {
    public String minerName;
    private Block candidateBlock;
    private boolean running = true;
    private volatile boolean minningRound = false;
    private static final int DIFFICULTY = 100; // Simplified difficulty for proof-of-work

    public BlockMiner(String name) {
        minerName = name;
    }

    public void run() {
        while(running) {
            List<Transaction> txPool = TransactionPool.getMemPool();

            // Candidate Block already created
            if(candidateBlock != null) {
                // candidate block empty of mined Transactions OR set (candidateBlock = null)
                checkBlockTx(txPool);
            }

            if(candidateBlock == null) {
                createBlock(txPool);
            }

            while(minningRound) {
                Logger.log(minerName + " Trying to solve Block's puzzle: " + candidateBlock.getBlockId());
                if(solveMinningPuzzle()) {
                    mineBlock(candidateBlock);
                } else {
                    System.out.println(minerName + " failed to solve puzzle");
                }
            }

            // stop time re-render Mempool
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void mineBlock(Block block) {
        Logger.log(minerName + " Try to Mine");
        if(TransactionUtils.verifyTransaction(block.geTransaction())) {

            TransactionPool.removeTransaction(block.geTransaction());

            UTXOSet.removeConsumedUtxosTransaction(block.geTransaction());
            UTXOSet.addUtxosTransaction(block.geTransaction());

            Blockchain.addToBlockchain(block);
            Logger.log(minerName + " Mined Block: " + block.getBlockId());

            minningRound = false;
        } else {
            Logger.log(minerName + " Block's Transaction not valid!");
        }
    }

    public void stopMiner() {
        running = false;
    }

    public void checkBlockTx(List<Transaction> txPool) {
        // txpool doesnt contain candidateBlock's transaction -----------> Transaction in the block already mined
        if(!txPool.contains(candidateBlock.geTransaction())) {
            candidateBlock = null;
        }
    }

    public void createBlock(List<Transaction> txPool) {
        if(txPool.size() > 0) {
            Transaction newTx = txPool.get(new Random().nextInt(txPool.size() - 1));
            candidateBlock = new Block(newTx, this);
            // After Candidate Block Creation
            minningRound = true;
        } else {
            System.out.println("TxPool is empty! try later");
        }

    }

    private boolean solveMinningPuzzle() {
        int nonce = new Random().nextInt(DIFFICULTY + 1);
        return nonce == DIFFICULTY;
    }
}
