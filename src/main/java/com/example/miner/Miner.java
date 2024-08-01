package com.example.miner;

import com.example.Pool.TransactionPool;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.utils.OutputWriter;
import com.example.utils.TransactionUtils;

import java.util.List;
import java.util.Random;

public class Miner extends Thread {
    private String minerName;
    private static volatile boolean running = true;
    private static final int DIFFICULTY = 100; // Simplified difficulty for proof-of-work

    public Miner(String minerName) {
        this.minerName = minerName;
    }

    @Override
    public void run() {
        // Get a transaction from the pool
        Transaction tx = getTransactionFromPool();
        if (tx == null) {
            OutputWriter.writeOutput(minerName + " found no transactions to mine.");
            return;
        }

        // Try to mine the transaction (simplified puzzle)
        OutputWriter.writeOutput(minerName + " is mining transaction: " + tx.getSignature());
        int attempts = 0;

        while(running) {
            attempts++;
            boolean mined = mineTransaction();
            if (mined) {
                OutputWriter.writeOutput(minerName + " successfully mined transaction in attempt: " + attempts);
                stopAllMiners();
            } else {
                OutputWriter.writeOutput(minerName + " failed to mine transaction attempt: " + attempts);
            }

            // Sleep to simulate time between mining attempts
            try {
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                OutputWriter.writeOutput("Error: " + e.getMessage());
            }
        }    
    }

    public void stopAllMiners() {
        running = false;
    }

    private Transaction getTransactionFromPool() {
        List<Transaction> mempool = TransactionPool.getMemPool();
        if (mempool.isEmpty()) {
            return null;
        }
        return mempool.get(new Random().nextInt(mempool.size()));
    }

    private boolean mineTransaction() {
        int nonce = new Random().nextInt(DIFFICULTY + 1);
        if(nonce == DIFFICULTY) {
            return true;
        }
        return false;
    }


    public static void test1() {
        Wallet h2tayWallet = UTXO.genesisUtxo();
        Wallet recipient = new Wallet();
        try {
            Transaction t1 = TransactionUtils.createTransaction(h2tayWallet, recipient.getPublicKey(), 100);
            OutputWriter.writeOutput("Transaction created: " + t1);
            boolean isValid = TransactionUtils.propagateTransaction(t1);
            OutputWriter.writeOutput("Added to network validity: " + isValid);
            List<Transaction> mempool = TransactionPool.getMemPool();
            for(Transaction tx: mempool) {
                OutputWriter.writeOutput("Transaction in mempool: " + tx.getPublicKey());
            }
        } catch (Exception e) {
            OutputWriter.writeOutput("Error: " + e.getMessage());
        }
        
        // Create and start multiple miners
        Miner miner1 = new Miner("Miner1");
        Miner miner2 = new Miner("Miner2");
        Miner miner3 = new Miner("Miner3");
        Miner miner4 = new Miner("miner4");
        Miner miner5 = new Miner("miner5");
        
        miner1.start();
        miner2.start();
        miner3.start();
        miner4.start();
        miner5.start();

        try {
            miner1.join();
            miner2.join();
            miner3.join();
            miner4.join();
            miner5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }


    public static void main(String[] args) {
        test1();
    }
}
