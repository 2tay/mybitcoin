package com.example.utils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;
import com.example.Transaction.TransactionInput;
import com.example.Transaction.TransactionOutput;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;

public class TransactionUtils {

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
            Security.addProvider(new BouncyCastleProvider());
            Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            byte[] signedBytes = Base64.getDecoder().decode(signedData);
            return signature.verify(signedBytes);
        } catch (Exception e) {
            System.err.println("Failed in verify signature");
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyTransaction(Transaction transaction) {
        //verify signature
        boolean validSignature = verifySignature(transaction.toString(), transaction.getSignature(), transaction.getPublicKey());
        if(!validSignature) {
            System.out.println("Invalid signature");
            return false;
        }

        //verify inputs validity
        for(TransactionInput input : transaction.getInputs()) {
            if(!verifyInput(transaction.getPublicKey(), input)) {
                return false;
            }
        }
        return true;
    }

    public static boolean verifyInput(PublicKey publicKey, TransactionInput input) {
        //verfiy transaction publickey suit the input public key
        if(publicKey != input.getSenderPublicKey()) {
            System.out.println("Transaction publicKey doesnt match input");
            return false;
        }

        //check if the there is any utxos suits the input
        List<UTXO> senderUtxos = UTXOSet.findUTXOs(publicKey);
        for(UTXO utxo : senderUtxos) {
            if(utxo.getTransactionId().equals(input.getPrevTxId()) && utxo.getIndex() == input.getOutputIndex()) {
                System.out.println("utxo input exist");
                return true;
            }
        }
        //if none fo the utxos suit the input
        System.out.println("utxo input doesnt exist");
        return false;
    }

    //transaction creation
    public static Transaction createTransaction(Wallet senderWallet, PublicKey recipient, int amount) throws Exception {
        List<UTXO> availableUTXOs = senderWallet.getUtxoPool().getAllUTXOs();
        List<TransactionInput> inputs = new ArrayList<>();
        int totalValue = 0;

        // Select UTXOs to cover the amount
        for (UTXO utxo : availableUTXOs) {
            inputs.add(new TransactionInput(utxo.getTransactionId(), utxo.getIndex(), senderWallet.getPublicKey()));
            totalValue += utxo.getValue();
            if (totalValue >= amount) {
                break;
            }
        }

        // Check if totalValue covers the amount
        if (totalValue < amount) {
            return null;
        }

        // Create transaction
        Transaction transaction = new Transaction(senderWallet.getPublicKey());
        for (TransactionInput input : inputs) {
            transaction.addInput(input);
        }

        // Create outputs
        transaction.addOutput(new TransactionOutput(amount, recipient));

        // Calculate and add change
        if (totalValue > amount) {
            transaction.addOutput(new TransactionOutput(totalValue - amount, senderWallet.getPublicKey()));
        }

        // Sign the transaction
        String transactionData = transaction.toString();
        String signature = senderWallet.signData(transactionData);
        transaction.setSignature(signature);

        // Remove inputs Utxos from WalletUtxosPOOL
        senderWallet.getUtxoPool().removeConsumedUtxos(transaction);

        return transaction;
    }



    //propagate transaction to network
    public static boolean propagateTransaction(Transaction transaction) {
        return TransactionPool.addToPool(transaction);
    }

    // --------------------->   TESTING FUNCTIONS   <----------------------------

    //tets1: test sign data and verfiy signature test function 
    public static void test1() {
        Wallet myWallet = new Wallet();
        String data = "hakim2tay";
        String signedData = myWallet.signData(data);
        System.err.println(signedData);
        boolean isValid = verifySignature(data, signedData, myWallet.getPublicKey());
        System.out.println("is valid: " + isValid);
    }

    //test2: test createTransaction function
    public static void test2() {
        Wallet h2tayWallet = new Wallet();
        UTXO.genesisUtxo(h2tayWallet, 200);
        Wallet recipient = new Wallet();
        try {
            Transaction t1 = createTransaction(h2tayWallet, recipient.getPublicKey(), 100);
            System.out.println("Transaction created: " + t1);
            boolean isValid = verifySignature(t1.toString(), t1.getSignature(), t1.getPublicKey());
            System.out.println("Transaction IsValid: " + isValid);
            List<UTXO> allutxos = UTXOSet.getAllUTXOs();
            System.out.println("All UTXOs");
            for(UTXO utxo : allutxos) {
                System.out.println(utxo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //test3: test propagate to network
    public static void test3() {
        Wallet h2tayWallet = new Wallet();
        UTXO.genesisUtxo(h2tayWallet, 200);
        Wallet recipient = new Wallet();
        try {
            Transaction t1 = createTransaction(h2tayWallet, recipient.getPublicKey(), 100);
            System.out.println("Transaction created: " + t1);
            boolean isValid = propagateTransaction(t1);
            System.out.println("Added to network validity: " + isValid);
            List<Transaction> mempool = TransactionPool.getMemPool();
            for(Transaction tx: mempool) {
                System.out.println("Transaction in mempool: " + tx.getPublicKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //test4: test verifyTransaction
    public static void test4() {
        Wallet h2tayWallet = new Wallet();
        UTXO.genesisUtxo(h2tayWallet, 200);
        Wallet recipient = new Wallet();
            Transaction t1;
            try {
                t1 = createTransaction(h2tayWallet, recipient.getPublicKey(), 100);
                System.out.println("Transaction created: " + t1);
                boolean isValid = verifyTransaction(t1);
                System.err.println("transaction isValid: " + isValid);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    
    public static void main(String[] args) {
        test1();
    }
}
