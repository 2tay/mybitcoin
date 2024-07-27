package com.example;

import java.security.PublicKey;


public class Main {
    public static void main(String[] args) {
        Wallet senderWallet = new Wallet();
        Wallet recipientWallet = new Wallet();
        PublicKey recipientPublicKey = recipientWallet.getPublicKey();
        String data = "helloHakimToutay";

        // Encrypt data with recipient's public key
        String encryptedData = senderWallet.encryptDataWithPubKey(data, recipientPublicKey);
        // Decrypt data with recipient's private key
        String decryptedData = recipientWallet.decryptDataWithPrivateKey(encryptedData);

        System.out.println("Encrypted Data: " + encryptedData);
        System.out.println("Decrypted Data: " + decryptedData);

        // Sign data with sender's private key
        String signedData = senderWallet.signData(data);
        // Verify signature with sender's public key
        boolean isVerified = recipientWallet.verifySignature(data, signedData, senderWallet.getPublicKey());

        System.out.println("Signed TxData: " + signedData);
        System.out.println("Signature Verified: " + isVerified);
    }
}
