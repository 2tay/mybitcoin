package com.example.Wallet.WalletPool;

import java.util.ArrayList;
import java.util.List;

import com.example.Transaction.Transaction;

public class WalletTransactionPool {
    List<Transaction> waitingTransaction = new ArrayList<>();

    public List<Transaction> getPoolTransactions() {
        return waitingTransaction;
    }

    public boolean addToPool(Transaction tx) {
        return waitingTransaction.add(tx);
    }

    public boolean removeFromPool(Transaction tx) {
        return waitingTransaction.remove(tx);
    }

}
