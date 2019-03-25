package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ListOfSharedFilesCommand {

    public static class Request {

        public static byte ID = 0x05;

        public static ByteBuffer serialize() {
            SerializerBuffer serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(1));
            serializerBuffer.writeByte(ID);
            return serializerBuffer.getByteBuffer();
        }

        public static void deserialize(ByteBuffer byteBuffer) {
        }
    }

    public static class Response {

        public static byte ID = 0x06;

        public static ByteBuffer serialize(List<SharedFile> listOfSharedFiles) {
            SerializerBuffer serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(512));
            serializerBuffer.writeByte(ID);
            serializerBuffer.writeInt(listOfSharedFiles.size());
            for (SharedFile listOfSharedFile : listOfSharedFiles) {
                serializerBuffer.writeString(listOfSharedFile.getName());
                serializerBuffer.writeLong(listOfSharedFile.getSize());
            }
            return serializerBuffer.getByteBuffer();
        }

        public static List<SharedFile> deserialize(ByteBuffer byteBuffer) {
            List<SharedFile> list = new ArrayList<>();
            SerializerBuffer serializerBuffer = new SerializerBuffer(byteBuffer);
            int taille = serializerBuffer.readInt();
            for (int i = 0; i < taille; i++) {
                String filename = serializerBuffer.readString();
                long size = serializerBuffer.readLong();
                list.add(new SharedFile(size, filename));
            }
            return list;
        }
    }

    public static void main(String[] args) {
        var t = Response.serialize(List.of(
                new SharedFile(12L, "name1.txt"),
                new SharedFile(13L, "name2.txt"))
        );
        t.rewind();
        var id = t.get();
        assert id == Response.ID;
        System.out.println(Response.deserialize(t));
    }
}
