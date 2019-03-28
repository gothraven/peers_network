package com.upec.peers.network;

import com.upec.peers.network.server.PeersConnectedManager;
import com.upec.peers.network.client.PeersConnectingManager;
import com.upec.peers.network.utils.NetworkObservable;
import com.upec.peers.network.utils.NetworkObserver;

import java.io.IOException;

public class Core implements NetworkObservable {

	private PeersConnectedManager peersConnectedManager;
	private PeersConnectingManager peersConnectingManager;
	private NetworkObserver networkObserver;

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
			e.printStackTrace();
		}
	}

	@Override
	public void regesterObserver(NetworkObserver observer) {
		this.networkObserver = observer;
	}
}
