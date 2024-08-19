package com.example.Networking.Nodes;

import java.security.PublicKey;

import com.example.Networking.Client.Message;
import com.example.Transaction.Transaction;
import com.example.Wallet.TransactionThread;
import com.example.Wallet.Wallet;


public class WalletNode extends Node {
    // TODO : set to protected again after test
    public Wallet wallet;

    public WalletNode(int walletServerPort) {
        super(walletServerPort);
        wallet = new Wallet();

        // Post Own Node in the Network (bootstrapNode)
        addMeToNetwork("localhost", 2000);
        // Get All Network Nodes
        peerNodes = getNetworkNodes("localhost", 2000);
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
            client.sendSerializedMessage(message, peerHost, peerPort);
            System.out.println(tx + " Broadcast Succefully to " + peerHost + ":" + peerPort);
        }
        System.out.println("transaction is NULL");
    }

    public static void main(String[] args) {
        WalletNode anode = new WalletNode(2005);
        
    }

}
