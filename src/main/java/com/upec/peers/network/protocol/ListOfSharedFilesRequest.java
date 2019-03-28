package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListOfSharedFilesRequest implements Serializable {

    public static final byte ID = 0x05;

    public static Creator<ListOfSharedFilesRequest> creator = byteBuffer -> new ListOfSharedFilesRequest();

    public ListOfSharedFilesRequest() {}

    @Override
    public SerializerBuffer serialize() {
        SerializerBuffer serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
        serializerBuffer.writeByte(ID);
        return serializerBuffer;
    }
}
