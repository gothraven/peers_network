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

	private static final int MAX_BUFF_SIZE = 65536;

	private PeerConnection context;
	private ReadableByteChannel in;
	private SerializerBuffer sb = new SerializerBuffer(MAX_BUFF_SIZE);
	private volatile boolean running;

	PeerInput(PeerConnection context, InputStream inputStream) {
		this.context = context;
		this.in = Channels.newChannel(inputStream);
		this.running = true;
	}

	void run() throws IOException, ServerNotActiveException {

		// contains the not processed data
		var dataStock = new LinkedList<Byte>();

		while (running) {

			// read from the network
			var bytesRead = in.read(sb.getByteBuffer());

			// this means the server has left or shutdown
			if (bytesRead < 0) throw new ServerNotActiveException();

			// if we read nothing and there is nothing to process we ignore the rest
			if (bytesRead == 0 && dataStock.isEmpty()) continue;

			// flipping the buffer to read from it in case we wrote somethings in
			sb.flip();

			// copy the bites from the buffer to the data stock
			while (sb.hasRemaining()) dataStock.add(sb.readByte());

			// clear the network buffer for the next turn
			sb.clear();

			// create a temporary buffer from the data stock to serialize it
			var tsb = new SerializerBuffer(Bytes.toArray(dataStock));

			try {

				// get the command ID
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
						this.context.recievedSharedFileFragment(sharedFileFragmentResponse);
						break;
					default:
						if (commandId >= ((byte)64) || commandId <= ((byte)128))
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

