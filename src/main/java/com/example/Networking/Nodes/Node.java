package com.example.Networking.Nodes;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import com.example.Networking.Client.Client;
import com.example.Networking.Client.Request;
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
