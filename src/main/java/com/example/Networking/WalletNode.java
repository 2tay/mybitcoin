package com.example.Networking;

import java.security.PublicKey;

import com.example.Networking.Client.Message;
import com.example.Transaction.Transaction;
import com.example.Wallet.TransactionThread;
import com.example.Wallet.Wallet;

public class WalletNode extends Node {
    private Wallet wallet;

    public WalletNode(int walletServerPort) {
        super(walletServerPort);
        wallet = new Wallet();
    }

    public Transaction processTransaction(PublicKey recipientPublicKey, int amount) {
        TransactionThread transactionCreator = new TransactionThread(wallet, recipientPublicKey, amount);
        transactionCreator.start();
        try {
            transactionCreator.join();
        } catch (InterruptedException e) {
            System.err.println("Failed transactionCreator join method interrupted");
            e.printStackTrace();
        }

        return transactionCreator.getTransaction();
    }

    public void BroadcastTxToPeer(Transaction tx, Message message, String peerHost, int peerPort) {
        if(tx != null) {
            System.out.println("Transaction created succefully: " + tx);
            sendMessageToPeer(message, peerHost, peerPort);
            System.out.println(tx + " Broadcast Succefully to " + peerHost + ":" + peerPort);
        }
        System.out.println("transaction is NULL");
    }
}
