package com.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class WalletHelper {

    public static void saveKeyToFile(String fileName, byte[] key) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey readPrivateKeyFromFile(String fileName) throws Exception {
        byte[] keyBytes = readFileToByteArray(fileName);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey readPublicKeyFromFile(String fileName) throws Exception {
        byte[] keyBytes = readFileToByteArray(fileName);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static byte[] readFileToByteArray(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            return fis.readAllBytes();
        }
    }

    public static boolean doesFilesExist(String privateKeyFileName, String publicKeyFileName) {
        return new File(privateKeyFileName).exists() && new File(publicKeyFileName).exists();
    }
}
