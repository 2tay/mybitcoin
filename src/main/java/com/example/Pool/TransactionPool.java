package com.example.Pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.example.Transaction.Transaction;
import com.example.utils.TransactionUtils;

public class TransactionPool {
    private static final Logger logger = Logger.getLogger(TransactionPool.class.getName());
    private static final List<Transaction> mempool = new ArrayList<>();

    public static synchronized boolean addToPool(Transaction tx) {
        boolean isValid = TransactionUtils.verifyTransaction(tx);
        if (isValid) {
            mempool.add(tx);
        }
        return isValid;
    }

    public static synchronized List<Transaction> shufflePool(int count) {
        if (mempool.size() < count) {
            logger.warning("Error: mempool size (" + mempool.size() + ") is less than requested count (" + count + ")");
            return null;
        }
        List<Transaction> txs = new ArrayList<>(mempool);
        Collections.shuffle(txs);
        return txs.subList(0, count);
    }

    public static synchronized List<Transaction> getMemPool() {
        return new ArrayList<>(mempool); // Return a copy to avoid concurrent modification issues
    }

    public static synchronized boolean removeTransaction(Transaction tx) {
        return mempool.remove(tx);
    }


    public static void main(String[] args) {
        
    }
}
