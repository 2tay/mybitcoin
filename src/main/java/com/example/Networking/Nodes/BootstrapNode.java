package com.example.Networking.Nodes;

import com.example.Wallet.Wallet;

public class BootstrapNode extends Node {
    private static final int serverPort = 2000;
    private static final String host = "localhost";
    private static Wallet satoshiWallet;
    
    public BootstrapNode() {
        super(serverPort);
        satoshiWallet = new Wallet();
        BootstrapHelper.genisisBlock(satoshiWallet);
    }
}
