package com.example.Wallet;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Request;
import com.example.Networking.Client.RequestHelper;
import com.example.Networking.Nodes.Node;
import com.example.Transaction.Transaction;
import com.example.Transaction.TransactionOutput;
import com.example.Transaction.UTXO;

import java.security.PublicKey;
import java.util.List;

public class TransactionThread extends Thread {
    private Wallet senderWallet;
    private PublicKey recipientKey;
    private int amount;
    private boolean running = true;
    private Block latestBlock;
    private Transaction transactionCreated;

    public TransactionThread(Wallet senderWallet, PublicKey recipientKey, int amount) {
        this.senderWallet = senderWallet;
        this.recipientKey = recipientKey;
        this.amount = amount;
        latestBlock = Blockchain.getLatestBlock();
    }

    public Transaction getTransaction() {
        return transactionCreated;
    }

    @Override
    public void run() {
        while(running) {
            try {
                // check if there is new blocks in blockchain
                if(!checkLatestBlock()) {
                    latestBlock = Blockchain.getLatestBlock();
                    updateWalletPool();
                }

                // Create transaction
                Transaction transaction = senderWallet.processTransaction(recipientKey, amount);
                if(transaction != null) {
                    System.out.println("Transaction created by " + Thread.currentThread().getName() + ": " + transaction.getSignature());
                    transactionCreated = transaction;
                    // Broadcast Transaction to Network
                    Request req = RequestHelper.postTx(transaction);
                    Node.sendReqToAllNetwork(req);
                    break;
                } else if(senderWallet.getWalletRunning()) {
                    // if(transaction == null && walletRunning == true) 
                    // if wallet is already working on transaction (wallet is sequentiel in creating tx)
                    System.out.println(Thread.currentThread().getName() + ": Wallet now is creating another transaction! wait a second");
                    Thread.sleep(1000);
                } else {
                    // if(transaction == null && walletRunning == false)
                    // if utxos on hold wait 2 second for miner to mine and return exchange money
                    System.out.println(Thread.currentThread().getName() + ": All wallet utxos on hold or Empty! wait 2second" );
                    Thread.sleep(2000);
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkLatestBlock() {
        return Blockchain.getLatestBlock().equals(latestBlock);
    }

    public boolean updateWalletPool() {
        // new blocks mined
        List<Block> newBlocks = Blockchain.getNewBlocks(latestBlock);
        if(newBlocks != null) 
        {
            for(Block block : newBlocks) 
            {
                for(Transaction tx : block.getTransactions()) {
                    if(senderWallet.getTxPool().getPoolTransactions().contains(tx)) {
                        // add mined transaction output's utxos
                        //senderWallet.getUtxoPool().addOutputUtxos(tx); // no more need check next for loop
                        // remove transaction from wallet proof
                        senderWallet.getTxPool().removeFromPool(tx);
                    }
    
                }
                // check if you have any outputs uder your publick key
                for(Transaction tx : block.getTransactions()) {
                    for(TransactionOutput output : tx.getOutputs()) {
                        if(output.getpubKey().equals(senderWallet.getPublicKey())) {
                            // you recieve money from someone
                            senderWallet.getUtxoPool().addOutputUtxos(tx); // will also add your own trnasction outputs (your exchange)
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static void test1() {
        // Create wallets
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();

        // give some initial utxo
        UTXO.genesisUtxo(w1, 50);
        UTXO.genesisUtxo(w1, 30);
        UTXO.genesisUtxo(w1, 10);
        UTXO.genesisUtxo(w2, 100);
        UTXO.genesisUtxo(w2, 10);
        UTXO.genesisUtxo(w2, 10);

        int senderBalance = w1.getBalance();
        int recipientBalance = w2.getBalance();
        System.out.println("Sender BALANCE: " + senderBalance);
        System.out.println("Recipient BALANCE: " + recipientBalance);

        // Create and start multiple transaction threads for w1 (WALLET1)
        TransactionThread t1 = new TransactionThread(w1, w2.getPublicKey(), 5);
        TransactionThread t2 = new TransactionThread(w1, w2.getPublicKey(), 5);
        TransactionThread t3 = new TransactionThread(w1, w2.getPublicKey(), 5);
        TransactionThread t4 = new TransactionThread(w1, w2.getPublicKey(), 5);
        TransactionThread t5 = new TransactionThread(w1, w2.getPublicKey(), 5);

        // create and start multiple transaction thread for w2 (WALLET2)
        TransactionThread t6 = new TransactionThread(w2, w1.getPublicKey(), 50);
        TransactionThread t7 = new TransactionThread(w2, w1.getPublicKey(), 10);
        TransactionThread t8 = new TransactionThread(w2, w1.getPublicKey(), 20);

        // w1 threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // w2 threads
        t6.start();
        t7.start();
        t8.start();

    }

    public static void main(String[] args) {
        test1();
    }
}

