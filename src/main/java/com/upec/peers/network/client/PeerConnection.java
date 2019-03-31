package com.upec.peers.network.client;


import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PeerConnection is a class to handle connection to another peer
 *
 * @version 1.5
 */
public class PeerConnection implements Runnable {

	private PeersConnectingManager context;
	private PeerInput peerInput;
	private PeerOutput peerOutput;
	private String inetAddress;
	private int port;
	private Logger logger;
	private Socket socket;
	private String identifier;

	/**
	 * PeerConnection constructor
	 *
	 * @param inetAddress the url of the peer network to connect
	 * @param port        the port which it listens to connection from
	 */
	PeerConnection(PeersConnectingManager context, String inetAddress, int port, Logger logger) throws IOException {
		this.logger = logger;
		this.context = context;
		this.inetAddress = inetAddress;
		this.port = port;
		this.identifier = inetAddress + ":" + port;
		this.socket = new Socket(inetAddress, port);
	}

	@Override
	public void run() {
		try {

			peerInput = new PeerInput(this, socket.getInputStream());
			peerOutput = new PeerOutput(socket.getOutputStream());
			peerInput.run();
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "PeerConnection IO issue, Disconnected !");
			this.context.terminateConnection(this.identifier);
		}
	}

	void recievedMessage(InformationMessage message) {
		logger.log(Level.INFO, this.identifier + ":" + message.getMessage());
	}

	void recievedListOfPeers(ListOfPeersResponse listOfPeersResponse) {
		this.context.putIntoListOfKnowPeers(listOfPeersResponse.getPeerAddresses());
	}

	void recievedListOfSharedFiles(ListOfSharedFilesResponse listOfSharedFilesResponse) {
		logger.log(Level.INFO, this.identifier + ":\n" + listOfSharedFilesResponse.toString());
	}

	void recievedSharedFileFragment(SharedFileFragmentResponse sharedFileFragmentResponse) {
		String fileName = sharedFileFragmentResponse.getFilename();
		long size = sharedFileFragmentResponse.getSize();
		long offset = sharedFileFragmentResponse.getOffset();
		int length = sharedFileFragmentResponse.getLength();
		ByteBuffer blob = sharedFileFragmentResponse.getBlob();

		try {
			this.context.putFragmentInFile(fileName, size, offset, length, blob);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}

		if (offset + length == size) {
			logger.log(Level.INFO, "Download finished " + fileName);
		} else {
			long sizeLeft = size - (offset + length);
			offset += length;
			length = 65536 <= sizeLeft ? 65536 : (int) sizeLeft;
			var command = new SharedFileFragmentRequest(fileName, size, offset, length);
			try {
				System.out.println("==>>" + command);
				peerOutput.sendCommand(command);
			} catch (IOException  e) {
				e.printStackTrace();
			}
		}
	}

	void recievedListOfPeersRequest() {
		try {
			this.peerOutput.sendCommand(new ListOfPeersResponse(new ArrayList<>()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void recievedListOfSharedFilesRequest() {
		try {
			this.peerOutput.sendCommand(new ListOfSharedFilesResponse(new ArrayList<>()));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void requestListOfPeers() {
		try {
			this.peerOutput.sendCommand(new ListOfPeersRequest());
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void requestListOfSharedFiles() {
		try {
			this.peerOutput.sendCommand(new ListOfSharedFilesRequest());
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			this.context.terminateConnection(this.identifier);
		}
	}

	void sendInformationMessage(String message) {
		try {
			this.peerOutput.sendCommand(new InformationMessage(message));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			this.context.terminateConnection(this.identifier);
		}
	}

	void sendListeningPort(int port) {
		try {
			this.peerOutput.sendCommand(new ListeningPort(port));
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void downloadAFile(String fileName, long size) {
		int length = 65536 <= size ? 65536 : (int) size;
		var command = new SharedFileFragmentRequest(fileName, size, 0, length);
		System.out.println("First Command Send==>>" + command);
		try {
			peerOutput.sendCommand(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void terminate() {
		this.peerInput.terminate();
	}
}


