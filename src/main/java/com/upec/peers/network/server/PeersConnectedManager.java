package com.upec.peers.network.server;

import com.upec.peers.network.objects.PeerAddress;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class PeersConnectedManager implements Runnable {
	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private ByteBuffer byteBuffer;
	private List<PeerAddress> knownPeers;

	private HashMap<SocketChannel, PeerConnected> connectedPeers;

	public PeersConnectedManager(int listeningPort) throws IOException {

		this.serverSocketChannel = ServerSocketChannel.open();
		this.selector = Selector.open();
		this.byteBuffer = ByteBuffer.allocateDirect(10);
		this.running = true;

		this.knownPeers = Collections.synchronizedList(new ArrayList<>());
		SocketAddress socketAddress = new InetSocketAddress(listeningPort);
		serverSocketChannel.bind(socketAddress);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		connectedPeers = new HashMap<>();

	}

	public Set<SocketChannel> getPeers() {
		return connectedPeers.keySet();
	}

	public List<PeerAddress> getKnownPeers() {
		return knownPeers;
	}

	private volatile boolean running;

	public void setKnownPeers(List<PeerAddress> p) {
		this.knownPeers = p;
	}

	private void accept() throws IOException {
		SocketChannel sc = serverSocketChannel.accept();
		System.out.println("Nouvelle connection" + sc);
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		connectedPeers.put(sc, new PeerConnected(sc, this));
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

	public void addKnownPeer(String s) {

	}
}
