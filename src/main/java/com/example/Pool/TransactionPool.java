package com.example.Pool;

import java.util.ArrayList;
import java.util.List;

import com.example.Transaction.Transaction;
import com.example.utils.TransactionUtils;

public class TransactionPool {
    private static List<Transaction> mempool = new ArrayList<>();

    public static boolean AddToPool(Transaction tx) {
        boolean isValid = TransactionUtils.verifyTransaction(tx);
        if(isValid) {
            // Add the new Utxos to UtxoSet
            //UTXOSet.addUtxosTransaction(tx);

            // Update UTXOs
            //UTXOSet.removeConsumedUtxosTransaction(tx);

            // Add transaction to transactionPool
            mempool.add(tx);
        }
        return isValid;
    }

    public static List<Transaction> getMemPool() {
        return mempool;
    }
}
