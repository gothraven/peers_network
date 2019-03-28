package com.upec.peers.network.client;

import com.upec.peers.network.protocol.ListOfSharedFilesRequest;
import com.upec.peers.network.protocol.SharedFileFragmentRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class PeerOutput {

	private WritableByteChannel out;

	PeerOutput(OutputStream outputStream) {
		this.out = Channels.newChannel(outputStream);
	}

	void someCommande() throws IOException {
		var command = new ListOfSharedFilesRequest();
//		var command = new SharedFileFragmentRequest("amrane.txt", 22, 0, 22);
		var buffer = command.serialize();
		buffer.flip();
		out.write(buffer.getByteBuffer());
	}

}
