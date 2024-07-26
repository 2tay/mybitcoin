package com.example;

public class TransactionOutput {
    private int value;
    private String pubKeyHash;

    public TransactionOutput(int value, String pubKeyHash) {
        this.value = value;
        this.pubKeyHash = pubKeyHash;
    }

    public int getValue() {
        return value;
    }

    public String getpubKeyHash() {
        return pubKeyHash;
    }

    public String toString() {
        return "TransactionOutput{" +
                "value=" + value +
                ", pubKeyHash='" + pubKeyHash + '\'' +
                '}';
    }
}
