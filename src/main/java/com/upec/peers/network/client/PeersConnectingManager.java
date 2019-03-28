package com.upec.peers.network.client;

import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.utils.ClientListener;
import com.upec.peers.network.utils.NetworkObserver;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class PeersConnectingManager {

	private NetworkCore conext;
	private HashMap<String, PeerConnection> connections;
	private ClientListener clientListener;

	public PeersConnectingManager(NetworkCore conext) {
		this.conext = conext;
		this.connections = new HashMap<>();
	}

	public void connectTo(String inetAddress, int port) throws IOException {
		var id = inetAddress + ":" + port;
		var connection = new PeerConnection(this, inetAddress, port);
		new Thread(connection).start();
		this.connections.put(id, connection);
		this.clientListener.listOfConnectionsChanged(getConnectionsIds());
	}

	public Collection<String> getConnectionsIds() {
		return connections.keySet();
	}

	public void regesterListener(ClientListener clientListener) {
		this.clientListener = clientListener;
	}
}
