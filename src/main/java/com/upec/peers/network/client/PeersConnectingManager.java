package com.upec.peers.network.client;

import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.database.DataBase;
import com.upec.peers.network.objects.PeerAddress;
import com.upec.peers.network.utils.ClientListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

public class PeersConnectingManager {

	private NetworkCore conext;
	private HashMap<String, PeerConnection> connections;
	private ClientListener clientListener;
	private DataBase dataBase;
	private Logger logger;

	public PeersConnectingManager(NetworkCore conext, DataBase dataBase, Logger logger) {
		this.conext = conext;
		this.logger = logger;
		this.dataBase = dataBase;
		this.connections = new HashMap<>();
	}

	public void connectTo(String inetAddress, int port) throws IOException {
		var id = inetAddress + ":" + port;
		var connection = new PeerConnection(this, inetAddress, port, logger);
		new Thread(connection).start();
		this.connections.put(id, connection);
		this.clientListener.listOfConnectionsChanged(getConnectionsIds());
	}

	public void sendMessageTo(String identifier, String message) {
		this.connections.get(identifier).sendInformationMessage(message);
	}

	private Collection<String> getConnectionsIds() {
		return connections.keySet();
	}

	public void regesterListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}

	public void terminateConnection(String identifier) {
		this.connections.get(identifier).terminate();
		this.connections.remove(identifier);
		this.clientListener.listOfConnectionsChanged(getConnectionsIds());
	}

	public void terminateConnections() {
		this.connections.forEach((identifier, peerConnection) -> peerConnection.terminate());
	}

	public void askForListOfSharedFiles(String identifier) {
		var connection = this.connections.get(identifier);
		if (connection != null) {
			connection.requestListOfSharedFiles();
		}
	}

	public void sendListeningPort(String identifier, int port) {
		var connection = this.connections.get(identifier);
		if (connection != null) {
			connection.sendListeningPort(port);
		}
	}

	public void askForListOfPeers(String identifier) {
		var connection = this.connections.get(identifier);
		if (connection != null) {
			connection.requestListOfPeers();
		}
	}

	public void downloadAFile(String identifier, String fileName, long size) {
		var connection = this.connections.get(identifier);
		if (connection != null) {
			connection.downloadAFile(fileName, size);
		}
	}

	void putFragmentInFile(String fileName, long size, long offset, int length, ByteBuffer blob) throws IOException, URISyntaxException {
		this.dataBase.putFileFragment(fileName, size, offset, length, blob);
	}

	void putIntoListOfKnowPeers(Collection<PeerAddress> peerAddresses) {
		this.dataBase.addKnowPeers(peerAddresses);
	}
}
