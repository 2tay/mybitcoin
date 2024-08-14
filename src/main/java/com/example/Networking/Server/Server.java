package com.example.Networking.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
            while (running) {
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Failed to start Server");
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Server stopped.");
            } catch (IOException e) {
                System.err.println("Error while stopping the server");
                e.printStackTrace();
            }
        }
    }

    // TESTING FUNCTIONS
    public static void testStartServer() {
        Server server = new Server(2000);
        server.startServer();
    }

    public static void main(String[] args) {
        testStartServer();
    }
}
