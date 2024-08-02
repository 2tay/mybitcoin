package com.example.Pool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.Transaction.Transaction;
import com.example.utils.TransactionUtils;

public class TransactionPool {
    private static List<Transaction> mempool = new ArrayList<>();

    public static boolean AddToPool(Transaction tx) {
        boolean isValid = TransactionUtils.verifyTransaction(tx);
        if(isValid) {
            // Add transaction to transactionPool
            mempool.add(tx);
        }
        return isValid;
    }

    public static List<Transaction> getMemPool() {
        return mempool;
    }

    public static boolean removeTransaction(Transaction tx) {
        Iterator<Transaction> iterator = mempool.iterator();
        while (iterator.hasNext()) {
            Transaction currentTx = iterator.next();
            if (currentTx.equals(tx)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
