package com.example.Wallet;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.security.KeyPair;

import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.WalletPool.WalletTransactionPool;
import com.example.Wallet.WalletPool.WalletUTXOPool;
import com.example.utils.KeyUtils;
import com.example.utils.TransactionUtils;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private WalletTransactionPool transactionPool = new WalletTransactionPool();
    private WalletUTXOPool utxosPool;
    private boolean walletRunning = false;

    public Wallet() {
        KeyPair keyPair = KeyUtils.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        utxosPool = new WalletUTXOPool(publicKey);
    }

    private PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public WalletTransactionPool getTxPool() {
        return transactionPool;
    }

    public WalletUTXOPool getUtxoPool() {
        return utxosPool;
    }

    public String encryptDataWithPubKey(String data, PublicKey recipientPublicKey) {
        return KeyUtils.encryptWithPubKey(data, recipientPublicKey);
    }

    public String decryptDataWithPrivateKey(String encryptedData) {
        return KeyUtils.decryptWithPrivateKey(encryptedData, privateKey);
    }

    public String signData(String data) {
        return TransactionUtils.signData(data, privateKey);
    }

    public int getBalance() {
        int balance = 0;
        for(UTXO utxo : UTXOSet.findUTXOs(publicKey)) {
            balance += utxo.getValue();
        }
        return balance;
    }

    // Post Transaction to Wallet's transaction POOL
    public boolean postToPool(Transaction tx) {
        if(tx != null) {
            return transactionPool.addToPool(tx);
        }
        return false;
    }


    public Transaction processTransaction(PublicKey recipient, int amount) {
        //sequential transaction creation (one by one for same user)
        if(walletRunning) {
            System.out.println("Wallet now is creating another transaction! can you try later.");
            return null;
        }
        //wallet is free to create transaction
        walletRunning = true;
        try {
            Transaction tx = TransactionUtils.createTransaction(this, recipient, amount);
            if(postToPool(tx)) {
                TransactionUtils.propagateTransaction(tx);
                return tx;
            } else{
                System.out.println("All wallet utxos on hold or Empty! try later.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Wallet failed to create transaction!");
            e.printStackTrace();
        } finally {
            walletRunning = false;
        }
        return null;
    }


    public static void test1() {
        Wallet w1 = new Wallet();
        System.out.println("Private Key: " + w1.getPrivateKey());
        System.out.println("Public Key: " + w1.getPublicKey());
    }

    public static void test2() {
        Wallet mine = new Wallet();
        Wallet recipient = new Wallet();
        UTXO genisisUtxo = new UTXO("txid0", 0, 200, mine.getPublicKey());
        UTXO genisisUtxo2 = new UTXO("txid0", 1, 500, mine.getPublicKey());
        UTXOSet.addUTXO(genisisUtxo);
        mine.utxosPool.addUTXO(genisisUtxo);
        UTXOSet.addUTXO(genisisUtxo2);
        mine.utxosPool.addUTXO(genisisUtxo2);
        List<UTXO> utxos = mine.utxosPool.getAllUTXOs();
        for(UTXO utxo : utxos) {
            System.out.println("UTXO : "+ utxo);
        }
        Transaction tx1 = mine.processTransaction(recipient.getPublicKey(), 200);
        System.out.println("TX1: " + tx1);
        Transaction tx2 = mine.processTransaction(recipient.getPublicKey(), 10);
        System.out.println("TX2: " + tx2);
        Transaction tx3 = mine.processTransaction(recipient.getPublicKey(), 500);
        System.out.println("TX3: " + tx3);
    }

    public static void main(String[] args) {
        test2();
    }
}
