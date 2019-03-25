package com.upec.peers;


import com.upec.peers.Server.Core;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		// to test the server core
		try {
			var core = new Core(2020);
			core.execute();
//			core.instantiateConnection("localhost", 2020);
			core.instantiateConnection("prog-reseau-m1.lacl.fr", 5486);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//lancer l'interface
//		PeerInterface pi = new PeerInterface();
//		pi.setVisible(true);
	}
}
