package com.example.Networking.Server;

import java.util.List;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Message;
import com.example.Networking.Nodes.BootstrapNode;
import com.example.Networking.Nodes.Node;
import com.example.Pool.TransactionPool;
import com.example.Transaction.Transaction;

public class HandleRequestHelper {
    private Message requestMessage;

    public HandleRequestHelper(Message msg) {
        requestMessage = msg;
    }

    public boolean requestChoice(String method, String path) {
        return requestMessage.getMethod().equals(method) && requestMessage.getPath().equals(path);
    }

    public Response handleGetLatestBlock() {
        Block lastBlock = Blockchain.getLatestBlock();
        if(lastBlock != null) 
        {
            return new Response(Response.Status.OK, Blockchain.getLatestBlockId());
        } 

        return new Response(Response.Status.NOT_FOUND);
    }

    public Response handleGetBlocksFrom() {
        if(requestMessage.getArgument() instanceof Block) 
        {
            // Block from client's message
            Block lastClientBlock = (Block) requestMessage.getArgument();
            // new Blocks for client in the blockchain
            List<Block> newClientBlocs = Blockchain.getNewBlocks(lastClientBlock);
            // Response if there is blocks
            if(newClientBlocs != null)
            {
                return new Response(Response.Status.OK, newClientBlocs);
            }
            // newBlocks == null
            return new Response(Response.Status.NOT_FOUND);
        }

        return new Response(Response.Status.BAD_REQUEST);
    }

    public Response handlePostBlock() {
        if(requestMessage.getArgument() instanceof Block)
        {
            // TODO : LOGIC BEFORE ADD BLOCK TO BLOCKCHAIN
            Block minedBlock = (Block) requestMessage.getArgument();
            Blockchain.addToBlockchain(minedBlock);
            return new Response(Response.Status.OK, minedBlock);
        }
        return new Response(Response.Status.BAD_REQUEST);
    }

    public Response handleGetBlockchain() {
        List<Block> blockchain = Blockchain.getBlockchain();
        if(blockchain != null) 
        {
            return new Response(Response.Status.OK, blockchain);
        }
        
        return new Response(Response.Status.NOT_FOUND);
    }

    public Response handlePostTransaction() 
    {
        if(requestMessage.getArgument() instanceof Transaction)
        {
            Transaction tx = (Transaction) requestMessage.getArgument();
            if(TransactionPool.addToPool(tx)) 
            {
                return new Response(Response.Status.OK, tx);
            }

            return new Response(Response.Status.UNAUTHORIZED);
        }

        return new Response(Response.Status.BAD_REQUEST);
    }

    // Get All Nodes from bootstrap Node
    public Response handleGetAllNodes() {
        List<String> peerNodes = BootstrapNode.getAllNodes();
        if(!peerNodes.isEmpty()) {
            return new Response(Response.Status.OK, peerNodes);
        }
        
        return new Response(Response.Status.NOT_FOUND);
    }

    public Response handlePostNode() {
        if(requestMessage.getArgument() instanceof String) {
            String nodeInfos = (String) requestMessage.getArgument();

            BootstrapNode.addNode(nodeInfos);

            return new Response(Response.Status.OK, nodeInfos);
        }

        return new Response(Response.Status.BAD_REQUEST);
    }


}
