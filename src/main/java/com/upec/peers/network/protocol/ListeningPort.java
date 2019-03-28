package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListeningPort implements Serializable {

    public static final byte ID = (byte)2;

    private int port;

    public static final Creator<ListeningPort> creator = serializerBuffer -> {
        int port = serializerBuffer.readInt();
        return new ListeningPort(port);
    };

    public int getPort() {
        return this.port;
    }

    public ListeningPort(int port) {
        this.port = port;
    }

    @Override
    public SerializerBuffer serialize() {
        SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeInt(port);
        return searlizerBuffer;
    }
}