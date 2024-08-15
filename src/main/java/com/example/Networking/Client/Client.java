package com.example.Networking.Client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
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


    // --------------->     TESTING FUNCTIONS   <------------------------------

    public static void testSendSerializedObject() {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter port server to connect: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        Message msg = MessageHelper.msgGetBlockchain();
        client.sendSerializObject(msg, "localhost", port);

        scanner.close();
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
        testSendSerializedObject();
    }
}
