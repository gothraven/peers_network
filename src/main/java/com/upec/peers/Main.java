package com.upec.peers;


import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.nio.SerializerBuffer;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.MessageFormat;

public class Main {

	public static void main(String[] args) throws Exception {
		var serverPort = 2020;
		var core = new NetworkCore(serverPort);
		var peerInterface = new NetworkInterface(core);
		core.regesterObserver(peerInterface);
		core.execute();
//		core.instantiateConnection("localhost", 2020);
		core.instantiateConnection("prog-reseau-m1.lacl.fr", 5486);

//		String fname = "id_emojie_key.pub";
//		var bb = getBytesFromFile(fname, 24, 20, 51);
//		var s = Charset.defaultCharset().decode(bb);
//		System.out.println(s);

	}

	public static ByteBuffer getBytesFromFile(String file, long size, int start, int length) throws IOException {
		byte[] bytes = new byte[length];
		URL sharedFold = ClassLoader.getSystemResource("shared/" + file);
		var inputStream = sharedFold.openStream();
		inputStream.skip(start);
		inputStream.read(bytes);
		return ByteBuffer.wrap(bytes);
	}

	public static void putBytesInFile(String file, long size, long start, int length, ByteBuffer Blob) {

	}
}
