package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class MessageCommand implements Serializable {

    private String message;
    private SerializerBuffer byteBuffer;

    public MessageCommand(ByteBuffer bb) {
        byteBuffer = new SerializerBuffer(bb);
        message = byteBuffer.readString();
    }

    public MessageCommand(String message) {
        byteBuffer.writeString(message);
    }

    public String getMessage() {
        return message;
    }

    public ByteBuffer getCommand(String message) {
        return null;
    }
}
