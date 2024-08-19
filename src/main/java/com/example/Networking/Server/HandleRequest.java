package com.example.Networking.Server;

import java.util.List;
import java.util.Set;

import com.example.Block.Block;
import com.example.Blockchain.Blockchain;
import com.example.Networking.Nodes.MainServer;
import com.example.Networking.Nodes.Node;
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
            // TODO: check block if mined verification
            Block minedBlock = (Block) argument;
            Blockchain.addToBlockchain(minedBlock);
            return new Response(Response.Status.OK, "Block Added successfully to Blockchain");
        }
        // Get Blockchain from MainServer (Trust Node)
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
        // Node sent from MAinServer to Each Node to add new Node
        else if(method.equals("postNode")) {
            if(argument instanceof String && argument != null) {
                String nodeInfos = (String) argument;
                Node.addToNetworkNodes(nodeInfos);
            }
        }
        // Specefied for MAinServer
        else if(method.equals("postMyNode"))
        {
            if(argument instanceof String && argument != null) {
                String nodeInfos = (String) argument;
                MainServer.addToNetwork(nodeInfos);
                return new Response(Response.Status.OK, "you are in network now under: " + nodeInfos);
            }

            return new Response(Response.Status.BAD_REQUEST, "invalid Argument [String 'myhost:myport']");
        }
        else if(method.equals("getNetwork")) {
            Set<String> networkNodes = MainServer.getNetworkNodes();
            return new Response(Response.Status.OK, networkNodes);
        }

        return null;
    }
}
