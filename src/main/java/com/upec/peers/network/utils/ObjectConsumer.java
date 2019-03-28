package com.upec.peers.network.utils;

import com.upec.peers.network.nio.SerializerBuffer;

public interface ObjectConsumer {
	void consume(SerializerBuffer serializerBuffer);
}
