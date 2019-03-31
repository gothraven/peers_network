package com.upec.peers.network.objects;


public class PeerAddress {

    private int port;
    private String url;

    public PeerAddress(int port, String url) {
        this.port = port;
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        PeerAddress that = (PeerAddress) obj;
        return that.url.equals(this.url)
                && that.port == this.port;
    }

    @Override
    public int hashCode() {
        return url.hashCode() + port;
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
