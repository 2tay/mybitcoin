package com.example.Transaction;

import com.example.Wallet.Wallet;

import java.security.PublicKey;
import java.util.Random;

public class TransactionThread extends Thread {
    private Wallet senderWallet;
    private PublicKey recipientKey;
    private int amount;

    public TransactionThread(Wallet senderWallet, PublicKey recipientKey, int amount) {
        this.senderWallet = senderWallet;
        this.recipientKey = recipientKey;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            // Random delay to simulate transaction creation time
            Thread.sleep(new Random().nextInt(1000));

            // Create transaction
            Transaction transaction = senderWallet.processTransaction(recipientKey, amount);
            System.out.println("Transaction created by " + Thread.currentThread().getName() + ": " + transaction);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test1() {
        // Create wallets
        Wallet senderWallet = new Wallet();
        Wallet recipientWallet = new Wallet();

    }

    public static void main(String[] args) {
        test1();
    }
}

