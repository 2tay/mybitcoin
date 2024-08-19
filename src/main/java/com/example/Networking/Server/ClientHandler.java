package com.example.Networking.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.Block.Block;
import com.example.Networking.Client.Request;
import com.example.Networking.Client.RequestHelper;
import com.example.Networking.Nodes.MainServer;


public class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        handleObjectMessage();
    }

    public void handleObjectMessage() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            // Read the object from the input stream
            Request req = (Request) in.readObject();
    
            // Log the received req details
            System.out.println("Received --> request: " + req);

            // Response to Client
            // Handle NullPOinter first before Response
            Response response;
            if (req != null) {
                response = new HandleRequest(req.getMethod(), req.getArgument()).handleRequest();

                // Thread Mainserver Send New MIned Block to All network nodes
                new Thread(() -> {
                    if(req.getMethod().equals("postBlock") && req.getArgument() instanceof Block) {
                        Block minedBlock = (Block) req.getArgument();
                        Request mainServerRequest = RequestHelper.postBlock(minedBlock);
                        for(String nodeinfos : MainServer.networkNodes) {
                            String[] info = nodeinfos.split(":");
                            MainServer.client.sendSerializedMessage(mainServerRequest, info[0], Integer.parseInt(info[1]));
                        }
                    }
                }).start();


            } else {
                response = new Response(Response.Status.BAD_REQUEST, "invalid request: req is null");
            }
            out.writeObject(response);
            System.out.println(response.toString() + "Sent to Client Successfully");


        } catch (ClassNotFoundException e) {
            System.err.println("Failed to cast the received object to Request class.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to read object from the input stream.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while handling the object req.");
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
