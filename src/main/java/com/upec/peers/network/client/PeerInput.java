package com.upec.peers.network.client;

import com.google.common.primitives.Bytes;
import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.rmi.server.ServerNotActiveException;
import java.util.LinkedList;

class PeerInput {

	private static final int MAX_BUFF_SIZE = 512;

	private PeerConnection context;
	private ReadableByteChannel in;
	private SerializerBuffer sb = new SerializerBuffer(MAX_BUFF_SIZE);
	private boolean running;

	PeerInput(PeerConnection context, InputStream inputStream) {
		this.context = context;
		this.in = Channels.newChannel(inputStream);
		this.running = true;
	}

	void run() throws IOException, ServerNotActiveException {
		var dataStock = new LinkedList<Byte>();
		var bytesRead = 0;

		while (running) {
			bytesRead = in.read(sb.getByteBuffer());

			if (bytesRead < 0)
				throw new ServerNotActiveException();

			if (bytesRead == 0 && dataStock.isEmpty())
				continue;

			sb.flip();
			while (sb.hasRemaining()) {
				dataStock.add(sb.readByte());
			}
			sb.flip();
			sb.clear();

			var tsb = new SerializerBuffer(Bytes.toArray(dataStock));

			try {
				byte commandId = tsb.readByte();

				switch (commandId) {
					case InformationMessage.ID:
						var informationMessage = tsb.readObject(InformationMessage.creator);
						this.context.recievedMessage(informationMessage);
						break;
					case ListOfPeersRequest.ID:
						tsb.readObject(ListOfPeersRequest.creator);
						this.context.recievedListOfPeersRequest();
						break;
					case ListOfPeersResponse.ID:
						var listOfPeersResponse = tsb.readObject(ListOfPeersResponse.creator);
						this.context.recievedListOfPeers(listOfPeersResponse);
						break;
					case ListOfSharedFilesRequest.ID:
						tsb.readObject(ListOfSharedFilesRequest.creator);
						this.context.recievedListOfSharedFilesRequest();
						break;
					case ListOfSharedFilesResponse.ID:
						var listOfSharedFilesResponse = tsb.readObject(ListOfSharedFilesResponse.creator);
						this.context.recievedListOfSharedFiles(listOfSharedFilesResponse);
						break;
					case SharedFileFragmentResponse.ID:
						var sharedFileFragmentResponse = tsb.readObject(SharedFileFragmentResponse.creator);
						this.context.recievedSharedFIleFragment(sharedFileFragmentResponse);
						break;
					default:
						if (commandId >= 0x064 || commandId <= 0x0128)
							tsb.ignoreObject(Extentions.consumer);
						else
							throw new ProtocolException("Command not right");
				}
				dataStock.clear();
				while (tsb.hasRemaining()) {
					dataStock.add(tsb.readByte());
				}
				tsb.clear();
			} catch (BufferUnderflowException ignored) {}
		}
	}

	void terminate() {
		this.running = false;
	}
}

