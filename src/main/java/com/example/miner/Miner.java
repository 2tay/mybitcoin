package com.example.miner;

import com.example.Block.Block;
import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.TransactionCreator;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.utils.TransactionUtils;
import com.example.Logger.Logger;

import java.util.List;
import java.util.Random;


public class Miner extends Thread {
    public String minerName;
    private static volatile boolean minningRound = false;
    private static volatile boolean running = true;
    private static final int DIFFICULTY = 100; // Simplified difficulty for proof-of-work
    private static Transaction minedTransaction;

    public Miner(String minerName) {
        this.minerName = minerName;
    }

    @Override
    public void run() {
        // Get a transaction from the pool
        Transaction tx = getTransactionFromPool();
        while(running) {
            //reRender miner WorkingTransaction (to make sure)
            if(minedTransaction != null && tx == minedTransaction) {
                tx = null;
            }

            // handle empty transactionPool : infinte loop with one second delay between loops until transaction added
            while(tx == null) {
                Logger.writeLogs(this,minerName + " found no transactions to mine yet.");
                
                try {
                    // Wait second to try to getTransaction from pool 
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tx = getTransactionFromPool();
            }

            // start new minning round after recent block mined
            minningRound = true;

            // Try to mine the transaction (simplified puzzle)
            int attempts = 0;

            while(minningRound) {
                attempts++;
                boolean solved = solveMinningPuzzle();
                if (solved) {
                    Logger.writeLogs(this, minerName + " successfully Solve puzzle in attempt: " + attempts);
                    mineTransaction(tx);
                    Logger.writeLogs(this, minerName + " CREATED BLOCK SUCCESSFULY!");
                    roundFinished();
                }

                // Sleep to simulate time between mining attempts
                try {
                    Thread.sleep(new Random().nextInt(2000));
                } catch (InterruptedException e) {
                    Logger.writeLogs(this, "Error: " + e.getMessage());
                }
            }    
        }
    }

    public void stopAllMiners() {
        running = false;
    }

    public void roundFinished() {
        minningRound = false;
    }

    private boolean solveMinningPuzzle() {
        int nonce = new Random().nextInt(DIFFICULTY + 1);
        return nonce == DIFFICULTY;
    }

    private Transaction getTransactionFromPool() {
        List<Transaction> mempool = TransactionPool.getMemPool();
        if (mempool.isEmpty()) {
            return null;
        }
        return mempool.get(new Random().nextInt(mempool.size()));
    }

    private boolean mineTransaction(Transaction tx) {
        Logger.writeLogs(tx, minerName + "is mining transaction: " + tx);
        // Miner Checking transaction validity (Second time checking overall)
        if(TransactionUtils.verifyTransaction(tx)) {
            // Add the new Utxos to UtxoSet
            UTXOSet.addUtxosTransaction(tx);

            // Update UTXOs
            UTXOSet.removeConsumedUtxosTransaction(tx);

            //remove transaction from transactionPool
            minedTransaction = tx;
            TransactionPool.removeTransaction(tx);

            // Create Block with Mined Transaction
            Block minedBlock = new Block(tx, this);
            Logger.writeLogs(minedBlock, minedBlock.toString());
            return true;
        }
        Logger.writeLogs(this, "Transaction verification by " + minerName + ": not valid transaction");
        return false;
    }


    public static void test1() {
        Wallet h2tayWallet = UTXO.genesisUtxo();
        Wallet recipient = new Wallet();
        try {
            Transaction t1 = TransactionUtils.createTransaction(h2tayWallet, recipient.getPublicKey(), 100);
            Logger.writeLogs(t1, "Transaction created: " + t1);
            boolean isValid = TransactionUtils.propagateTransaction(t1);
            Logger.writeLogs(t1, "Added to network validity: " + isValid);
            List<Transaction> mempool = TransactionPool.getMemPool();
            for(Transaction tx: mempool) {
                Logger.writeLogs(t1, "Transaction in mempool: " + tx.getPublicKey());
            }
        } catch (Exception e) {
            Logger.writeLogs(h2tayWallet, "Error: " + e.getMessage());
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

    //test5: test multiple transaction in mempool
    public static void test2() {
        // Create multiple transaction and added it to TransactionPOOL
        TransactionCreator.test1();

        // Create and start multiple Miners
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
        test2();
    }
}
