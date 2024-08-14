package com.example.Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    public void connectToServer(String host, int port) {
        try(Socket socket = new Socket(host, port))
        {
            System.out.println("Connected to server: " + host + " port: " + port);
            // socket streams
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in))
            {
                // send message to Node
                System.out.println("Enter message: ");
                String message = scanner.nextLine();
                out.println(message);

                // receive response
                String response;
                while((response = in.readLine()) != null) {
                    System.out.println("Server: " + response);
                }
            } 
            catch (IOException e) {
                System.err.println("Failed to establish clientSocket streams");
                e.printStackTrace();
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Failed to establish socket Unkown Host: " + host);
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("Failed to establish socket");
            e.printStackTrace();
        }
    }

    // Send Serialized object to node
    public void sendSerializObject(Object o, String host, int port) {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) 
        {
            out.writeObject(o);
            System.out.println(o.toString() + "Sent to server Successfully");
        } 
        catch (Exception e) {
            System.err.println("Failed to sendSerializeObject to node");
            e.printStackTrace();
        }
    }


    // TESTING FUNCTIONS
    public static void testConnectToNode() {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter port server to connect: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        client.connectToServer("localhost", port);
        scanner.close();
    }

    public static void testSendSerializedObject() {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter port server to connect: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        Message msg = new Message("heey server"); 
        client.sendSerializObject(msg, "localhost", port);

        scanner.close();
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
        //testConnectToNode();
        testSendSerializedObject();
    }
}
