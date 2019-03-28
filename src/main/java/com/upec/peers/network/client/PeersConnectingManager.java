package com.upec.peers.network.client;

import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.protocol.InformationMessage;
import com.upec.peers.network.utils.ClientListener;
import com.upec.peers.network.utils.NetworkObserver;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class PeersConnectingManager {

	private NetworkCore conext;
	private HashMap<String, PeerConnection> connections;
	private ClientListener clientListener;
	private Logger logger;

	public PeersConnectingManager(NetworkCore conext, Logger logger) {
		this.conext = conext;
		this.logger = logger;
		this.connections = new HashMap<>();
	}

	public void connectTo(String inetAddress, int port) throws IOException {
		var id = inetAddress + ":" + port;
		var connection = new PeerConnection(this, inetAddress, port, logger);
		new Thread(connection).start();
		this.connections.put(id, connection);
		this.clientListener.listOfConnectionsChanged(getConnectionsIds());
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
		this.connections.forEach((identifier, peerConnection) -> {
			peerConnection.terminate();
		});
	}
}
