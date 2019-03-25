package com.upec.peers.Treatement;

import com.upec.peers.Server.SerializerBuffer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ListOfPeers {

    private int numberOfPeers;
    private ArrayList<Peer> list;

    public ListOfPeers(ArrayList<Peer> list, int n) {
        this.numberOfPeers = n;
        this.list = list;
    }

    public static ByteBuffer getCommand(int numberOfPeers, ArrayList<Peer> list) {
        SerializerBuffer byteBuffre = new SerializerBuffer(ByteBuffer.allocate(1024));
        byteBuffre.writeChar('4');
        byteBuffre.writeInt(numberOfPeers);
        for (int i = 0; i < list.size(); i++) {
            byteBuffre.writeInt(list.get(i).getPort());
            byteBuffre.writeString(list.get(i).getUrl());
        }
        return byteBuffre.getByteBuffer();
    }

    public static void addToListPeer(ByteBuffer bb, ArrayList<Peer> list) {
        SerializerBuffer byteBuffer = new SerializerBuffer(bb.flip());
        // recupere l'ID
        //byteBuffer.readChar();
        int number = byteBuffer.readInt();
        for (int i = 0; i < number; i++) {
            list.add(new Peer(byteBuffer.readInt(), byteBuffer.readString()));
        }
    }


}
