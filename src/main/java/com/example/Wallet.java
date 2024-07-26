package com.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPair;
import com.example.utils.KeyUtils;

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

    private boolean sendMoney(PublicKey recipient) {
        //to do 
        return false;
    }


    public static void main(String[] args) {
        Wallet w1 = new Wallet();
        System.out.println("Private Key: " + w1.getPrivateKey());
        System.out.println("Public Key: " + w1.getPublicKey());
    }
}
