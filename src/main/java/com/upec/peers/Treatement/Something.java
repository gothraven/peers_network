package com.upec.peers.Treatement;


import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

class Something implements Serializable/*, Command*/ {

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

