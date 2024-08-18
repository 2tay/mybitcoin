package com.example.Networking.Nodes;

import com.example.Wallet.Wallet;

public class BootstrapNode {
    private static final int serverPort = 2000;
    private static final String HOST = "localhost";
    private static Wallet satoshiWallet;
    
    public BootstrapNode() {
        satoshiWallet = new Wallet();
        BootstrapHelper.genisisBlock(satoshiWallet);
    }
}
