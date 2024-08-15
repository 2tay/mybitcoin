package com.example.Networking.Server;

import java.security.PublicKey;
import java.util.List;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Client.Message;
import com.example.Pool.TransactionPool;
import com.example.Transaction.Transaction;

public class ResponseHelper {
    private Message requestMessage;

    public ResponseHelper(Message msg) {
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

        return new Response(Response.Status.NOT_FOUND, null);
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
            return new Response(Response.Status.NOT_FOUND, null);
        }

        return new Response(Response.Status.BAD_REQUEST, null);
    }

    public Response handlePostBlock() {
        if(requestMessage.getArgument() instanceof Block)
        {
            // TODO : LOGIC BEFORE ADD BLOCK TO BLOCKCHAIN
            Block minedBlock = (Block) requestMessage.getArgument();
            Blockchain.addToBlockchain(minedBlock);
            return new Response(Response.Status.OK, minedBlock);
        }
        return new Response(Response.Status.BAD_REQUEST, null);
    }

    public Response handleGetBlockchain() {
        List<Block> blockchain = Blockchain.getBlockchain();
        if(blockchain != null) 
        {
            return new Response(Response.Status.OK, blockchain);
        }
        
        return new Response(Response.Status.NOT_FOUND, null);
    }

    public Response handleGetTransaction() {
        if(requestMessage.getArgument() instanceof PublicKey)
        {
            PublicKey pubkey = (PublicKey) requestMessage.getArgument();
            // TODO ; LOGIC HERE
            return new Response(Response.Status.OK, pubkey);
        }
        return new Response(Response.Status.NOT_FOUND, null);
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

            return new Response(Response.Status.UNAUTHORIZED, tx);
        }

        return new Response(Response.Status.BAD_REQUEST, null);
    }

}
