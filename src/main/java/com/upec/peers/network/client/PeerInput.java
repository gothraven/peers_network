package com.upec.peers.network.client;

import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class PeerInput {

	private static final int MAX_BUFF_SIZE = 512;

	private ReadableByteChannel in;
	private SerializerBuffer serializerBuffer = new SerializerBuffer(MAX_BUFF_SIZE);
	private boolean running;

	PeerInput(PeerConnection context, InputStream inputStream) {
		this.in = Channels.newChannel(inputStream);
		this.running = true;
	}

	public void run() throws IOException {
		while (running) {
			serializerBuffer.getByteBuffer().clear();
			in.read(serializerBuffer.getByteBuffer());
			serializerBuffer.flip();
			var commandId = serializerBuffer.readByte();
			switch (commandId) {
				case InformationMessage.ID:
					var informationMessage = serializerBuffer.readObject(InformationMessage.creator);
					System.out.println(informationMessage);
					break;
				case ListOfPeersResponse.ID:
					var listOfPeersResponse = serializerBuffer.readObject(ListOfPeersResponse.creator);
					System.out.println(listOfPeersResponse.getPeerAddresses());
				case ListOfSharedFilesResponse.ID:
					var listOfSharedFilesResponse = serializerBuffer.readObject(ListOfSharedFilesResponse.creator);
					System.out.println(listOfSharedFilesResponse.getListOfSharedFiles());
				case SharedFileFragmentResponse.ID:
					var sharedFileFragmentResponse = serializerBuffer.readObject(SharedFileFragmentResponse.creator);
					System.out.println(sharedFileFragmentResponse);
				default:
					System.out.println("wrong commande");
					break;
			}
		}
	}

	public void terminate() {
		this.running = false;
	}
}
