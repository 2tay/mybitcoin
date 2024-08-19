package com.example.miner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Request;
import com.example.Networking.Client.RequestHelper;
import com.example.Networking.Nodes.Node;
import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.TransactionThread;
import com.example.Wallet.Wallet;
import com.example.utils.Logger;
import com.example.utils.TransactionUtils;

public class BlockMiner extends Thread implements Serializable {
    public final String minerName;
    private volatile Block candidateBlock;
    private final AtomicBoolean running;
    private static final int DIFFICULTY = 1000; // Configurable difficulty for proof-of-work
    private static final int MINING_INTERVAL = 100; // 100ms interval between mining attempts
    private static final int TIMEOUT = 5000; // 5 seconds timeout for stopping the miner
    private static final int BLOCK_TXS_COUNT = 3;
    private long lastBlockCreationTime;
    private final Random random;

    public BlockMiner(String name) {
        this.minerName = name;
        this.running = new AtomicBoolean(true);
        lastBlockCreationTime = System.currentTimeMillis();
        this.random = new Random();
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                if (candidateBlock == null || isBlockchainUpdated()) {
                    updateCandidateBlock();
                }

                if (candidateBlock != null) {
                    lastBlockCreationTime = System.currentTimeMillis();
                    boolean solved = solveMiningPuzzle();
                    if (solved) {
                        mineBlock(candidateBlock);
                        // Broadcast Mined Block to all Network
                        Request req = RequestHelper.postBlock(candidateBlock);
                        Node.sendReqToAllNetwork(req);
                    }
                }

                if (isTransactionPoolEmptyForTooLong()) {
                    Logger.log(minerName + ": No transactions to mine. Stopping miner.");
                    stopMiner();
                }

                Thread.sleep(MINING_INTERVAL);
            } catch (InterruptedException e) {
                Logger.log(minerName + " was interrupted. Stopping mining operations.");
                running.set(false);
            } catch (Exception e) {
                Logger.log(minerName + " encountered an error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean isBlockchainUpdated() {
        return candidateBlock == null || !Blockchain.getLatestBlockId().equals(candidateBlock.getPreviousBlockId());
    }

    private synchronized void updateCandidateBlock() {
        List<Transaction> txPool = TransactionPool.getMemPool();
        String previousBlockId = Blockchain.getLatestBlockId();

        if (txPool.size() >= BLOCK_TXS_COUNT) {
            List<Transaction> newTxs = TransactionPool.shufflePool(BLOCK_TXS_COUNT);
            candidateBlock = new Block(newTxs, previousBlockId, this);
            Logger.log(minerName + " Updated CandidateBlock: " + candidateBlock.getBlockId());
        } else if (txPool.isEmpty()) {
            Logger.log(minerName + ": Transaction Pool is Empty. Waiting...");
            candidateBlock = null;
        } else {
            if (mineUncompleteCandidateBlock()) {
                candidateBlock = new Block(txPool.subList(0, Math.min(txPool.size(), BLOCK_TXS_COUNT)), previousBlockId, this);
                Logger.log("Mine Incomplete Block (block txs count: " + candidateBlock.getTransactions().size() + ")!");
            } else {
                Logger.log(minerName + ": " + txPool.size() + " txs in Transactions Pool (not enough for block)");
                candidateBlock = null;
            }
        }
    }

    private synchronized void mineBlock(Block block) {
        Logger.log(minerName + " Attempting to mine Block: " + block.getBlockId());
        synchronized (Blockchain.class) {
            if (isBlockchainUpdated()) {
                Logger.log(minerName + ": Blockchain updated, abandoning current block.");
                return;
            }

            List<Transaction> validTransactions = new ArrayList<>();
            for (Transaction tx : block.getTransactions()) {
                if (TransactionUtils.verifyTransaction(tx)) {
                    validTransactions.add(tx);
                } else {
                    Logger.log(minerName + " Block's Transaction not valid: " + tx.getSignature());
                }
            }

            if (!validTransactions.isEmpty()) {
                Blockchain.addToBlockchain(block);
                Logger.log(minerName + " Successfully mined Block: " + block);
                candidateBlock = null;  // Reset candidate block after successful mining
            } else {
                Logger.log(minerName + " No valid transactions in block.");
            }
        }
    }

    private boolean solveMiningPuzzle() {
        // This is a simplified puzzle-solving mechanism.
        // In a real implementation, this would involve calculating a hash that meets certain criteria.
        return random.nextInt(DIFFICULTY) == 0;
    }

    public void stopMiner() {
        running.set(false);
    }

    private boolean isTransactionPoolEmptyForTooLong() {
        return TransactionPool.getMemPool().isEmpty() && 
               (System.currentTimeMillis() - lastBlockCreationTime) > TIMEOUT;
    }

    private boolean mineUncompleteCandidateBlock() {
        return !TransactionPool.getMemPool().isEmpty() && 
                TransactionPool.getMemPool().size() < BLOCK_TXS_COUNT &&
                (System.currentTimeMillis() - lastBlockCreationTime) > TIMEOUT - 2000;
    }

    public static void test2() {
        // Wallets in the Network
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();
        Wallet w3 = new Wallet();

        // Genisis UTXOs for each Wallet
        UTXO.genesisUtxo(w1, 50);
        UTXO.genesisUtxo(w1, 30);
        UTXO.genesisUtxo(w2, 10);
        UTXO.genesisUtxo(w2, 100);
        UTXO.genesisUtxo(w3, 90);
        UTXO.genesisUtxo(w3, 10);

        // Print each Wallet Balance
        System.out.println("w1 start balance: " + w1.getBalance());
        System.out.println("w2 start balance: " + w2.getBalance());
        System.out.println("w3 start balance: " + w3.getBalance());

        // Create multiple transaction threads for w1 (WALLET1)
        TransactionThread t0 = new TransactionThread(w1, w2.getPublicKey(), 20);
        TransactionThread t1 = new TransactionThread(w1, w2.getPublicKey(), 5);
        TransactionThread t2 = new TransactionThread(w1, w3.getPublicKey(), 5);

        // for w2
        TransactionThread t3 = new TransactionThread(w2, w1.getPublicKey(), 10);
        TransactionThread t4 = new TransactionThread(w2, w3.getPublicKey(), 5);

        // for w3 (WALLET2)
        TransactionThread t5 = new TransactionThread(w3, w1.getPublicKey(), 50);

        // Start threads
        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // Create and start multiple miners
        BlockMiner miner1 = new BlockMiner("BlockMiner1");
        BlockMiner miner2 = new BlockMiner("BlockMiner2");
        BlockMiner miner3 = new BlockMiner("BlockMiner3");
        BlockMiner miner4 = new BlockMiner("BlockMiner4");
        BlockMiner miner5 = new BlockMiner("BlockMiner5");
        
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

        System.out.println("W1 BALANCE: " + w1.getBalance());
        System.out.println("W2 BALANCE: " + w2.getBalance());
        System.out.println("W2 BALANCE: " + w3.getBalance());

        List<Transaction> transactions = TransactionPool.getMemPool();
        System.out.println("MemPool Size: " + transactions.size());
        for (Transaction tx : transactions) {
            System.out.println("TX : " + tx.getSignature());
        }
    }

    public static void main(String[] args) {
        test2();
    }
}
