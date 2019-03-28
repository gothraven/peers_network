package com.upec.peers.network;

import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.server.PeersConnectedManager;
import com.upec.peers.network.client.PeersConnectingManager;
import com.upec.peers.network.utils.NetworkObservable;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkCore implements NetworkObservable {

	private PeersConnectedManager peersConnectedManager;
	private PeersConnectingManager peersConnectingManager;
	private NetworkInterface networkObserver;
	private Logger logger = Logger.getLogger("NetworkLogger");

	public NetworkCore(int serverPort) throws IOException {
		this.peersConnectedManager = new PeersConnectedManager(serverPort, logger);
		this.peersConnectingManager = new PeersConnectingManager(this, logger);
		this.execute();
	}

	public void execute() {
		new Thread(this.peersConnectedManager).start();
	}

	public void shutDown() {
		this.peersConnectedManager.terminate();
		this.peersConnectingManager.terminateConnections();
	}

	public void instantiateConnection(String inetAddress, int port) {
		try {
			this.peersConnectingManager.connectTo(inetAddress, port);
			logger.log(Level.INFO, "Connected to " + inetAddress + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "Failed to connect to " + inetAddress + ":" + port + "\n");
		}
	}

	public void terminateConnection(String identifier) {
		this.peersConnectingManager.terminateConnection(identifier);
		logger.log(Level.INFO, "Connection closed with " + identifier);
	}

	@Override
	public void regesterObserver(NetworkInterface observer) {
		this.networkObserver = observer;
		this.logger.addHandler(observer.getLogHandler());
		this.peersConnectedManager.regesterListener(observer);
		this.peersConnectingManager.regesterListener(observer);
	}
}
