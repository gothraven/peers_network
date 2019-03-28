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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PeerConnected {

    final private int BUFFER_SIZE = 512;
    private SocketChannel socketChannel;
    private SerializerBuffer serializerBuffer;
    private List<PeerAddress> knownPeers;


    public PeerConnected(SocketChannel sc) {
        this.socketChannel = sc;
        this.knownPeers = Collections.synchronizedList(new ArrayList<>());
        // UN proble d'intialisation
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
        if (MessageRequest.ID == id) {
            var message = serializerBuffer.readObject(MessageRequest.creator);
            System.out.println(socketChannel.socket().getPort() + " => " + message.getMessage());
        } else if (ListeningPortRequest.ID == id) listeningPort();
        else if (ListOfPeersRequest.ID == id) peersList();
        else if (ListOfSharedFilesRequest.ID == id) fileList();
        else if (SharedFileFragmentRequest.ID == id) sharedFragmentFile();
        else if (id >= 64 && id <= 128) extensions();
        else {
            System.out.println("Error");
            System.out.println("close Connexion");
        }
        //writeData(MessageCommand.serialize("Hello back\n"), socketChannel);
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
        System.out.println("Message d'extensions ignore");
    }

    public void peersList() {
        var response = new ListOfPeersResponse(knownPeers);
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
        knownPeers.add(new PeerAddress(port.getPort(), socketChannel.socket().getInetAddress().toString()));
        System.out.println(socketChannel.socket().getPort() + " => " + port.getPort());
    }

}
