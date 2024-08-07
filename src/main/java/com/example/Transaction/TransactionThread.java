package com.example.Transaction;

import com.example.Wallet.Wallet;

import java.security.PublicKey;
import java.util.Random;

public class TransactionThread extends Thread {
    private Wallet senderWallet;
    private PublicKey recipientKey;
    private int amount;
    private boolean running = true;

    public TransactionThread(Wallet senderWallet, PublicKey recipientKey, int amount) {
        this.senderWallet = senderWallet;
        this.recipientKey = recipientKey;
        this.amount = amount;
    }

    @Override
    public void run() {
        while(running) {
            try {
                // Random delay to simulate transaction creation time
                //Thread.sleep(new Random().nextInt(1000));
    
                // Create transaction
                Transaction transaction = senderWallet.processTransaction(recipientKey, amount);
                if(transaction != null) {
                    System.out.println("Transaction created by " + Thread.currentThread().getName() + ": " + transaction);
                    break;
                } else if(senderWallet.getWalletRunning()) {
                    // if wallet is already working on transaction (wallet is sequentiel in creating tx)
                    System.out.println("Wallet now is creating another transaction! can you try later.");
                    System.out.println(Thread.currentThread().getName() + " is waiting");
                    Thread.sleep(1000);
                } else {
                    // if utxos on hold wait 2 second for miner to mine and return exchange money
                    Thread.sleep(2000);
                }
    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test1() {
        // Create wallets
        Wallet senderWallet = new Wallet();
        Wallet recipientWallet = new Wallet();

        // give some initial utxo
        UTXO.genesisUtxo(senderWallet, 50);
        UTXO.genesisUtxo(senderWallet, 30);
        UTXO.genesisUtxo(senderWallet, 10);
        UTXO.genesisUtxo(recipientWallet, 100);
        UTXO.genesisUtxo(recipientWallet, 10);
        UTXO.genesisUtxo(recipientWallet, 10);

        int senderBalance = senderWallet.getBalance();
        int recipientBalance = recipientWallet.getBalance();
        System.out.println("Sender BALANCE: " + senderBalance);
        System.out.println("Recipient BALANCE: " + recipientBalance);

        // Create and start multiple transaction threads
        TransactionThread t1 = new TransactionThread(senderWallet, recipientWallet.getPublicKey(), 5);
        TransactionThread t2 = new TransactionThread(senderWallet, recipientWallet.getPublicKey(), 5);
        TransactionThread t3 = new TransactionThread(senderWallet, recipientWallet.getPublicKey(), 5);
        TransactionThread t4 = new TransactionThread(senderWallet, recipientWallet.getPublicKey(), 5);
        TransactionThread t5 = new TransactionThread(senderWallet, recipientWallet.getPublicKey(), 5);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    public static void main(String[] args) {
        test1();
    }
}

