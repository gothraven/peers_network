package com.upec.peers;

import com.upec.peers.Server.ServerCore;

public class Main {

	public static void main(String[] args) {
		var serverCore = new ServerCore("gitlabe", 80);
		serverCore.start();
	}
}
