package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.io.*;
import java.nio.ByteBuffer;

public class SharedFile {

    private Long size;
    private String name;

    public SharedFile(Long size, String name) {
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
