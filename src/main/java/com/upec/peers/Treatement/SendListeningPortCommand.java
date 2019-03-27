package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class SendListeningPortCommand {

    public static byte ID = 0x02;

    public static ByteBuffer serialize(int port) throws BufferOverflowException {
        SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
        searlizerBuffer.writeByte(ID);
        searlizerBuffer.writeInt(port);
        return searlizerBuffer.getByteBuffer();
    }

    public static int deserialize(ByteBuffer bb) throws BufferUnderflowException {
        SerializerBuffer byteBuffer = new SerializerBuffer(bb);

        return byteBuffer.readInt();
    }

    public static void main(String[] args) {
        var t = serialize(1234);
        t.rewind();
        var id = t.get();
        assert id == ID;
        System.out.println(deserialize(t));
    }
}



interface Creator<T> {
    T construct(ByteBuffer byteBuffer) throws BufferUnderflowException;
}

interface Serializable {
    ByteBuffer serialize() throws BufferOverflowException;
}


class Something implements Serializable, Command {

    int s;

    public Creator<Something> creator = new Creator<Something>() {
        @Override
        public Something construct(ByteBuffer byteBuffer) throws BufferUnderflowException {
            var s = new Something();

            return null;
        }
    };

    Something(int s) {
        this.s = s;
    }

    Something() {

    }

    public ByteBuffer serialize() throws BufferOverflowException {
        return ByteBuffer.allocate(12);
    }

    public void deserialize(ByteBuffer bb) throws BufferUnderflowException {
        this.setS(bb.getInt());
    }

    public void setS(int s) {
        this.s = s;
    }


    public static void main(String[] args) {
        var s = new Something(21);
        ByteBuffer bb = s.serialize();

        var s2  = new Something();
        s2.deserialize(bb);

    }

}

