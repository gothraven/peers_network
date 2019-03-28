package com.upec.peers.network.utils;

import com.upec.peers.network.objects.PeerAddress;

import java.util.List;

public interface ServerListener {

	void listOfKnowPeersChanged(List<PeerAddress> peerAddresses);
	void logInformations(String data);

}
