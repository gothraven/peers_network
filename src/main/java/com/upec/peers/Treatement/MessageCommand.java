package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class MessageCommand {


    public static String getMessage(ByteBuffer bb) {
        SerializerBuffer byteBuffer = new SerializerBuffer(bb.flip());
        // recuperer l'ID
        //System.out.println(byteBuffer.readInt());
        byteBuffer.readChar();
        return byteBuffer.readString();
    }

    public static ByteBuffer getCommand(String message) {
        var strLength = Charset.forName("UTF-8").encode(message).remaining();
        SerializerBuffer byteBuffer = new SerializerBuffer(ByteBuffer.allocate(1024));
        byteBuffer.writeChar('\1');
        byteBuffer.writeString(message);
        return byteBuffer.getByteBuffer();
    }


    public static void main(String[] args) {
        var t = getCommand("test");
        System.out.println(getMessage(t));
    }
}
