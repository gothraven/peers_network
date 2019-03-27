package com.upec.peers.Treatement;

public class SharedFile {

    private Long size;
    private String name;

    public SharedFile(String name, Long size) {
        this.size = size;
        this.name = name;
    }

    public Long getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "size: " + size + ", name: " + name;
    }
}
