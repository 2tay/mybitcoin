package com.example;

import java.security.PublicKey;
import java.util.UUID;

public class TransactionOutput {
    private String txOutputId;
    private int value;
    private PublicKey pubKey;

    public TransactionOutput(int value, PublicKey pubKey) {
        this.txOutputId = UUID.randomUUID().toString();
        this.value = value;
        this.pubKey = pubKey;
    }

    public String getTxOutputId() {
        return txOutputId;
    }

    public int getValue() {
        return value;
    }

    public PublicKey getpubKey() {
        return pubKey;
    }

    public String toString() {
        return "TransactionOutput{" +
                "value=" + value +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }
}
