package com.upec.peers.network.server;

import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.objects.SharedFile;
import com.upec.peers.network.protocol.*;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;


public class PeerConnected {

    final private int BUFFER_SIZE = 512;
    private PeersConnectedManager manager;
    private SocketChannel socketChannel;
    private SerializerBuffer serializerBuffer;


    public PeerConnected(SocketChannel sc, PeersConnectedManager m) {
        this.manager = m;
        this.socketChannel = sc;
        this.serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(BUFFER_SIZE));
    }

    public void read(SelectionKey sk) throws IOException {

        int bytesRead = socketChannel.read(serializerBuffer.getByteBuffer());
        LinkedList<Byte> byteLinkedList = new LinkedList<>();
        if (bytesRead < 0) {
            System.out.println("Client Left");
            sk.cancel();
            socketChannel.close();
            return;
        }
        if (bytesRead == 0) return;

        while (bytesRead > 0) {
            serializerBuffer.flip();
            while (serializerBuffer.getByteBuffer().hasRemaining()) {
                byteLinkedList.add(serializerBuffer.getByteBuffer().get());
            }
            serializerBuffer.getByteBuffer().clear();
            bytesRead = socketChannel.read(serializerBuffer.getByteBuffer());
        }
        try {
            response();
        } catch (Exception e) {
            e.printStackTrace();
//			serverError(stringWriter.toString(), socketChannel);
        }
    }


    public void response() {
        byte id = serializerBuffer.readByte();
        switch (id) {
            case InformationMessage.ID:
                var message = serializerBuffer.readObject(InformationMessage.creator);
                System.out.println(socketChannel.socket().getPort() + " => " + message.getMessage());
                break;
            case ListeningPortRequest.ID:
                listeningPort();
                break;
            case ListOfPeersRequest.ID:
                peersList();
                break;
            case ListOfSharedFilesRequest.ID:
                fileList();
                break;
            case SharedFileFragmentRequest.ID:
                sharedFragmentFile();
                break;
            default:
                if (id >= 0x64 && id <= 0x128) {
                    extensions();
                    break;
                }
                System.out.println("Error");
                System.out.println("close Connexion");
                break;
        }
    }

    public void sharedFragmentFile() {
        var fragementFile = serializerBuffer.readObject(SharedFileFragmentResponse.creator);
        fragementFile.serialize();
        try {
            socketChannel.write(serializerBuffer.getByteBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extensions() {
        serializerBuffer.readByte();
        serializerBuffer.readInt();
        serializerBuffer.readString();
        System.out.println("Message d'extensions ignore");
    }

    public void peersList() {
        var response = new ListOfPeersResponse(manager.getKnownPeers());
        response.serialize();
        // send it
        try {
            socketChannel.write(serializerBuffer.getByteBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileList() {
        File repository = new File("home/nadir");
        var list = repository.listFiles();
        var files = new ArrayList<SharedFile>();
        for (int i = 0; i < list.length; i++) {
            files.add(new SharedFile(list[i].getName(), list[i].getTotalSpace()));
        }
        var fileListRequest = new ListOfSharedFilesResponse(files);
        fileListRequest.serialize();
        //send it
        try {
            socketChannel.write(serializerBuffer.getByteBuffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listeningPort() {
        var port = serializerBuffer.readObject(ListeningPortRequest.creator);
        var peers = manager.getKnownPeers();
        peers.add(new PeerAddress(port.getPort(), socketChannel.socket().getInetAddress().toString()));
        manager.setKnownPeers(peers);
        System.out.println(socketChannel.socket().getPort() + " => " + port.getPort());
    }

}
