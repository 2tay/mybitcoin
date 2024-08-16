package com.example.Networking_Testing;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Message;
import com.example.Networking.Client.MessageHelper;
import com.example.Networking.Nodes.WalletNode;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.miner.BlockMiner;

public class TestWalletNode {

    // ----------------> TESTING FUNCTIONS <-------------------------------
    public static void testSendTxToMiner() {
        WalletNode walletNode = new WalletNode(2005);
        Wallet recipient = new Wallet();

        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);

        Transaction tx0 = walletNode.processTransaction(recipient.getPublicKey(), 100);
        Message message = MessageHelper.msgPostTransaction(tx0);
        walletNode.sendMessageToPeer(message, "localhost", 2005);
    }

    public static void testSendBlockToMiner() {
        WalletNode walletNode = new WalletNode(2005);
        Wallet recipient = new Wallet();
        BlockMiner miner1 = new BlockMiner("miner1");

        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);
        UTXO.genesisUtxo(walletNode.wallet, 100);

        Transaction tx0 = walletNode.processTransaction(recipient.getPublicKey(), 100);
        Transaction tx1 = walletNode.processTransaction(recipient.getPublicKey(), 100);
        Transaction tx2 = walletNode.processTransaction(recipient.getPublicKey(), 100);

        Block newBlock = new Block(new ArrayList<>(Arrays.asList(tx0, tx1, tx2)), Blockchain.getLatestBlockId(), miner1);
        Message message = MessageHelper.msgPostBlock(newBlock);
        walletNode.sendMessageToPeer(message, "localhost", 2000);
    }


    public static void main(String[] args) {
        testSendBlockToMiner();
    }
}
