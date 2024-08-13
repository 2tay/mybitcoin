package com.example.Networking;

import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(socket.getInputStream())) {
            while (in.hasNextLine()) {
                String message = in.nextLine();
                System.out.println("Received: " + message);
            }
        } catch (Exception e) {
            System.err.println("Failed to handle client connection");
            e.printStackTrace();
        }
    }
}
