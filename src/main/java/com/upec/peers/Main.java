package com.upec.peers;


import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.NetworkCore;

public class Main {

	public static void main(String[] args) throws Exception {
		var serverPort = 2020;
		var core = new NetworkCore(serverPort);
		var peerInterface = new NetworkInterface(core);
		core.regesterObserver(peerInterface);
		core.execute();
	}
}
