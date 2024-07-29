package com.example;

import java.security.PublicKey;
import java.util.UUID;

public class TransactionInput {
    private String txInputId;
    private String prevTxId;
    private int index;
    private PublicKey pubKey;

    public TransactionInput(String prevTxString, int index, PublicKey senderPublicKey) {
        this.txInputId = UUID.randomUUID().toString();
        this.prevTxId = prevTxString;
        this.index = index;
        this.pubKey = senderPublicKey;
    }

    public String getTXInputId() {
        return txInputId;
    }

    public String getPrevTxId() {
        return prevTxId;
    }

    public int getOutputIndex() {
        return index;
    }

    public PublicKey getSenderPublicKey() {
        return pubKey;
    }


    public String toString() {
        return "TransactionInput {" + 
        " prevTxId: " + prevTxId + "/" + 
        " outputIndex: " + index + "/" + 
        " pubKey: " + pubKey +
        ")";
    }

}
