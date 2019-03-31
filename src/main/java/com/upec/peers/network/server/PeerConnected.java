package com.upec.peers.network.server;

import com.google.common.primitives.Bytes;
import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;
import com.upec.peers.network.utils.ClientNotActive;

import java.io.IOException;
import java.net.ProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.ServerException;
import java.util.LinkedList;


class PeerConnected {

    private static final int BUFFER_SIZE = 1024;
    private PeersConnectedManager manager;
    private SocketChannel socketChannel;
    private SerializerBuffer serializerBuffer;
    private LinkedList<Byte> dataStock;



    PeerConnected(SocketChannel sc, PeersConnectedManager manager) {
        this.manager = manager;
        this.socketChannel = sc;
        this.serializerBuffer = new SerializerBuffer(ByteBuffer.allocate(BUFFER_SIZE));
        this.dataStock = new LinkedList<>();
    }

    void read() throws IOException, ClientNotActive {

        // read from the network
        var bytesRead = socketChannel.read(serializerBuffer.getByteBuffer());

        // this means the server has left or shutdown
        if (bytesRead < 0) throw new ClientNotActive();

        // if we read nothing and there is nothing to process we ignore the rest
        if (bytesRead == 0 && dataStock.isEmpty()) return;

        while (bytesRead > 0) {

            // flipping the buffer to read from it in case we wrote somethings in
            serializerBuffer.flip();

            while (serializerBuffer.hasRemaining()) dataStock.add(serializerBuffer.readByte());

            // clear the network buffer for the next turn
            serializerBuffer.clear();

            bytesRead = socketChannel.read(serializerBuffer.getByteBuffer());
        }

        // create a temporary buffer from the data stock to serialize it
        var tsb = new SerializerBuffer(Bytes.toArray(dataStock));

        try {

            byte id = tsb.readByte();

            switch (id) {
                case InformationMessage.ID:
                    var informationMessage = tsb.readObject(InformationMessage.creator);
                    this.manager.recievedMessage(informationMessage, this);
                    break;
                case ListeningPort.ID:
                    var listeningPort = tsb.readObject(ListeningPort.creator);
                    this.manager.recievedListeningPort(listeningPort, this);
                    break;
                case ListOfPeersRequest.ID:
                    tsb.readObject(ListOfPeersRequest.creator);
                    this.manager.recievedListOfPeersRequest(this);
                    break;
                case ListOfSharedFilesRequest.ID:
                    tsb.readObject(ListOfSharedFilesRequest.creator);
                    this.manager.recievedListOfSharedFilesRequest(this);
                    break;
                case SharedFileFragmentRequest.ID:
                    var sharedFileFragmentRequest = tsb.readObject(SharedFileFragmentRequest.creator);
                    this.manager.recievedSharedFileFragmentRequest(sharedFileFragmentRequest, this);
                    break;
                default:
                    if (id >= ((byte)64) || id <= ((byte)128))
                        tsb.ignoreObject(Extentions.consumer);
                    else
                        throw new ProtocolException("Command is not right");
            }

            dataStock.clear();
            while (tsb.hasRemaining()) dataStock.add(tsb.readByte());
            tsb.clear();

        } catch (BufferUnderflowException ignored) {
        } catch (ProtocolException | ServerException e) {
            e.printStackTrace();
			serverError(e.getMessage());
        }
    }

    private void serverError(String wrong_command) throws IOException {
        var error_message = new InformationMessage(wrong_command);
        var buffer = error_message.serialize();
        buffer.flip();
        socketChannel.write(buffer.getByteBuffer());
    }

    SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
