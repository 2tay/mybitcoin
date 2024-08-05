package com.example.Transaction;

import com.example.Wallet.Wallet;


public class TransactionCreator extends Thread {
    private Wallet senderWallet;
    private Wallet recipientWallet;
    private int amount;

    public TransactionCreator(Wallet senderWallet, Wallet recipientWallet, int amount) {
        this.senderWallet = senderWallet;
        this.recipientWallet = recipientWallet;
        this.amount = amount;
    }

    @Override
    public void run() {
        while(senderWallet.creatingRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Transaction tx = senderWallet.processTransaction(recipientWallet.getPublicKey(), amount);
        System.out.println("Transaction creator: " + Thread.currentThread().getName() + " tx: " + tx);
    }

    public static void test1() {
        //todo
    }

    public static void main(String[] args) {
        test1();
    }
}

