package com.example.Networking_Testing;

import com.example.Networking.WalletNode;
import com.example.Networking.Client.Message;
import com.example.Networking.Client.MessageHelper;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;

public class TestWalletNode {
    public static void main(String[] args) {
        WalletNode walletNode = new WalletNode(2000);
        Wallet recipient = new Wallet();

        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);

        Transaction tx0 = walletNode.processTransaction(recipient.getPublicKey(), 100);
        Message message = MessageHelper.msgPostTransaction(tx0);
        walletNode.sendMessageToPeer(message, "localhost", 2005);
        
    }
}
