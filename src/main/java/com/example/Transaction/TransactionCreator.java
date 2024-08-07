package com.example.Transaction;

import com.example.Pool.TransactionPool;
import com.example.Wallet.Wallet;
import com.example.utils.TransactionUtils;

import java.util.Random;

public class TransactionCreator extends Thread {
    private Wallet senderWallet;
    private Wallet recipientWallet;
    private int amount;
    private Random random = new Random();

    public TransactionCreator(Wallet senderWallet, Wallet recipientWallet, int amount) {
        this.senderWallet = senderWallet;
        this.recipientWallet = recipientWallet;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            // Random delay to simulate transaction creation time
            Thread.sleep(random.nextInt(1000));

            // Create transaction
            Transaction transaction = TransactionUtils.createTransaction(senderWallet, recipientWallet.getPublicKey(), amount);
            System.out.println("Transaction created by " + Thread.currentThread().getName() + ": " + transaction);

            // Propagate transaction to the network
            boolean isValid = TransactionUtils.propagateTransaction(transaction);
            System.out.println("Transaction added to TransactionPOOL by " + Thread.currentThread().getName() + ": " + isValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test1() {
        // Create wallets
        Wallet senderWallet = new Wallet();
        UTXO.genesisUtxo(senderWallet);
        Wallet recipientWallet = new Wallet();

        // Create and start multiple TransactionCreator threads
        int numberOfThreads = 5; // You can change the number of threads to simulate more transactions
        TransactionCreator[] creators = new TransactionCreator[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            creators[i] = new TransactionCreator(senderWallet, recipientWallet, 10); // Example amount
            creators[i].setName("TransactionCreator-" + (i + 1));
            creators[i].start();
        }

        // Wait for all threads to finish
        for (TransactionCreator creator : creators) {
            try {
                creator.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Print all transactions in the mempool
        System.out.println("All transactions in mempool:");
        TransactionPool.getMemPool().forEach(tx -> System.out.println(tx));
    }

    public static void main(String[] args) {
        test1();
    }
}

