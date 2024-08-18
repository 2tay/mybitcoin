package com.example.Networking.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.example.Networking.Server.Response;

public class Client {
    
    // Send Serialized object to node
    public Response sendSerializedMessage(Message message, String host, int port) {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
        {
            // Serialize message and send
            out.writeObject(message);
            System.out.println(message.toString() + " Sent to server Successfully");

            // receive and Deserialize Response
            Response response = (Response) in.readObject();
            if(response.getStatus() == Response.Status.OK) {
                System.out.println(response.toString());
            } else{
                System.out.println("request failed: " + response.toString());
            }
            return response;
        } 
        catch (Exception e) {
            System.err.println("Failed to sendSerializeObject to node");
            e.printStackTrace();
            return null;
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
        client.sendSerializedMessage(msg, "localhost", port);

        scanner.close();
    }

    // MAIN FUNCTION
    public static void main(String[] args) {
        testSendSerializedObject();
    }
}
