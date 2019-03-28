package com.upec.peers.network.client;


import java.io.IOException;
import java.net.Socket;

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
	private Socket socket;

	/**
	 * PeerConnection constructor
	 *
	 * @param inetAddress the url of the peer network to connect
	 * @param port        the port which it listens to connection from
	 */
	public PeerConnection(PeersConnectingManager context, String inetAddress, int port) throws IOException {
		this.context = context;
		this.inetAddress = inetAddress;
		this.port = port;
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
			//todo end and update connecting list
		}
	}

	public void sendCommand(/*Command command*/) {
		try {
			this.peerOutput.someCommande();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void terminate() {
//		this.running = false;
	}
}
