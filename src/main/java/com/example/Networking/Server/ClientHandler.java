package com.example.Networking.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.example.Networking.Client.Message;

public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        handleObjectMessage();
    }

    public void handleTextMessage() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    public void handleObjectMessage() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            // Read the object from the input stream
            Message message = (Message) in.readObject();
    
            // Log the received message details
            System.out.println("Received --> request: " + message);
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to cast the received object to Message class.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to read object from the input stream.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while handling the object message.");
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to close the socket.");
                e.printStackTrace();
            }
        }
    }
    
}
