package com.upec.peers.network.protocol;

import com.upec.peers.network.nio.SerializerBuffer;

import java.util.function.Consumer;

public class Extentions {
	public static final Consumer<SerializerBuffer> consumer = serializerBuffer -> {
		var length = serializerBuffer.readInt();
		serializerBuffer.readToByteBuffer(length);
	};
}
