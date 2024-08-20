package com.example.Networking.Nodes;

import java.util.HashSet;
import java.util.Set;

import com.example.Networking.Client.Client;
import com.example.Networking.Client.Request;
import com.example.Networking.Server.Server;
import com.example.Wallet.Wallet;

public class MainServer {
    public static final int PORT = 2000;
    public static final String HOST = "localhost";
    public static Client client;
    public static Server server;
    public static final Set<String> networkNodes = new HashSet<>();
    public static Wallet satoshiWallet;

    public MainServer() {
        client = new Client();
        server = new Server(PORT);
        satoshiWallet = new Wallet();
        ServerHelper.genisisBlock(satoshiWallet);
        networkNodes.add(HOST + ":" + PORT);
        ServerHelper.startNodeServer(server);
    }

    public synchronized static void addToNetwork(String nodeInfos) {
        networkNodes.add(nodeInfos);
    }

    public synchronized static Set<String> getNetworkNodes() {
        return networkNodes;
    }

    public static void sendReqToAllNetwork(Request req) {
        for(String node : networkNodes) {
            String[] nodeInfos = node.split(":");
            client.sendSerializedMessage(req, nodeInfos[0], Integer.parseInt(nodeInfos[1]));
        }
    }


}
