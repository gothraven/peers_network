package com.upec.peers.network;

import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.client.PeersConnectingManager;
import com.upec.peers.network.database.DataBase;
import com.upec.peers.network.server.PeersConnectedManager;
import com.upec.peers.network.utils.NetworkObservable;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkCore implements NetworkObservable {

	private int serverPort;
	private PeersConnectedManager peersConnectedManager;
	private PeersConnectingManager peersConnectingManager;
	private DataBase dataBase;
	private Logger logger = Logger.getLogger("NetworkLogger");

	public NetworkCore(int serverPort) throws IOException {
		this.serverPort = serverPort;
		this.dataBase = new DataBase();
		this.peersConnectedManager = new PeersConnectedManager(serverPort, dataBase, logger);
		this.peersConnectingManager = new PeersConnectingManager(dataBase, logger);
	}

	@Override
	public void regesterObserver(NetworkInterface observer) {
		this.logger.addHandler(observer.getLogHandler());
		this.dataBase.regesterListener(observer);
		this.peersConnectingManager.regesterListener(observer);
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

	public void sendMessage(String identifier, String message) {
		this.peersConnectingManager.sendMessageTo(identifier, message);
	}

	public void askForListOfSharedFiles(String identifier) {
		this.peersConnectingManager.askForListOfSharedFiles(identifier);
	}

	public void sendListeningPort(String identifier) {
		this.peersConnectingManager.sendListeningPort(identifier, serverPort);
	}

	public void askForListOfPeers(String identifier) {
		this.peersConnectingManager.askForListOfPeers(identifier);
	}

	public void downloadAFile(String identifier, String fileName, long size) {
		this.peersConnectingManager.downloadAFile(identifier, fileName, size);
	}
}
