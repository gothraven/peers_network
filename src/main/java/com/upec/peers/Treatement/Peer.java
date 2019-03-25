package com.upec.peers.Treatement;


public class Peer {

    private int port;
    private String url;

    public Peer(int p, String u) {
        this.port = p;
        this.url = u;
    }

    public int getPort() {
        return this.port;
    }

    public String getUrl() {
        return this.url;
    }
}
