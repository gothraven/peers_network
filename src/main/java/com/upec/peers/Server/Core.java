package com.upec.peers.Server;

import java.io.IOException;

public class Core {

	private PeersConnectedManager peersConnectedManager;
	private PeersConnectingManager peersConnectingManager;

	public Core(int listeningPort) throws IOException {
		this.peersConnectedManager = new PeersConnectedManager(listeningPort);
		this.peersConnectingManager = new PeersConnectingManager(this);
	}

	public void execute() {
		new Thread(this.peersConnectedManager).start();
	}

	public void instantiateConnection(String inetAddress, int port) {
		try {
			this.peersConnectingManager.connectTo(inetAddress, port);
		} catch (IOException e) {
			// connection problem
			e.printStackTrace();
		}
	}
}
