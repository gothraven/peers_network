package com.upec.peers.network.utils;

import com.upec.peers.network.objects.PeerAddress;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface NetworkObservable extends Serializable {
	void regesterObserver(NetworkObserver observer);

	void listOfConnectionsChaged(Collection<String> connections);
	void listOfKnowPeersChanged(List<PeerAddress> peerAddresses);
	void logInformations(String data);
}
