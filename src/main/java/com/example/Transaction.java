package com.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.example.utils.KeyUtils;

public class Transaction {
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;
    private String txData;
    private String signature;

    public Transaction() {
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public void addInput(TransactionInput input) {
        inputs.add(input);
    }

    public void addOutput(TransactionOutput output) {
        outputs.add(output);
    }

    public void setTxData() {
        // serialize transaction data 
    }

    /*public String setTxSignature(PrivateKey privateKey) {
        signature = KeyUtils.encrypt(txData, privateKey);
        return signature;
    }*/

    /*public boolean verifyTransaction(PublicKey publicKey) {
        return KeyUtils.decrypt(txData, publicKey) == txData;
    }*/

    public String toString() {
        return "Transaction{" +
                "inputs=" + inputs +
                ", outputs=" + outputs +
                ", signature=" + signature +
                '}';
    }

}
