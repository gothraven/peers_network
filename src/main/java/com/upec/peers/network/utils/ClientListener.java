package com.upec.peers.network.utils;

import java.util.Collection;

public interface ClientListener {

	void listOfConnectionsChanged(Collection<String> connections);

}