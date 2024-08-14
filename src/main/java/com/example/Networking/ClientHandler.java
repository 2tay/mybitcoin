package com.example.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
            try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) 
            {
                String inputLine;
                while((inputLine = in.readLine()) != null) {
                    System.out.println("Client: " + inputLine);
                }
            } 
            catch (IOException e) {
                System.err.println("Failed to establish serverSocket streams");
                e.printStackTrace();
            }
    }
}
