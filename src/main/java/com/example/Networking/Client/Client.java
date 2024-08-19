package com.example.Networking.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.Networking.Server.Response;

public class Client {
    
    // Send Serialized object to node
    public Response sendSerializedMessage(Request req, String host, int port) {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
        {
            // Serialize req and send
            out.writeObject(req);
            System.out.println(req.toString() + " Sent to server Successfully");

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

    // MAIN FUNCTION
    public static void main(String[] args) {

    }
}
