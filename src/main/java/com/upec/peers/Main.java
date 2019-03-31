package com.upec.peers;


import com.upec.peers.interfaces.NetworkInterface;
import com.upec.peers.network.NetworkCore;
import com.upec.peers.network.nio.SerializerBuffer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.text.MessageFormat;

public class Main {

	public static void main(String[] args) throws Exception {
		var serverPort = 2020;
		var core = new NetworkCore(serverPort);
		var peerInterface = new NetworkInterface(core);
		core.regesterObserver(peerInterface);
		core.execute();
		core.instantiateConnection("localhost", 2020);
//		core.instantiateConnection("prog-reseau-m1.lacl.fr", 5486);

	}

	/**
	 * Reads the relative path to the resource directory from the <code>RESOURCE_PATH</code> file located in
	 * <code>src/main/resources</code>
	 * @return the relative path to the <code>resources</code> in the file system, or
	 *         <code>null</code> if there was an error
	 */
	private static String getResourcePath() {
		try {
			URI resourcePathFile = System.class.getResource("/RESOURCE_PATH").toURI();
			String resourcePath = Files.readAllLines(Paths.get(resourcePathFile)).get(0);
			URI rootURI = new File("").toURI();
			URI resourceURI = new File(resourcePath).toURI();
			URI relativeResourceURI = rootURI.relativize(resourceURI);
			return relativeResourceURI.getPath();
		} catch (Exception e) {
			return null;
		}
	}
}
