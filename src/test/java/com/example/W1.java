package com.example;

import java.security.PublicKey;

import com.example.Wallet.Wallet;

public class W1 {
    public Wallet w1;

    public W1() {
        w1 = new Wallet("folderKeys");
    }

    public PublicKey getPublicKey() {
        return w1.getPublicKey();
    }

    
}
