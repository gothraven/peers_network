package com.upec.peers.network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class PeersConnectedManager implements Runnable {
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private ByteBuffer byteBuffer;
	private HashMap<SocketChannel, PeerConnected> connectedPeers;

	private volatile boolean running;



	public PeersConnectedManager(int listeningPort) throws IOException {

		this.serverSocketChannel = ServerSocketChannel.open();
		this.selector = Selector.open();
		this.byteBuffer = ByteBuffer.allocateDirect(10);
		this.running = true;

		SocketAddress socketAddress = new InetSocketAddress(listeningPort);
		serverSocketChannel.bind(socketAddress);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		connectedPeers = new HashMap<>();

	}

	private void accept() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		System.out.println("Nouvelle connection" + sc);
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		connectedPeers.put(sc, new PeerConnected(sc));
	}

	private void writeData(ByteBuffer response, SocketChannel channel) {
		response.flip();
		while (response.hasRemaining()) {
			try {
				channel.write(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
						//this.read(sk);
						this.connectedPeers.get(sk.channel()).read(sk);
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
