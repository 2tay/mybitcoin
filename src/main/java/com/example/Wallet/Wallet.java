package com.example.Wallet;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.security.KeyPair;

import com.example.Pool.WalletUTXOPool;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.utils.KeyUtils;
import com.example.utils.TransactionUtils;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private WalletUTXOPool utxosPool;
    public boolean creatingRunning = false;

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

    public int getBalance() {
        int balance = 0;
        for(UTXO utxo : getAllUTXOs()) {
            balance += utxo.getValue();
        }
        return balance;
    }

    // to add genisis utxo (hard coding for now)
    public void addUtxoToPool(UTXO utxo) {
        utxosPool.addUTXO(utxo);
    }

    //get all utxos from pool
    public List<UTXO> getAllUTXOs() {
        return utxosPool.getAllUTXOs();
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

    // All transaction process handled by the wallet (Create -> propagate to network)
    public Transaction processTransaction(PublicKey recipient, int amount) {
        try {
            creatingRunning = true;
            Transaction tx = TransactionUtils.createTransaction(this, recipient, amount, utxosPool.getAllUTXOs());
            TransactionUtils.propagateTransaction(tx);
            System.out.println("Transaction Created and Propagated Successfully: " + tx);
            // Only remove used UTXOs temporarily until it is verified
            // to not use the same UTXO more than once
            // UTXO change will be added to the pool when the transaction is verified
            utxosPool.removeConsumedUtxos(tx);
            System.out.println("Wallet UTXO pool updated Successfully");
            return tx;
        } catch (IllegalArgumentException e) {
            System.err.println("Wallet's no enough funds");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            creatingRunning = false;
        }
        return null;
    }

    public static void test1() {
        Wallet w1 = new Wallet();
        System.out.println("Private Key: " + w1.getPrivateKey());
        System.out.println("Public Key: " + w1.getPublicKey());
    }

    public static void test2() {
        Wallet hakimWallet = UTXO.genesisUtxo();
        Wallet recipient = new Wallet();
        int amount = 29;
        System.out.println("hakim Balance: " + hakimWallet.getBalance());
        System.out.println("recipient Balance: " + recipient.getBalance());
        System.out.println("hakim send " + amount + "$");
        hakimWallet.processTransaction(recipient.getPublicKey(), amount);
        hakimWallet.processTransaction(recipient.getPublicKey(), amount);
        hakimWallet.processTransaction(recipient.getPublicKey(), amount);
        hakimWallet.processTransaction(recipient.getPublicKey(), amount);
        System.out.println("hakim Balance: " + hakimWallet.getBalance());
        System.out.println("recipient Balance: " + recipient.getBalance());

    }

    public static void main(String[] args) {
        Wallet.test2();
    }
}
