package com.example.Networking.Nodes;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Client;
import com.example.Networking.Client.Request;
import com.example.Networking.Client.RequestHelper;
import com.example.Networking.Server.Response;
import com.example.Networking.Server.Server;

public class Node {
    public static String host;
    public static int port;
    public static Server server;
    public static Client client;
    public static final Set<String> networkNodes = new HashSet<>();

    public Node(int serverPort) {
        try {
            host = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Failed to determine the local host address.");
            throw new RuntimeException("Could not initialize Node: " + e.getMessage(), e);
        }
        port = serverPort;
        server = new Server(port);
        client = new Client();

        // JOin Network by MainServer
        ServerHelper.addMeToNetwork(port, "localhost", 2000);
        // Get Network Nodes from MainServer
        Set<String> nodes = ServerHelper.getNetworkNodes("localhost", 2000);
        if (nodes != null) {
            networkNodes.addAll(nodes);
        }

        startupBlockchain();
    }

    public void startupBlockchain() {
        // Request the blockchain and add blocks to the blockchain
        Request req = RequestHelper.getBlockchain();
        Response response = client.sendSerializedMessage(req, "localhost", 2000);

        // Check if the response is valid and the content is of the expected type
        if (response != null && response.getStatus() == Response.Status.OK) {
            Object content = response.getContent();
            if (content instanceof List<?>) {
                List<?> contentList = (List<?>) content;
                
                // Ensure that the content list contains Block objects
                if (!contentList.isEmpty() && contentList.get(0) instanceof Block) {
                    List<Block> chain = (List<Block>) contentList;
                    
                    // Add each block to the blockchain
                    for (Block block : chain) {
                        Blockchain.addToBlockchain(block);
                    }
                } else {
                    System.err.println("Unexpected content type in the list.");
                }
            } else {
                System.err.println("Unexpected response content type.");
            }
        } else {
            System.err.println("Failed to retrieve blockchain or invalid response.");
        }

    }

    public synchronized static void addToNetworkNodes(String nodeInfos) {
        networkNodes.add(nodeInfos);
    }

    public static void sendReqToAllNetwork(Request req) {
        for(String node : networkNodes) {
            String[] nodeInfos = node.split(":");
            client.sendSerializedMessage(req, nodeInfos[0], Integer.parseInt(nodeInfos[1]));
        }
    }
}
