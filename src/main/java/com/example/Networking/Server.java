package com.example.Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Server started on port: " + port);
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Failed to start Server");
            e.printStackTrace();
        }
    }


    // TESTING FUNCTIONS
    public static void testStartServer() {
        Server server = new Server(2000);
        server.startServer();
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
        testStartServer();
    }
}
