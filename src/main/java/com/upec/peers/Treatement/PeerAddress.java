package com.upec.peers.Treatement;


public class PeerAddress {

    private int port;
    private String url;

    public PeerAddress(int port, String url) {
        this.port = port;
        this.url = url;
    }

    public int getPort() {
        return this.port;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public String toString() {
        return url + ":" + port;
    }
}
