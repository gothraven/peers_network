package com.upec.peers.network.utils;

import com.upec.peers.network.objects.PeerAddress;

import java.util.Collection;

public interface ServerListener {

	void listOfKnowPeersChanged(Collection<PeerAddress> peerAddresses);
	void logInformations(String data);

}
