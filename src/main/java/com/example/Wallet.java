package com.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import com.example.utils.KeyUtils;
import com.example.utils.TransactionUtils;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        KeyPair keyPair = KeyUtils.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    private PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String encryptDataWithPubKey(String data, PublicKey recipientPublicKey) {
        return KeyUtils.encryptWithPubKey(data, recipientPublicKey);
    }

    public String decryptDataWithPrivateKey(String encryptedData) {
        return KeyUtils.decryptWithPrivateKey(encryptedData, privateKey);
    }

    public String signData(String data) {
        return TransactionUtils.signData(data, privateKey);
    }

    public static void test1() {
        Wallet w1 = new Wallet();
        System.out.println("Private Key: " + w1.getPrivateKey());
        System.out.println("Public Key: " + w1.getPublicKey());
    }

    public static void main(String[] args) {
        Wallet.test1();
    }
}
