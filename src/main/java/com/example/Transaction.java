package com.example;

import java.util.ArrayList;
import java.util.List;


public class Transaction {
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;
    private String signature;

    public Transaction() {
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String txSignature) {
        signature = txSignature;
    }

    public void addInput(TransactionInput input) {
        inputs.add(input);
    }

    public void addOutput(TransactionOutput output) {
        outputs.add(output);
    }

    public String toString() {
        return "Transaction{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                ", signature=" + signature +
                '}';
    }

    public static void test1() {
        //test to do
    }

    public static void main(String[] args) {
        
    }

}
