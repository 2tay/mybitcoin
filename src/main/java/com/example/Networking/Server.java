package com.example.Networking;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {

    private int port;
    private String ipAddress;

    public void startServer() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter port: ");
            port = scanner.nextInt();
            scanner.nextLine(); // consume newline

            ipAddress = Inet4Address.getLocalHost().getHostAddress();
            System.out.println("Server starting at IP: " + ipAddress + " on port: " + port);

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    try {
                        Socket socket = serverSocket.accept();
                        new ClientHandler(socket).start();
                    } catch (IOException e) {
                        System.err.println("Failed to accept client socket.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();

        try {
            server.startServer();
        } catch (UnknownHostException e) {
            System.err.println("Failed to get server IP address.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to start server at IP: " + server.ipAddress + " Port: " + server.port);
            e.printStackTrace();
        }
    }
}
