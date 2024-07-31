package com.example.Transaction;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


public class Transaction {
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;
    private PublicKey publicKey;
    private String signature;

    public Transaction(PublicKey publicKey) {
        this.publicKey = publicKey;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
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
                '}';
    }

    public static void test1() {
        //test to do
    }

    public static void main(String[] args) {
        
    }

}
