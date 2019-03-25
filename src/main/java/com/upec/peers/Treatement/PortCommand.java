package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class PortCommand implements Serializable {

    public static ByteBuffer getCommand(int p) {
        SerializerBuffer byteBuffer = new SerializerBuffer(ByteBuffer.allocate(20));
        byteBuffer.writeChar('2');
        byteBuffer.writeInt(p);
        return byteBuffer.getByteBuffer();
    }

    public static int getPort(ByteBuffer bb) {
        SerializerBuffer byteBuffer = new SerializerBuffer(bb.flip());
        // recuperer l'ID
        byteBuffer.readChar();
        return byteBuffer.readInt();
    }

    public static void main(String[] args) {
        var t = getCommand(50);
        System.out.println(getPort(t));
    }
}
