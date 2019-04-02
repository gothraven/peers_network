package com.upec.peers.network.client;


import com.upec.peers.network.nio.Bytes;
import com.upec.peers.network.nio.SerializerBuffer;
import com.upec.peers.network.protocol.*;
import com.upec.peers.network.utils.Serializable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.SocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PeerConnection is a class to handle connection to another peer
 *
 * @version 3.2
 */
public class PeerConnection implements Runnable {

	private static final int MAX_BUFF_SIZE = 65536;

	private PeersConnectingManager context;
	private Logger logger;
	private SocketChannel socketChannel;
	private String identifier;
	private volatile boolean running;
	private LinkedList<Byte> dataStock;

	private SerializerBuffer serializerBuffer = new SerializerBuffer(MAX_BUFF_SIZE);


	/**
	 * PeerConnection constructor
	 *
	 * @param inetAddress the url of the peer network to connect
	 * @param port        the port which it listens to connection from
	 */
	PeerConnection(PeersConnectingManager context, String inetAddress, int port, Logger logger) throws IOException {
		this.logger = logger;
		this.context = context;
		this.identifier = inetAddress + ":" + port;
		SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
		this.socketChannel = SocketChannel.open();
		this.socketChannel.connect(socketAddress);
		this.socketChannel.configureBlocking(false);
		this.running = this.socketChannel.isConnected();
		this.dataStock = new LinkedList<>();
	}

	@Override
	public void run() {
		try {
			while (running) {

				// read from the network
				var bytesRead = socketChannel.read(serializerBuffer.getByteBuffer());

				// this means the server has left or shutdown
				if (bytesRead < 0) throw new ServerNotActiveException();

				// if we read nothing and there is nothing to process we ignore the rest
				if (bytesRead == 0 && dataStock.isEmpty()) continue;

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

					// get the command ID
					byte commandId = tsb.readByte();

					switch (commandId) {
						case InformationMessage.ID:
							var informationMessage = tsb.readObject(InformationMessage.creator);
							this.recievedMessage(informationMessage);
							break;
						case ListOfPeersRequest.ID:
							tsb.readObject(ListOfPeersRequest.creator);
							this.recievedListOfPeersRequest();
							break;
						case ListOfPeersResponse.ID:
							var listOfPeersResponse = tsb.readObject(ListOfPeersResponse.creator);
							this.recievedListOfPeers(listOfPeersResponse);
							break;
						case ListOfSharedFilesRequest.ID:
							tsb.readObject(ListOfSharedFilesRequest.creator);
							this.recievedListOfSharedFilesRequest();
							break;
						case ListOfSharedFilesResponse.ID:
							var listOfSharedFilesResponse = tsb.readObject(ListOfSharedFilesResponse.creator);
							this.recievedListOfSharedFiles(listOfSharedFilesResponse);
							break;
						case SharedFileFragmentResponse.ID:
							var sharedFileFragmentResponse = tsb.readObject(SharedFileFragmentResponse.creator);
							this.recievedSharedFileFragment(sharedFileFragmentResponse);
							break;
						default:
							if (commandId >= ((byte) 64) || commandId <= ((byte) 128))
								tsb.ignoreObject(Extentions.consumer);
							else
								throw new ProtocolException("Command not right");
					}
					dataStock.clear();
					while (tsb.hasRemaining()) {
						dataStock.add(tsb.readByte());
					}
					tsb.clear();
				} catch (BufferUnderflowException ignored) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "PeerConnection IO issue, Disconnected !");
			this.context.terminateConnection(this.identifier);
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Peer Server is down, Disconnected !");
			this.context.terminateConnection(this.identifier);
		}
	}

	private void recievedMessage(InformationMessage message) {
		logger.log(Level.INFO, this.identifier + ":" + message.getMessage());
	}

	private void recievedListOfPeers(ListOfPeersResponse listOfPeersResponse) {
		this.context.putIntoListOfKnowPeers(listOfPeersResponse.getPeerAddresses());
	}

	private void recievedListOfSharedFiles(ListOfSharedFilesResponse listOfSharedFilesResponse) {
		logger.log(Level.INFO, this.identifier + ":\n" + listOfSharedFilesResponse.toString());
	}

	private void recievedSharedFileFragment(SharedFileFragmentResponse sharedFileFragmentResponse) {
		String fileName = sharedFileFragmentResponse.getFilename();
		long size = sharedFileFragmentResponse.getSize();
		long offset = sharedFileFragmentResponse.getOffset();
		int length = sharedFileFragmentResponse.getLength();
		ByteBuffer blob = sharedFileFragmentResponse.getBlob();

		try {
			this.context.putFragmentInFile(fileName, blob);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
		if (offset + length == size)
			logger.log(Level.INFO, "Download finished " + fileName + " !!!");
		else {
			long sizeLeft = size - (offset + length);
			offset += length;
			length = 65536 <= sizeLeft ? 65536 : (int) sizeLeft;
			var command = new SharedFileFragmentRequest(fileName, size, offset, length);
			try {
				this.sendCommand(command);
			} catch (IOException  e) {
				e.printStackTrace();
				logger.log(Level.WARNING, e.getMessage());
				terminate();
			}
		}
	}

	private void recievedListOfPeersRequest() {
		try {
			this.sendCommand(new ListOfPeersResponse(new ArrayList<>()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	private void recievedListOfSharedFilesRequest() {
		try {
			this.sendCommand(new ListOfSharedFilesResponse(new ArrayList<>()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void requestListOfPeers() {
		try {
			this.sendCommand(new ListOfPeersRequest());
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void requestListOfSharedFiles() {
		try {
			this.sendCommand(new ListOfSharedFilesRequest());
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			this.context.terminateConnection(this.identifier);
		}
	}

	void sendInformationMessage(String message) {
		try {
			this.sendCommand(new InformationMessage(message));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			this.context.terminateConnection(this.identifier);
		}
	}

	void sendListeningPort(int port) {
		try {
			this.sendCommand(new ListeningPort(port));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void downloadAFile(String fileName, long size) {
		int length = 65536 <= size ? 65536 : (int) size;
		var command = new SharedFileFragmentRequest(fileName, size, 0, length);
		try {
			this.sendCommand(command);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	private void sendCommand(Serializable command) throws IOException {
		var buffer = command.serialize();
		buffer.flip();
		socketChannel.write(buffer.getByteBuffer());
	}

	void terminate() {
		this.running = false;
	}
}


