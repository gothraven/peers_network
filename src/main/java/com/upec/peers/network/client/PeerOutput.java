package com.upec.peers.network.client;

import com.upec.peers.network.utils.Serializable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

class PeerOutput {

	private WritableByteChannel out;

	PeerOutput(OutputStream outputStream) {
		this.out = Channels.newChannel(outputStream);
	}

	void sendCommand(Serializable command) throws IOException {
		var buffer = command.serialize();
//		System.out.println("sending " + command.getClass());
		buffer.flip();
		out.write(buffer.getByteBuffer());

	}
}
