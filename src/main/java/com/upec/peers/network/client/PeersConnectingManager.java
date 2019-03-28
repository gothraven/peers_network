package com.upec.peers.network.client;

import com.upec.peers.network.Core;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class PeersConnectingManager {

	private Core conext;
	private HashMap<String, PeerConnection> connections;

	public PeersConnectingManager(Core conext) {
		this.conext = conext;
		this.connections = new HashMap<>();
	}

	public void connectTo(String inetAddress, int port) throws IOException {
		var id = inetAddress + ":" + port;
		var connection = new PeerConnection(this, inetAddress, port);
		new Thread(connection).start();
		this.connections.put(id, connection);
	}

	public Collection<String> getConnectionsIds() {
		return connections.keySet();
	}
}
