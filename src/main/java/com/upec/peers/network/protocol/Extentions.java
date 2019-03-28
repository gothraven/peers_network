package com.upec.peers.network.protocol;

import com.upec.peers.network.utils.ObjectConsumer;

public class Extentions {
	public static ObjectConsumer consumer = serializerBuffer -> {
		var length = serializerBuffer.readInt();
		serializerBuffer.readToByteBuffer(length);
	};
}
