package com.example;

import com.example.Wallet.Wallet;

public class WalletKeysTest2 {
    public static void main(String[] args) {
        Wallet w1 = new Wallet("w1Keys");
        System.out.println("W1 Public Key: " + w1.getPublicKey());
        
        Wallet w2 = new Wallet();
        System.out.println("W2 Public key: " + w2.getPublicKey());
    }
}
