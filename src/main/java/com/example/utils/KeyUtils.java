package com.example.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class KeyUtils {

    public static KeyPair generateKeyPair() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(256, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String crypting(String data, PrivateKey privateKey) { 
        String cryptedData = null;
        // logic to crypt data using private key
        return cryptedData;
    }

    public static String decrypting(String cryptedData, PublicKey publicKey) {
        String decryptedData = null;
        //logic to decrypt data using public key
        return decryptedData;
    }
}
