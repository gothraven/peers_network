package com.upec.peers.Treatement;


import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class MessageRequest implements Serializable {

    public static byte ID = 0x01;

    private String message;

    public static Creator<MessageRequest> creator = serializerBuffer -> {
        var message = serializerBuffer.readString();
        return new MessageRequest(message);
    };

    public MessageRequest(String message) {
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
}
