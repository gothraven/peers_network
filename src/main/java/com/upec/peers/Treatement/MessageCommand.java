package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class MessageCommand {

    public static byte ID = 0x01;

    public static ByteBuffer serialize(String message) {
        SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeString(message);
        return searlizerBuffer.getByteBuffer();
    }

    public static String deserialize(ByteBuffer bb) {
        SerializerBuffer byteBuffer = new SerializerBuffer(bb);
        return byteBuffer.readString();
    }

    public static void main(String[] args) {
        var t = serialize("test");
        t.rewind();
        var id = t.get();
        assert id == ID;
        System.out.println(deserialize(t));
    }
}
