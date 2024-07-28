package com.example.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.example.Wallet;

import java.util.Base64;

public class KeyUtils {

    public static KeyPair generateKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(256, random);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptWithPubKey(String data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptWithPrivateKey(String encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("ECIES", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String signData(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            byte[] signedBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String data, String signedData, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] signedBytes = Base64.getDecoder().decode(signedData);
            return signature.verify(signedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void test1(){
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

    public static void main(String[] args){
        test1();
    }
    
}
