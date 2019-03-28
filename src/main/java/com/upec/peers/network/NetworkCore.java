package com.upec.peers.network;

import com.upec.peers.network.server.PeersConnectedManager;
import com.upec.peers.network.client.PeersConnectingManager;
import com.upec.peers.network.utils.NetworkObservable;
import com.upec.peers.network.utils.NetworkObserver;

import java.io.IOException;

public class NetworkCore implements NetworkObservable {

	private PeersConnectedManager peersConnectedManager;
	private PeersConnectingManager peersConnectingManager;
	private NetworkObserver networkObserver;

	public NetworkCore(int serverPort) throws IOException {
		this.peersConnectedManager = new PeersConnectedManager(serverPort);
		this.peersConnectingManager = new PeersConnectingManager(this);
		this.execute();
	}

	public void execute() {
		new Thread(this.peersConnectedManager).start();
	}

	public void instantiateConnection(String inetAddress, int port) {
		try {
			System.out.printf(inetAddress + ":" + port);
			this.peersConnectingManager.connectTo(inetAddress, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void regesterObserver(NetworkObserver observer) {
		this.networkObserver = observer;
		this.peersConnectedManager.regesterListener(observer);
		this.peersConnectingManager.regesterListener(observer);
	}
}
