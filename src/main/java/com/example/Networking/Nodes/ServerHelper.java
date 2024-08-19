package com.example.Networking.Nodes;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Request;
import com.example.Networking.Client.RequestHelper;
import com.example.Networking.Server.Response;
import com.example.Networking.Server.Server;
import com.example.Transaction.Transaction;
import com.example.Transaction.UTXO;
import com.example.Wallet.Wallet;
import com.example.miner.BlockMiner;

public class ServerHelper {
    
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

        return firstBlock;
    }

    public static void startNodeServer(Server server) {
        new Thread(() -> server.startServer()).start();
    }

    public static void stopNodeServer(Server server) {
        server.stopServer(); 
    }

    public static void addMeToNetwork(int myport, String bootstrapNodeHost, int bootstrapNodePort) {
        try {
            String ipAdd = Inet4Address.getLocalHost().getHostAddress();
            Request req = RequestHelper.postMyNode(ipAdd + ":" + myport);
            Node.client.sendSerializedMessage(req, bootstrapNodeHost, bootstrapNodePort);
        } 
        catch (UnknownHostException e) {
            System.err.println("Failed to get own node IP ADDRESS");
            e.printStackTrace();
        } 
    }

    public static HashSet<String> getNetworkNodes(String bootstrapNodeHost, int bootstrapNodePort) {
        Request req = RequestHelper.getNetwork();
        Response response = Node.client.sendSerializedMessage(req, bootstrapNodeHost, bootstrapNodePort);
    
        if (response != null && response.getStatus() == Response.Status.OK && response.getContent() instanceof HashSet<?>) {
            System.out.println("Response getAllNodes is not empty and response Status == OK");
            HashSet<?> content = (HashSet<?>) response.getContent();
    
            if (!content.isEmpty() && content.iterator().next() instanceof String) {
                return (HashSet<String>) content;
            } else {
                System.err.println("Unexpected content type or empty content: " + content);
            }
        } else {
            System.err.println("Failed to retrieve network nodes or invalid content type.");
        }
        
        return new HashSet<>(); // Return an empty HashSet if something goes wrong
    }
}
