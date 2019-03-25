package com.upec.peers.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeersConnectedManager implements Runnable {
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private ByteBuffer byteBuffer;

	private volatile boolean running;

	private List<String> knownPeers;


	public PeersConnectedManager(int listeningPort) throws IOException {
		this.knownPeers = Collections.synchronizedList(new ArrayList<>());
		this.serverSocketChannel = ServerSocketChannel.open();
		this.selector = Selector.open();
		this.byteBuffer = ByteBuffer.allocateDirect(512);
		this.running = true;

		SocketAddress socketAddress = new InetSocketAddress(listeningPort);
		serverSocketChannel.bind(socketAddress);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

	}

	private void accept() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		System.out.println("Nouvelle connection" + sc);
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
	}

	private void read(SelectionKey sk) throws IOException {
		SocketChannel sc = (SocketChannel) sk.channel();
		int n = sc.read(byteBuffer);
		if (n < 0) {
			System.out.println("Client Leave");
			sk.cancel();
			sc.close();
			return;
		}

		byteBuffer.flip();
		Charset c = Charset.forName("UTF-8");
		CharBuffer cb = c.decode(byteBuffer);
		System.out.println("Message: " + cb.toString());
		byteBuffer.rewind();
		sc.write(byteBuffer);
		byteBuffer.clear();
	}

	@Override
	public void run() {
		while (this.running) {
			try {
				selector.select();
				for (SelectionKey sk : selector.selectedKeys()) {
					if (sk.isAcceptable()) {
						this.accept();
					} else {
						this.read(sk);
					}
				}
				selector.selectedKeys().clear();
			} catch (IOException e) {
				e.printStackTrace();
				this.terminate();
			}
		}
	}

	public void terminate() {
		this.running = false;
		try {
			this.serverSocketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}
}
