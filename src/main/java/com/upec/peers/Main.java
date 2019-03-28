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
//		core.instantiateConnection("localhost", 2020);
		core.instantiateConnection("prog-reseau-m1.lacl.fr", 5486);
	}
}
