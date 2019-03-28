package com.upec.peers.network.protocol;


import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.utils.Creator;
import com.upec.peers.network.utils.Serializable;

import java.nio.ByteBuffer;

public class InformationMessage implements Serializable {

    public static final byte ID = 0x01;

    private String message;

    public static final Creator<InformationMessage> creator = serializerBuffer -> {
        var message = serializerBuffer.readString();
        return new InformationMessage(message);
    };

    public InformationMessage(String message) {
        this.message = message;
    }

    @Override
    public SerializerBuffer serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.message.getBytes().length + 1 + 4);
        SerializerBuffer searlizerBuffer = new SerializerBuffer(byteBuffer);
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeString(this.message);
        return searlizerBuffer;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
