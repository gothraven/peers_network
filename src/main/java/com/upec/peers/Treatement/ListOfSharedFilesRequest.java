package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListOfSharedFilesRequest implements Serializable {

    public static byte ID = 0x05;
    public Creator<ListOfSharedFilesRequest> creator = byteBuffer -> new ListOfSharedFilesRequest();

    public ListOfSharedFilesRequest() {

    }

    @Override
    public SerializerBuffer serialize() {
        SerializerBuffer serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
        serializerBuffer.writeByte(ID);
        return serializerBuffer;
    }
}
