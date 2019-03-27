package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;
import com.upec.peers.network.nio.SerializerBuffer;

import java.nio.ByteBuffer;

public class ListeningPortRequest implements Serializable {

    public static byte ID = 0x02;

    private int port;

    public static Creator<ListeningPortRequest> creator = serializerBuffer -> {
        int port = serializerBuffer.readInt();
        return new ListeningPortRequest(port);
    };

    public int getPort() {
        return this.port;
    }

    public ListeningPortRequest(int port) {
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