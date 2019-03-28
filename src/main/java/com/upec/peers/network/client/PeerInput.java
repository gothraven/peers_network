package com.upec.peers.network.client;

import com.google.common.primitives.Bytes;
import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.rmi.ServerError;
import java.rmi.server.ServerNotActiveException;
import java.util.LinkedList;

class PeerInput {

	private static final int MAX_BUFF_SIZE = 1024;

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

			sb.getByteBuffer().clear();
			sb.flip();
			bytesRead = in.read(sb.getByteBuffer());

			if (bytesRead < 0)
				throw new ServerNotActiveException();

			if (bytesRead == 0 && dataStock.isEmpty())
				continue;

			while (bytesRead > 0) {
				sb.flip();
				while (sb.hasRemaining()) {
					dataStock.add(sb.readByte());
				}
				sb.clear();
				bytesRead = in.read(sb.getByteBuffer());
			}

			var tsb = new SerializerBuffer(Bytes.toArray(dataStock));

			try {
				byte commandId = tsb.readByte();
				switch (commandId) {
					case InformationMessage.ID:
						var informationMessage = tsb.readObject(InformationMessage.creator);
						this.context.recievedMessage(informationMessage);
						break;
					case ListOfPeersResponse.ID:
						var listOfPeersResponse = tsb.readObject(ListOfPeersResponse.creator);
						this.context.recievedListOfPeers(listOfPeersResponse);
					case ListOfSharedFilesResponse.ID:
						var listOfSharedFilesResponse = tsb.readObject(ListOfSharedFilesResponse.creator);
						this.context.recievedListOfSharedFiles(listOfSharedFilesResponse);
					case SharedFileFragmentResponse.ID:
						var sharedFileFragmentResponse = tsb.readObject(SharedFileFragmentResponse.creator);
						this.context.recievedSharedFIleFragment(sharedFileFragmentResponse);
					default:
						if (commandId >= 0x64 || commandId <= 0x128)
							tsb.ignoreObject(Extentions.consumer);
						else
							throw new ProtocolException("Command not right");
						break;
				}
				dataStock.clear();
				while (tsb.hasRemaining()) {
					dataStock.add(tsb.readByte());
				}
			} catch (BufferUnderflowException ignored) {}
		}
	}

	void terminate() {
		this.running = false;
	}
}

