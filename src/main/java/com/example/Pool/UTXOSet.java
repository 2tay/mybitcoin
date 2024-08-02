package com.example.Pool;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.Transaction.Transaction;
import com.example.Transaction.TransactionInput;
import com.example.Transaction.TransactionOutput;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;

public class UTXOSet {
    private static Map<String, UTXO> utxoMap = new HashMap<>();

    public static Map<String, UTXO> getUtxoMap() {
        return utxoMap;
    }

    // Add a new UTXO
    public static void addUTXO(UTXO utxo) {
        String key = utxo.getTransactionId() + ":" + utxo.getIndex();
        utxoMap.put(key, utxo);
    }

    public static void addUtxosTransaction(Transaction transaction) {
        int index = 0;
        for(TransactionOutput output : transaction.getOutputs()) {
            UTXOSet.addUTXO(new UTXO(transaction.getSignature(), index, output.getValue(), output.getpubKey()));
            index++;
        }
    }

    public static void removeConsumedUtxosTransaction(Transaction transaction) {
        List<UTXO> availableUTXOs = UTXOSet.findUTXOs(transaction.getPublicKey());
        for (TransactionInput input : transaction.getInputs()) {
            UTXO spentUTXO = null;
            for (UTXO utxo : availableUTXOs) {
                if (utxo.getTransactionId().equals(input.getPrevTxId()) && utxo.getIndex() == input.getOutputIndex()) {
                    spentUTXO = utxo;
                    break;
                }
            }
            if (spentUTXO != null) {
                UTXOSet.removeUTXO(spentUTXO.getTransactionId(), spentUTXO.getIndex());
            }
        }
    }

    // Remove a spent UTXO
    public static void removeUTXO(String transactionId, int index) {
        String key = transactionId + ":" + index;
        utxoMap.remove(key);
    }

    // Find UTXOs for a given public key hash
    public static List<UTXO> findUTXOs(PublicKey pubKey) {
        List<UTXO> result = new ArrayList<>();
        for (UTXO utxo : utxoMap.values()) {
            if (utxo.getpublicKey().equals(pubKey)) {
                result.add(utxo);
            }
        }
        return result;
    }

    // Get all UTXOs
    public static List<UTXO> getAllUTXOs() {
        return new ArrayList<>(utxoMap.values());
    }

    public static void test1() {
        Wallet w1 = new Wallet();
        Wallet w2 = new Wallet();
        UTXO utxo1 = new UTXO("txId0", 0, 6, w1.getPublicKey());
        UTXO utxo2 =new UTXO("txid0", 1, 4, w1.getPublicKey());
        UTXO utxo3 = new UTXO("txid0", 3, 10, w2.getPublicKey());

        UTXOSet.addUTXO(utxo1);
        UTXOSet.addUTXO(utxo2);
        UTXOSet.addUTXO(utxo3);

        System.out.println("All Utxos:");
        for(UTXO utxo: UTXOSet.getAllUTXOs()) {
            System.out.println(utxo);
        }

        System.out.println("w2 Utxos:");
        for(UTXO utxo : UTXOSet.findUTXOs(w2.getPublicKey())) {
            System.out.println(utxo);
        }
    }

    public static void main(String[] args) {
        UTXOSet.test1();
    }
}

