package com.upec.peers.network.utils;

import java.io.Serializable;

public interface NetworkObservable extends Serializable {
	void regesterObserver(NetworkObserver observer);
}
