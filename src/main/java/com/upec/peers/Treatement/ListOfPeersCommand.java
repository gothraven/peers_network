package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ListOfPeersCommand {

    public static class Request {

        public static byte ID = 0x03;

        public static ByteBuffer serialize() {
            SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
            searlizerBuffer.writeByte(ID);
            return searlizerBuffer.getByteBuffer();
        }

        public static void deserialize(ByteBuffer byteBuffer) {

        }
    }

    public static class Response {

        public static byte ID = 0x04;

        public static ByteBuffer serialize(List<PeerAddress> peersAddressList) {
            SerializerBuffer searlizerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
            searlizerBuffer.writeByte(ID);
            searlizerBuffer.writeInt(peersAddressList.size());
            for (PeerAddress peerAddress : peersAddressList) {
                searlizerBuffer.writeInt(peerAddress.getPort());
                searlizerBuffer.writeString(peerAddress.getUrl());
            }
            return searlizerBuffer.getByteBuffer();
        }

        public static List<PeerAddress> deserialize(ByteBuffer byteBuffer) {
            List<PeerAddress> peersAddressList = new ArrayList<>();
            SerializerBuffer serializerBuffer = new SerializerBuffer(byteBuffer);
            int listLength = serializerBuffer.readInt();
            for (int i = 0; i < listLength; i++)
                peersAddressList.add(new PeerAddress(serializerBuffer.readInt(), serializerBuffer.readString()));
            return peersAddressList;
        }

    }


    public static void main(String[] args) {
        var t = Response.serialize(List.of(
                new PeerAddress(12, "someurl1"),
                new PeerAddress(13, "someurl2"))
        );
        t.rewind();
        var id = t.get();
        assert id == Response.ID;
        System.out.println(Response.deserialize(t));
    }
}
