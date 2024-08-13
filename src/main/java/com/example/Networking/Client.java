package com.example.Networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static final Scanner scanner = new Scanner(System.in);
    private Socket socket;

    public void connectToNode(String ipAddress, int port) throws UnknownHostException, IOException {
        socket = new Socket(ipAddress, port);
        System.out.println("Connected to server at IP: " + ipAddress + " Port: " + port);

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            sendMessage(out);
        } finally {
            socket.close();
        }
    }

    private void sendMessage(PrintWriter out) {
        System.out.print("Enter message to send: ");
        String message = scanner.nextLine();
        out.println(message);
    }

    public static void main(String[] args) {
        Client client = new Client();

        System.out.print("Enter the Node Port to connect: ");
        int port = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        try {
            client.connectToNode("localhost", port);
        } catch (UnknownHostException e) {
            System.err.println("Failed to connect to the specified Node IP.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to establish connection with Node at localhost, port: " + port);
            e.printStackTrace();
        }
    }
}
