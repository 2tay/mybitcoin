package com.example;

import java.security.PublicKey;

import com.example.Wallet.Wallet;

public class W2 {
    public Wallet w2;

    public W2() {
        w2 = new Wallet("folderKeys");
    }

    public static void main(String[] args) {
        W1 w1 = new W1();
        PublicKey w1pubkey = w1.getPublicKey();
        System.out.println(w1pubkey);
        String encrypted = w1.w1.encryptDataWithPubKey("hakim toutay", w1pubkey);

        W2 w2 = new W2();
        PublicKey w2pubkey = w2.w2.getPublicKey();
        System.out.println(w2pubkey);
        String data = w2.w2.decryptDataWithPrivateKey(encrypted);

        System.out.println(data);
        
    }
}
