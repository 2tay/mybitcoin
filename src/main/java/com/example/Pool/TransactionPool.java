package com.example.Pool;

import java.util.ArrayList;
import java.util.List;

import com.example.Transaction.Transaction;
import com.example.Wallet.Wallet;
import com.example.utils.TransactionUtils;

public class TransactionPool {
    private static List<Transaction> mempool = new ArrayList<>();

    public static boolean addToPool(Transaction tx) {
        boolean isValid = TransactionUtils.verifyTransaction(tx);
        if(isValid) {
            mempool.add(tx);
        }
        return isValid;
    }

    public static List<Transaction> getMemPool() {
        return mempool;
    }

    public static boolean removeTransaction(Transaction tx) {
        return mempool.remove(tx);
    }

    public static void removeTest() {
        Wallet reciepWallet = new Wallet();
        Transaction tx1 = new Transaction(reciepWallet.getPublicKey());
        Transaction tx2 = new Transaction(reciepWallet.getPublicKey());
        mempool.add(tx1);
        mempool.add(tx2);
        List<Transaction> pool = getMemPool();
        System.out.println("POOL SIZE: " + pool.size());
        for(Transaction tx : pool) {
            System.out.println("TX : " + tx);
        }
        System.out.println("remove tx1");
        removeTransaction(tx2);
        System.out.println("POOL SIZE: " + pool.size());
        for(Transaction tx : pool) {
            System.out.println("TX : " + tx);
        }
    }

    public static void main(String[] args) {
        removeTest();
    }
}
