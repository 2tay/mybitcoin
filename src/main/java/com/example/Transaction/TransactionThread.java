package com.example.Transaction;

import com.example.Wallet.Wallet;

import java.security.PublicKey;

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
                // Create transaction
                Transaction transaction = senderWallet.processTransaction(recipientKey, amount);
                if(transaction != null) {
                    System.out.println("Transaction created by " + Thread.currentThread().getName() + ": " + transaction.getSignature());
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
