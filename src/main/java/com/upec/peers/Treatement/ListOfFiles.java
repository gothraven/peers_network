package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ListOfFiles {


    public static ByteBuffer getCommand(ArrayList<File> listOfFiles) {
        SerializerBuffer byteBuffer = new SerializerBuffer(ByteBuffer.allocate(1024));
        byteBuffer.writeChar('6');
        byteBuffer.writeInt(listOfFiles.size());
        for (int i = 0; i < listOfFiles.size(); i++) {
            byteBuffer.writeString(listOfFiles.get(i).getName());
            byteBuffer.writeLong(listOfFiles.get(i).getLength());
        }
        return byteBuffer.getByteBuffer();
    }

    public static ArrayList<File> getListFiles(ByteBuffer bb) {
        ArrayList<File> list = new ArrayList<>();
        SerializerBuffer byteBuffer = new SerializerBuffer(bb.flip());
        // recuperer l'ID
        //byteBuffer.readChar() ;
        int taille = byteBuffer.readInt();
        for (int i = 0; i < taille; i++) {
            list.add(new File(byteBuffer.readLong(), byteBuffer.readString()));
        }
        return list;
    }
}
