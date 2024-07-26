package com.example;

public class TransactionInput {
    private String prevTxId;
    private int index;
    private String pubKey;

    public TransactionInput(String prevTxString, int index) {
        this.prevTxId = prevTxString;
        this.index = index;
    }


    public String toString() {
        return "TransactionInput {" + 
        " prevTxId: " + prevTxId + "/" + 
        " outputIndex: " + index + "/" + 
        " pubKey: " + pubKey +
        ")";
    }

}
