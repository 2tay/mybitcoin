package com.example.Wallet;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.io.File;
import java.security.KeyPair;

import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.WalletPool.WalletTransactionPool;
import com.example.Wallet.WalletPool.WalletUTXOPool;
import com.example.utils.KeyUtils;
import com.example.utils.TransactionUtils;
import com.example.utils.WalletHelper;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private WalletTransactionPool transactionPool = new WalletTransactionPool();
    private WalletUTXOPool utxosPool;
    private boolean walletRunning = false;

    // Default constructor
    public Wallet() {
        this("default");  // Use a default folder name
    }

    // Constructor with folder name
    public Wallet(String folderName) {
        ensureFolderExists(folderName);

        // Define key file paths
        String privateKeyFile = folderName + File.separator + "privateKey.key";
        String publicKeyFile = folderName + File.separator + "publicKey.key";

        initWallet(privateKeyFile, publicKeyFile);
        
        // Initialize Wallet UTXO Pool
        this.utxosPool = new WalletUTXOPool(publicKey);
    }

    // Ensure the folder exists
    private void ensureFolderExists(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Failed to create directory: " + folderName);
        }
    }

    // Initialize wallet with the given key file paths
    public void initWallet(String privateKeyFile, String publicKeyFile) {
        if (!WalletHelper.doesFilesExist(privateKeyFile, publicKeyFile)) {
            generateAndSaveKeys(privateKeyFile, publicKeyFile);
        } else {
            loadKeysFromFile(privateKeyFile, publicKeyFile);
        }
    }

    // Generate new key pair and save to files
    private void generateAndSaveKeys(String privateKeyFile, String publicKeyFile) {
        KeyPair keyPair = KeyUtils.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();

        // Save the keys to files
        WalletHelper.saveKeyToFile(privateKeyFile, privateKey.getEncoded());
        WalletHelper.saveKeyToFile(publicKeyFile, publicKey.getEncoded());
    }

    // Load keys from files
    private void loadKeysFromFile(String privateKeyFile, String publicKeyFile) {
        try {
            this.privateKey = WalletHelper.readPrivateKeyFromFile(privateKeyFile);
            this.publicKey = WalletHelper.readPublicKeyFromFile(publicKeyFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize wallet from files.", e);
        }
    }


    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public WalletTransactionPool getTxPool() {
        return this.transactionPool;
    }

    public WalletUTXOPool getUtxoPool() {
        return this.utxosPool;
    }

    public boolean getWalletRunning() {
        return this.walletRunning;
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

    public int getBalance() {
        int balance = 0;
        for(UTXO utxo : UTXOSet.findUTXOs(publicKey)) {
            balance += utxo.getValue();
        }
        return balance;
    }

    // Post Transaction to Wallet's transaction POOL
    public boolean postToPool(Transaction tx) {
        if(tx != null) {
            return transactionPool.addToPool(tx);
        }
        return false;
    }


    public Transaction processTransaction(PublicKey recipient, int amount) {
        //sequential transaction creation (one by one for same user)
        if(walletRunning) {
            //System.out.println("Wallet now is creating another transaction! can you try later.");
            return null;
        }
        //wallet is free to create transaction
        walletRunning = true;
        try {
            Transaction tx = TransactionUtils.createTransaction(this, recipient, amount);
            if(postToPool(tx)) {
                TransactionUtils.propagateTransaction(tx);
                return tx;
            } else{
                //System.out.println("All wallet utxos on hold or Empty! try later.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Wallet failed to create transaction!");
            e.printStackTrace();
        } finally {
            walletRunning = false;
        }
        return null;
    }


}
