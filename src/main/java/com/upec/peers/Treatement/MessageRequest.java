package com.upec.peers.Treatement;


import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class MessageRequest implements Serializable {

    private static byte ID = 0x01;
    /*
    * public Creator<Something> creator = new Creator<Something>() {
        @Override
        public Something construct(ByteBuffer byteBuffer) throws BufferUnderflowException {
            var s = new Something();

            return null;
    }*/
    public Creator<MessageRequest> creator = serializerBuffer -> {

        return
    };
    private String message;


    public MessageRequest(String message) {
        this.message = message;
    }

    @Override
    public SerializerBuffer serialize() throws BufferOverflowException {
        SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeString(this.message);
        return searlizerBuffer;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
