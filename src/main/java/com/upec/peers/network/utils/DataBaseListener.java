package com.upec.peers.network.utils;

import com.upec.peers.network.objects.PeerAddress;

import java.util.Collection;

public interface DataBaseListener {
	void listOfKnowPeersChanged(Collection<PeerAddress> peerAddresses);
}
