package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;


public class ListOfSharedFilesRequest implements Serializable {

    public static final byte ID = (byte)5;

    public static final Creator<ListOfSharedFilesRequest> creator = byteBuffer -> new ListOfSharedFilesRequest();

    public ListOfSharedFilesRequest() {}

    @Override
    public SerializerBuffer serialize() {
        SerializerBuffer serializerBuffer = new SerializerBuffer(1);
        serializerBuffer.writeByte(ID);
        return serializerBuffer;
    }
}
