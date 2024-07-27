package com.example;

public class TransactionOutput {
    private int value;
    private String pubKey;

    public TransactionOutput(int value, String pubKey) {
        this.value = value;
        this.pubKey = pubKey;
    }

    public int getValue() {
        return value;
    }

    public String getpubKey() {
        return pubKey;
    }

    public String toString() {
        return "TransactionOutput{" +
                "value=" + value +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }
}
