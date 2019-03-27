package com.upec.peers.Treatement;


import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;

public class MessageRequest implements Serializable {


    private static byte ID = 0x01;
    private String message;

    public Creator<MessageRequest> creator = serializerBuffer -> new MessageRequest(serializerBuffer.readString());



    public MessageRequest(String message) {
        this.message = message;
    }

    @Override
    public SerializerBuffer serialize() {
        SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeString(this.message);
        return searlizerBuffer;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
