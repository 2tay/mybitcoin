package com.example.Wallet.WalletPool;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.TransactionInput;
import com.example.Transaction.TransactionOutput;
import com.example.Transaction.UTXO;

public class WalletUTXOPool {
    PublicKey publicKey;
    private Map<String, UTXO> utxoMap = new HashMap<>();

    public WalletUTXOPool(PublicKey publicKey) {
        this.publicKey = publicKey;
        initiliazeUtxosMap();
    }

    public Map<String, UTXO> getUtxoMap() {
        return utxoMap;
    }

    //helper function to initiliaze
    private void initiliazeUtxosMap() {
        for (UTXO utxo : UTXOSet.getUtxoMap().values()) {
            if (utxo.getpublicKey().equals(publicKey)) {
                String key = utxo.getTransactionId() + ":" + utxo.getIndex();
                utxoMap.put(key, utxo);
            }
        }
    }

    // Add a new UTXO
    public void addUTXO(UTXO utxo) {
        String key = utxo.getTransactionId() + ":" + utxo.getIndex();
        utxoMap.put(key, utxo);
    }

    // Remove a spent UTXO
    public void removeUTXO(String transactionId, int index) {
        String key = transactionId + ":" + index;
        utxoMap.remove(key);
    }

    // Get all UTXOs
    public List<UTXO> getAllUTXOs() {
        return new ArrayList<>(utxoMap.values());
    }

    // Add UTXOs for the outputs of a transaction
    public void addOutputUtxos(Transaction transaction) {
        int index = 0;
        for (TransactionOutput output : transaction.getOutputs()) {
            if(output.getpubKey().equals(publicKey)) {
                addUTXO(new UTXO(transaction.getSignature(), index, output.getValue(), output.getpubKey()));
            }
            index++;
        }
    }

    // Remove UTXOs for the inputs of a transaction
    public void removeConsumedUtxos(Transaction transaction) {
        for (TransactionInput input : transaction.getInputs()) {
            removeUTXO(input.getPrevTxId(), input.getOutputIndex());
        }
    }

    // SET utxoPool IN WALLET CLASS TO PUBLIC THEN TRY THIS TESTS

    /*public static void test1() {
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();
        UTXO utxo1 = new UTXO("txId0", 0, 6, w1.getPublicKey());
        UTXO utxo2 =new UTXO("txid0", 1, 4, w1.getPublicKey());
        UTXO utxo3 = new UTXO("txid0", 3, 10, w2.getPublicKey());

        w1.addUtxoToPool(utxo1);
        w1.addUtxoToPool(utxo2);
        w2.addUtxoToPool(utxo3);

        System.out.println("w1 Utxos:");
        for(UTXO utxo : w1.getAllUTXOs()) {
            System.out.println(utxo);
        } 
        System.out.println("W1 Balance: " + w1.getBalance());

        w1.utxosPool.removeUTXO(utxo1.getTransactionId(), utxo1.getIndex());
        System.out.println("removed utxo1 from w1");

        System.out.println("w1 Utxos now:");
        for(UTXO utxo : w1.getAllUTXOs()) {
            System.out.println(utxo);
        } 
        System.out.println("w1 balance: " + w1.getBalance());
    }*/

    /*public static void test2() {
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();
        UTXO utxo1 = new UTXO("txId0", 0, 40, w1.getPublicKey());
        UTXO utxo2 = new UTXO("txId0", 1, 100, w1.getPublicKey());
        w1.addUtxoToPool(utxo1);
        w1.addUtxoToPool(utxo2);
        System.out.println("w1 balance: " + w1.getBalance());
        try {
            Transaction tx = TransactionUtils.createTransaction(w1, w2.getPublicKey(), 20, w1.getAllUTXOs());
            System.out.println("Transaction: " + tx);
            w1.utxosPool.removeConsumedUtxos(tx);
            w1.utxosPool.addOutputUtxos(tx);
            System.out.println("w1 balance: " + w1.getBalance());
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }*/

    // *************************************** ************************************ //

    public static void main(String[] args) {
        //test2();
    }
}