package com.upec.peers.network.utils;

import com.upec.peers.interfaces.NetworkInterface;

import java.io.Serializable;

public interface NetworkObservable extends Serializable {
	void regesterObserver(NetworkInterface observer);
}
