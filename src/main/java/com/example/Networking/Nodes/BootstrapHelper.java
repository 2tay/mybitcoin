package com.example.Networking.Nodes;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Pool.TransactionPool;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.miner.BlockMiner;

public class BootstrapHelper {

    public static Block genisisBlock(Wallet satoshiWallet) {
        BlockMiner miner = new BlockMiner("satoshiMiner");

        UTXO.genesisUtxo(satoshiWallet, 200);
        UTXO.genesisUtxo(satoshiWallet, 200);
        UTXO.genesisUtxo(satoshiWallet, 200);

        Transaction tx0 = satoshiWallet.processTransaction(satoshiWallet.getPublicKey(), 200);
        Transaction tx1 = satoshiWallet.processTransaction(satoshiWallet.getPublicKey(), 200);
        Transaction tx2 = satoshiWallet.processTransaction(satoshiWallet.getPublicKey(), 200);

        Block firstBlock = new Block(new ArrayList<>(Arrays.asList(tx0, tx1, tx2)), null, miner);
        Blockchain.addToBlockchain(firstBlock);

        TransactionPool.removeTransaction(tx0);
        TransactionPool.removeTransaction(tx1);
        TransactionPool.removeTransaction(tx2);

        return firstBlock;
    }

}
