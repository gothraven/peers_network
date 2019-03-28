package com.upec.peers.network.client;


import com.upec.peers.network.protocol.*;

import java.io.IOException;
import java.net.Socket;
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
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	void recievedMessage(InformationMessage message) {
		logger.log(Level.INFO, this.identifier + ":" + message.getMessage());
	}

	void recievedListOfPeers(ListOfPeersResponse listOfPeersResponse) {
		logger.log(Level.INFO, this.identifier + ":\n" + listOfPeersResponse.toString());
	}

	void recievedListOfSharedFiles(ListOfSharedFilesResponse listOfSharedFilesResponse) {
		logger.log(Level.INFO, this.identifier + ":\n" + listOfSharedFilesResponse.toString());
	}

	public void requestListOfPeers() {
		try {
			this.peerOutput.sendCommand(new ListOfPeersRequest());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	public void requestListOfSharedFiles() {
		try {
			this.peerOutput.sendCommand(new ListOfSharedFilesRequest());
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	public void sendInformationMessage(String message) {
		try {
			this.peerOutput.sendCommand(new InformationMessage(message));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	public void sendListeningPort(int port) {
		try {
			this.peerOutput.sendCommand(new ListeningPort(port));
		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage());
			terminate();
		}
	}

	public void terminate() {
		this.peerInput.terminate();
		this.context.terminateConnection(this.identifier);
	}

	public void recievedSharedFIleFragment(SharedFileFragmentResponse sharedFileFragmentResponse) {

	}

}


