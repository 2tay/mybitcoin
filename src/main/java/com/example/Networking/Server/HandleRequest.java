package com.example.Networking.Server;

import java.util.List;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Pool.TransactionPool;
import com.example.Pool.UTXOSet;
import com.example.Transaction.Transaction;

public class HandleRequest {
    private String method;
    private Object argument;

    public HandleRequest(String method, Object argument) {
        this.method = method;
        this.argument = argument;
    }

    public Response handleRequest() {
        if(method.equals("postTx")) 
        {
            if(argument instanceof Transaction && argument != null) 
            {
                Transaction tx = (Transaction) argument;
                if(TransactionPool.addToPool(tx)) 
                {
                    // Update UTXO Pool
                    UTXOSet.removeConsumedUtxosTransaction(tx);
                    UTXOSet.addUtxosTransaction(tx);

                    return new Response(Response.Status.OK, "Added Transaction Successfully");
                }

                return new Response(Response.Status.UNAUTHORIZED, "Transaction is not valid");
            }

            return new Response(Response.Status.BAD_REQUEST, "invalid Argument [Transaction]");
        }
        else if(method.equals("postBlock"))
        {
            // TODO: check block if mined 
            Block minedBlock = (Block) argument;
            Blockchain.addToBlockchain(minedBlock);
            return new Response(Response.Status.OK, "Block Added successfully to Blockchain");
        }
        else if(method.equals("getBlockchain")) 
        {
            List<Block> chain = Blockchain.getBlockchain();
            return new Response(Response.Status.OK, chain);
        }
        else if(method.equals("getBlocks"))
        {
            if(argument instanceof Block && argument != null) 
            {
                Block block = (Block) argument;
                List<Block> blocks = Blockchain.getNewBlocks(block);
                return new Response(Response.Status.OK, blocks);
            }
            
            return new Response(Response.Status.BAD_REQUEST, "invalid Argument [Block]");
        }
        else if(method.equals("postNode")) {
            // TODO : post nodeInfos received from bootstrap node in own peerNode
        }
        else if(method.equals("postMyNode"))
        {
            // TODO: todo later
        }
        else if(method.equals("getNetwork")) {
            //TODO : todolater
        }

        return null;
    }
}
