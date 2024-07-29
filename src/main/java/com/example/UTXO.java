package com.example;

import java.security.PublicKey;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UTXO {
    private String utxoId;
    private String transactionId;
    private int index;
    private int value;
    private PublicKey publicKey;

    public UTXO(String transactionId, int index, int value, PublicKey publicKey) {
        this.utxoId = UUID.randomUUID().toString();
        this.transactionId = transactionId;
        this.index = index;
        this.value = value;
        this.publicKey = publicKey;
    }

    public static Wallet genesisUtxo() {
        Wallet h2tayWallet = new Wallet();
        UTXO firstUtxo = new UTXO("txid0", 0, 200, h2tayWallet.getPublicKey());
        UTXOSet.addUTXO(firstUtxo);
        return h2tayWallet;
    }

    public String getUtxoId() {
        return utxoId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    public PublicKey getpublicKey() {
        return publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UTXO utxo = (UTXO) o;
        return index == utxo.index && Objects.equals(transactionId, utxo.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, index);
    }

    @Override
    public String toString() {
        return "UTXO{" +
                "transactionId='" + transactionId + '\'' +
                ", index=" + index +
                ", value=" + value +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }

    public static void test1() {
        Wallet w1 = new Wallet();
        UTXO utxo1 = new UTXO("txid0", 0, 6, w1.getPublicKey());
        System.out.println(utxo1);
    }

    // test genisis utxo 
    public static void test2() {
        UTXO.genesisUtxo();
        List<UTXO> utxos = UTXOSet.getAllUTXOs();
        System.out.println("Genisis UTXO:");
        for(UTXO utxo: utxos) {
            System.out.println(utxo);
        }
    }

    public static void main(String[] args) {
        UTXO.test1();
        UTXO.test2();
    }
}
