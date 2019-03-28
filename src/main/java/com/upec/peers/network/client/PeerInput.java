package com.upec.peers.network.client;

import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

class PeerInput {

	private static final int MAX_BUFF_SIZE = 512;

	private PeerConnection context;
	private ReadableByteChannel in;
	private SerializerBuffer serializerBuffer = new SerializerBuffer(MAX_BUFF_SIZE);
	private boolean running;

	PeerInput(PeerConnection context, InputStream inputStream) {
		this.context = context;
		this.in = Channels.newChannel(inputStream);
		this.running = true;
	}

	void run() throws IOException {
		while (running) {
			serializerBuffer.getByteBuffer().clear();
			in.read(serializerBuffer.getByteBuffer());
			serializerBuffer.flip();
			byte commandId = serializerBuffer.readByte();
			switch (commandId) {
				case InformationMessage.ID:
					var informationMessage = serializerBuffer.readObject(InformationMessage.creator);
					this.context.recievedMessage(informationMessage);
					break;
				case ListOfPeersResponse.ID:
					var listOfPeersResponse = serializerBuffer.readObject(ListOfPeersResponse.creator);
					this.context.recievedListOfPeers(listOfPeersResponse);
				case ListOfSharedFilesResponse.ID:
					var listOfSharedFilesResponse = serializerBuffer.readObject(ListOfSharedFilesResponse.creator);
					this.context.recievedListOfSharedFiles(listOfSharedFilesResponse);
				case SharedFileFragmentResponse.ID:
					var sharedFileFragmentResponse = serializerBuffer.readObject(SharedFileFragmentResponse.creator);
					this.context.recievedSharedFIleFragment(sharedFileFragmentResponse);
				default:
					if (commandId >= 0x64 || commandId <= 0x128)
						serializerBuffer.ignoreObject(Extentions.consumer);
					else
						throw new ProtocolException("Command not right");
					break;
			}
		}
	}

	void terminate() {
		this.running = false;
	}
}

